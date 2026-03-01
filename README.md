# MiniERP (Spring Boot)

MiniERP is a lightweight ERP-style backend that covers the core flow most small businesses actually need: customers, products, orders, and inventory. The goal is not to recreate enterprise Software (I enjoy sleep), but to build a clean, well-structured system that proves real backend skills: relational modeling, business rules, and a usable API.

This repo is being built in public as a portfolio project.

## What it does (target scope)
MiniERP focuses on four modules:
Customer management, product catalog, order processing, and inventory tracking.

Once the MVP is complete, it will support a simple order lifecycle where confirming an order checks stock and deducts inventory.

## Tech stack
Java, Spring Boot, PostgreSQL, Docker, Flyway (DB migrations), JUnit (tests), Swagger/OpenAPI (API docs).

## Current status
Planned build order:
Database schema → REST API → business rules (inventory checks) → tests → small demo dataset.

## Quickstart (local)
Prerequisites:
Java 17+ and Docker.

1) Start the database
docker compose up -d

2) Run the app
./mvnw spring-boot:run

3) Open API docs
http://localhost:8080/swagger-ui/index.html

If you don’t have Maven wrapper yet, you can create the project via Spring Initializr and commit the wrapper files with the initial code.

## Configuration
The app will use environment variables or `application.yml` for DB connection. Example values:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/minierp
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

## Core entities (MVP)
Customers
Products (SKU, name, price, active)
Orders (status, createdAt)
Order items (product, qty, unit price)
Inventory (quantity on hand, reorder point)

Optional but likely:
Stock movements (so inventory changes are traceable instead of “magic numbers”).

## API overview (planned)
Customers
POST /customers
GET /customers
GET /customers/{id}

Products
POST /products
GET /products
PATCH /products/{id}

Orders
POST /orders
POST /orders/{id}/items
POST /orders/{id}/confirm
GET /orders?status=

Inventory
GET /inventory
POST /inventory/{productId}/adjust

## Project structure (planned)
src/main/java/.../
controller/
service/
repository/
model/
src/main/resources/
application.yml
db/migration/ (Flyway SQL migrations)

## Roadmap
- [ ] Define schema + migrations (Flyway)
- [ ] Implement CRUD for customers and products
- [ ] Implement order creation + order items
- [ ] Implement order confirmation with inventory checks
- [ ] Add test coverage for business rules
- [ ] Add seed data for instant demo
- [ ] Add basic auth (optional)


## License
MIT
