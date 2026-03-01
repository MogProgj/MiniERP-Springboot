# Architecture Decision Records

> Public build started on 2026-03-01.

## ADR 001: PostgreSQL + Flyway

**Status:** Accepted

**Context:**
The project needs a relational database that supports constraints, joins, and
transactional integrity. Schema changes must be versioned and repeatable across
environments.

**Decision:**
Use PostgreSQL as the primary database and Flyway for versioned SQL migrations.
A local Postgres instance runs via Docker Compose on port 5433 to avoid
conflicts with any host-level PostgreSQL.

**Consequences:**
- Every schema change is tracked in `src/main/resources/db/migration/`.
- Local setup requires only `docker compose up -d` before running the app.
- CI can use a service container or an embedded DB for tests when needed.

---

## ADR 002: Inventory deducted on order confirmation

**Status:** Accepted

**Context:**
When an order moves from DRAFT to CONFIRMED, the system must reduce
`quantity_on_hand` for each ordered product. This is a core business rule.

**Decision:**
Inventory deduction happens in the service layer when the order status
transitions to CONFIRMED. The service checks stock availability and
throws an exception if any product has insufficient inventory.

**Consequences:**
- Business logic lives in the service layer, not in database triggers.
- The confirmation endpoint is the single place where stock is validated.
- Future enhancements (e.g., stock-movement audit log) can hook into
  the same service method.
