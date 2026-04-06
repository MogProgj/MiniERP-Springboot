# MiniERP (Spring Boot)

MiniERP is a lightweight ERP backend built with Spring Boot. It provides authentication plus core business modules for customers, products, orders, and inventory.

## Tech stack
- Java 17
- Spring Boot 3.2.5
- Spring Security + JWT (JJWT)
- Spring Data JPA + PostgreSQL
- Flyway migrations
- Swagger / OpenAPI (springdoc)
- Maven Wrapper (`./mvnw`)
- Docker

## Implemented modules
- **Authentication**: login endpoint and JWT-based authorization.
- **Customers**: create, list, retrieve, and soft-delete customers.
- **Products**: create, list/filter, retrieve, and soft-delete products.
- **Orders**: create orders, add items, and confirm with stock validation.
- **Inventory**: stock tracking and adjustment flow used by order confirmation.

## Documentation & Roadmap 🚀
We are actively building MiniERP into a commercial-grade application.
All project documentation, including the Product Roadmap, Backlog, System Architecture, Decisions (ADRs), and Database Schema, has been consolidated into a single document:
- **[Project Documentation](docs/project-documentation.md)**

## Running locally
1. Copy env template:
   ```bash
   cp .env.example .env
   ```
2. Start PostgreSQL:
   ```bash
   docker compose up -d
   ```
3. Run the API:
   ```bash
   ./mvnw spring-boot:run
   ```

## Test and verification
```bash
./mvnw -B test
./mvnw -B verify
```

## API and health URLs
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Health: http://localhost:8080/actuator/health

## Environment variables
Use `.env.example` as the tracked template and keep `.env` local-only.

- `SPRING_DATASOURCE_URL` (default local: `jdbc:postgresql://localhost:5433/minierp`)
- `SPRING_DATASOURCE_USERNAME` (default local: `postgres`)
- `SPRING_DATASOURCE_PASSWORD` (default local: `postgres`)
- `APP_JWT_SECRET` (required for token signing)

## Docker build
```bash
docker build -t minierp:local .
```

## License
MIT
