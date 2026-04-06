# QA Diagnosis Verification (branch-local)

Date: 2026-04-06

This note verifies the four reported major issues against current code in this branch.

## Verdicts

1. **Returning JPA entities directly from API** — **True**.
2. **Soft-deleted customers usable in order creation** — **True**.
3. **Invalid order state transitions become HTTP 500** — **True**.
4. **JWT secret predictable fallback in default config** — **True**.

## Evidence Summary

- Controllers return entity types (`Order`, `Product`, `Customer`, `Inventory`) directly.
- `Order` and `OrderItem` have bidirectional references and lazy relations.
- `OrderService.createDraft` uses `customerRepository.findById(...)` instead of `findActiveById(...)`.
- `Order.confirm()` and `Order.cancel()` throw `IllegalStateException`.
- `GlobalExceptionHandler` has no explicit `IllegalStateException` handler, so it falls through to generic 500 handler.
- `application.yml` sets `app.security.jwt.secret` with a concrete default fallback value.
