# Current Status & Functionality Analysis

_Last reviewed: 2026-03-11 (compatibility re-check)_

## Implementation status (actual)

MiniERP currently includes implemented backend modules for:

- **Customers**: create, read (single/list), update, and soft-delete.
- **Products**: create, read (single/list with filters), update, and deactivate via delete endpoint.
- **Orders**: draft creation, add item, confirm, cancel, read (single/list by status).
- **Inventory**: list inventory and adjust stock by product.
- **Auth/Security**: register/login, JWT-based authentication, role-based route authorization.
- **Platform concerns**: Flyway migrations, OpenAPI/Swagger, global exception handling, actuator health endpoint.

## Key business rules currently enforced

- Orders can receive items only in `DRAFT` status.
- Inactive products cannot be added to an order.
- Empty orders cannot be confirmed.
- Confirming an order decrements stock for each order item.
- Cancelling a confirmed order restores stock for its items.
- Customer deletion is soft-delete based (`deleted_at`) and excluded from active read paths.

## Data model coverage

Flyway migrations currently define:

- `customers` (+ `updated_at`, `deleted_at`)
- `products` (+ `updated_at`)
- `inventory`
- `orders`
- `order_items`
- `users` (for authentication/authorization)

## Validation/test/build status

### What currently works

- The repository includes focused unit/controller tests for customer, product, order, and inventory service behavior.

### Current blocker

- A fresh compatibility re-check confirms `pom.xml` is structurally valid XML and no longer includes the previously invalid `spring-boot-starter-webmvc-test` dependency.
- `mvn validate`/`mvn test` are still blocked in this environment because Maven Central access returns HTTP `403 Forbidden` while resolving the Spring Boot parent POM (`3.2.5`).
- Conclusion: the current blocking issue is environment/repository access, not a malformed POM definition.

## Suggested immediate next actions

1. Re-run `mvn validate` and `mvn test` in CI or a network-permitted environment to confirm full dependency resolution and test pass.
2. Align `README.md` "Current status" section (currently roadmap-oriented) with implemented functionality.
3. Follow `docs/MAVEN_NETWORK_TROUBLESHOOTING.md` and commit a project-local `.mvn/settings.xml` in CI (or use the provided `.mvn/settings.xml.example`).
4. Add a small smoke script or CI workflow to catch dependency-resolution regressions early.
