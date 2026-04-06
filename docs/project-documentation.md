# MiniERP Project Documentation

This is the single source of truth for the project's strategy, requirements, architecture, and database mapping. It combines the roadmap, backlog, system architecture decisions, and schema into one unified document to ensure there are no redundant or conflicting files.

---

## Part 1. Commercialization Roadmap 🚀

This section outlines the strategic phases required to transform the current MiniERP codebase from its existing foundation into a robust, fully functional commercial Enterprise Resource Planning (ERP) application.

### 🏗️ Current State Analysis
MiniERP is currently a lightweight API backend (Spring Boot 3.2.5, Java 17, PostgreSQL).
- **Implemented:** Security (JWT), Customers (CRUD), Products (Catalog), Orders (Draft vs Confirm), Inventory (Stock adjustments).
- **Missing:** Financials (Invoicing/Payments), Advanced Supply Chain (Purchasing/Shipping), UI, Observability, Mail/Notifications.

### 🗺️ Execution Plan: Phased Workload

1. **Phase 1: Core Hardening & Observability (Current - Month 1)**
   - **Goal:** Make the existing API robust, observable, and ready for continuous deployment.
   - **Tasks:** Setup CI/CD (GitHub Actions), structured logging, centralized exceptions, Swagger enhancements, DB backups.
   - **Tools:** GitHub Actions, Docker, Flyway, SLF4J, Micrometer.

2. **Phase 2: Financial & Commerce Capabilities (Month 2 - Month 3)**
   - **Goal:** Processing money, generating invoices, and handling taxes.
   - **Tasks:** Invoicing (PDF output), Payments Integration (Stripe/PayPal), Tax Engine, Discounts.
   - **Tools:** Stripe/PayPal Java SDKs, iText/Thymeleaf.

3. **Phase 3: Advanced Supply Chain (Month 3 - Month 4)**
   - **Goal:** Manage suppliers, purchasing, and fulfillment.
   - **Tasks:** Supplier directory, Purchase Orders (POs), Shipping & Fulfillment tracking, Multi-Warehouse.
   - **Tools:** Shippo/EasyPost APIs, Spring Data JPA.

4. **Phase 4: Communications & User Engagement (Month 4 - Month 5)**
   - **Goal:** Automate interactions with users and customers.
   - **Tasks:** Transactonal Email Service, Notification Hub, Audit Logs (Hibernate Envers).
   - **Tools:** Spring Boot Mail, SendGrid/Mailgun API, RabbitMQ/Kafka, Envers.

5. **Phase 5: Front-End User Interface (Month 5 - Month 7)**
   - **Goal:** Provide a usable graphical interface for end-users.
   - **Tasks:** SPA dashboard, dynamic tables, charts, integrated JWT auth flow.
   - **Tools:** React/Vue, Tailwind CSS, Axios, Chart.js.

6. **Phase 6: Cloud Deployment & Multi-Tenancy (Month 7+)**
   - **Goal:** Scale the application as a SaaS product.
   - **Tasks:** Multi-Tenancy (Tenant IDs), Managed Cloud hosting, scale monitoring.
   - **Tools:** AWS/Azure/GCP, Terraform, Kubernetes, Datadog/Prometheus.

---

## Part 2. Product Requirements & Backlog 📋

### Functional Requirements (Epics)
- **1. User & Role Management:** Basic JWT (Done). Needs Granular Permissions, Profile Management, Account lockouts.
- **2. CRM:** Customer CRUD (Done). Needs Customer Types, Order History, Contact Logging.
- **3. Catalog & Inventory:** CRUD & Base Inventory (Done). Needs Categories/Tags, Multi-Warehouse, Low-stock alerts, Barcode API.
- **4. Sales & Orders:** Draft/Confirm cycle (Done). Needs Full Status lifecycle, Discounts, Shipping Calculation.
- **5. Financials & Billing:** Needs Invoices, Payment Processing, Tax Engine, Accounts Receivable tracking.
- **6. Purchasing:** Needs Supplier Directory, PO creation, Goods Receiving.
- **7. Reporting:** Needs Dashboard API, CSV/Excel Export, P&L summaries.

