# MiniERP (Spring Boot)

MiniERP is a lightweight ERP-style backend for core SME workflows: customers, products, orders, inventory, and authentication.

## Tech stack
- Java 17
- Spring Boot 3
- PostgreSQL
- Flyway
- Spring Security + JWT
- Swagger/OpenAPI
- JUnit + Mockito
- Docker / Docker Compose

## Current status
Implemented:
- Customer management (CRUD + soft delete)
- Product catalog (CRUD + soft delete)
- Order lifecycle (draft, add items, confirm, cancel)
- Inventory tracking and stock adjustments
- Auth (register/login) with JWT
- Role-based authorization (ADMIN/USER)
- Flyway migrations for schema + users table

## VS Code workflow

### 1) Open + prepare environment
- Open folder: `/home/runner/work/MiniERP-Springboot/MiniERP-Springboot`
- Install recommended extensions from `.vscode/extensions.json`:
  - `vscjava.vscode-java-pack`
  - `vmware.vscode-spring-boot`
  - `redhat.vscode-yaml`
  - `ms-azuretools.vscode-docker`
  - `humao.rest-client`
  - `mtxr.sqltools`
  - `mtxr.sqltools-driver-pg`
  - `editorconfig.editorconfig`
- Ensure Java 17 and Docker are installed.
- Copy `.env.example` to `.env` and set values:
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/minierp`
  - `SPRING_DATASOURCE_USERNAME=postgres`
  - `SPRING_DATASOURCE_PASSWORD=postgres`
  - `APP_JWT_SECRET=<base64-encoded secret>`

### 2) Run in VS Code
Use Terminal or VS Code tasks (`.vscode/tasks.json`):
- `DB: Up` → `docker compose up -d`
- `App: Run` → `./mvnw spring-boot:run`

Open:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health: `http://localhost:8080/actuator/health`

### 3) Test in VS Code
- Testing panel, task `App: Test`, or terminal:
  - `./mvnw test`
- CI uses verify (`.github/workflows/ci.yml`), so also run:
  - `./mvnw verify`

### 4) Edit effectively in VS Code
- Main code: `src/main/java/com/mogproj/minierp/**`
- Config: `src/main/resources/application.yml`, `application-prod.yml`
- Migrations: `src/main/resources/db/migration/*.sql`
- Tests: `src/test/java/com/mogproj/minierp/**`
- Launch config: `.vscode/launch.json` (`Spring Boot - MiniERP`)

## Security notes
- Development profile allows a local default JWT secret for convenience.
- Production profile requires `APP_JWT_SECRET` to be provided via environment.
- Security rules are configured in `src/main/java/com/mogproj/minierp/security/SecurityConfig.java`; review per-endpoint role access before exposing new APIs.

## API overview

### Authentication
- `POST /auth/register`
- `POST /auth/login`

### Customers
- `POST /customers`
- `GET /customers`
- `GET /customers/{id}`
- `PATCH /customers/{id}`
- `DELETE /customers/{id}`

### Products
- `POST /products`
- `GET /products`
- `GET /products/{id}`
- `PATCH /products/{id}`
- `DELETE /products/{id}`

### Orders
- `POST /orders`
- `POST /orders/{id}/items`
- `POST /orders/{id}/confirm`
- `POST /orders/{id}/cancel`
- `GET /orders`
- `GET /orders/{id}`

### Inventory
- `GET /inventory`
- `POST /inventory/{productId}/adjust`

## License
MIT