### Non-Functional Requirements
- **Security & Compliance:** GDPR compliance, Rate Limiting, Automated Vuln Scanning, Audit Logging.
- **Performance:** Response < 200ms, advanced pagination/filtering, Redis caching.
- **Multi-Tenancy:** Logical DB separation, Subdomain routing.

---

## Part 3. Architecture & Decisions 🏗️

The project is intentionally small, but structured like production code with clear separation of concerns (API â†’ Service â†’ Data).

### Architectural Decision Records (ADRs)

**ADR 001: PostgreSQL + Flyway**
- **Decision:** Use PostgreSQL as the primary database and Flyway for versioned SQL migrations.
- **Consequences:** Schema changes are tracked in `db/migration/`. Local setup via `docker compose up -d`.

**ADR 002: Inventory deducted on order confirmation**
- **Decision:** Inventory deduction happens in the service layer when the order status transitions to CONFIRMED.
- **Consequences:** Business logic lives in the service layer, not DB triggers. Centralized validation point.

---

## Part 4. Database Schema 🗄️

### `customers`
| Column     | Type         | Constraints              |
|------------|--------------|--------------------------|
| id         | BIGSERIAL    | PRIMARY KEY              |
| name       | TEXT         | NOT NULL                 |
| email      | TEXT         | UNIQUE                   |
| phone      | TEXT         |                          |
| created_at | TIMESTAMPTZ  | NOT NULL, DEFAULT now()  |

### `products`
| Column     | Type         | Constraints                       |
|------------|--------------|-----------------------------------|
| id         | BIGSERIAL    | PRIMARY KEY                       |
| sku        | TEXT         | NOT NULL, UNIQUE                  |
| name       | TEXT         | NOT NULL                          |
| price_cents| INTEGER      | NOT NULL, CHECK (price_cents >= 0)|
| active     | BOOLEAN      | NOT NULL, DEFAULT true            |
| created_at | TIMESTAMPTZ  | NOT NULL, DEFAULT now()           |

### `inventory`
| Column           | Type         | Constraints                                |
|------------------|--------------|--------------------------------------------|
| product_id       | BIGINT       | PRIMARY KEY, REFERENCES products(id)       |
| quantity_on_hand | INTEGER      | NOT NULL, DEFAULT 0, CHECK (>= 0)          |
| reorder_point    | INTEGER      | NOT NULL, DEFAULT 0, CHECK (>= 0)          |
| updated_at       | TIMESTAMPTZ  | NOT NULL, DEFAULT now()                    |

### `orders`
| Column      | Type         | Constraints                                         |
|-------------|--------------|-----------------------------------------------------|
| id          | BIGSERIAL    | PRIMARY KEY                                         |
| customer_id | BIGINT       | NOT NULL, REFERENCES customers(id)                  |
| status      | TEXT         | NOT NULL, CHECK (IN 'DRAFT','CONFIRMED','CANCELLED')|
| created_at  | TIMESTAMPTZ  | NOT NULL, DEFAULT now()                             |
*(Indexes: `idx_orders_customer`, `idx_orders_status`)*

### `order_items`
*(Composite PK: order_id, product_id)*
| Column          | Type    | Constraints                                    |
|-----------------|---------|------------------------------------------------|
| order_id        | BIGINT  | NOT NULL, REFERENCES orders(id) ON DELETE CASCADE |
| product_id      | BIGINT  | NOT NULL, REFERENCES products(id)              |
| qty             | INTEGER | NOT NULL, CHECK (qty > 0)                      |
| unit_price_cents| INTEGER | NOT NULL, CHECK (unit_price_cents >= 0)        |
