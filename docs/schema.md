# Schema

## Entities

### customers
Basic customer record.

| Column     | Type         | Constraints              |
|------------|--------------|--------------------------|
| id         | BIGSERIAL    | PRIMARY KEY              |
| name       | TEXT         | NOT NULL                 |
| email      | TEXT         | UNIQUE                   |
| phone      | TEXT         |                          |
| created_at | TIMESTAMPTZ  | NOT NULL, DEFAULT now()  |

### products
SKU-based product catalog.

| Column     | Type         | Constraints                       |
|------------|--------------|-----------------------------------|
| id         | BIGSERIAL    | PRIMARY KEY                       |
| sku        | TEXT         | NOT NULL, UNIQUE                  |
| name       | TEXT         | NOT NULL                          |
| price_cents| INTEGER      | NOT NULL, CHECK (price_cents >= 0)|
| active     | BOOLEAN      | NOT NULL, DEFAULT true            |
| created_at | TIMESTAMPTZ  | NOT NULL, DEFAULT now()           |

### inventory
Per-product stock tracking. One row per product.

| Column           | Type         | Constraints                                |
|------------------|--------------|--------------------------------------------|
| product_id       | BIGINT       | PRIMARY KEY, REFERENCES products(id)       |
| quantity_on_hand | INTEGER      | NOT NULL, DEFAULT 0, CHECK (>= 0)         |
| reorder_point    | INTEGER      | NOT NULL, DEFAULT 0, CHECK (>= 0)         |
| updated_at       | TIMESTAMPTZ  | NOT NULL, DEFAULT now()                    |

### orders
Draft → Confirmed → Cancelled workflow.

| Column      | Type         | Constraints                                         |
|-------------|--------------|-----------------------------------------------------|
| id          | BIGSERIAL    | PRIMARY KEY                                         |
| customer_id | BIGINT       | NOT NULL, REFERENCES customers(id)                  |
| status      | TEXT         | NOT NULL, CHECK (IN 'DRAFT','CONFIRMED','CANCELLED')|
| created_at  | TIMESTAMPTZ  | NOT NULL, DEFAULT now()                             |

Indexes: `idx_orders_customer(customer_id)`, `idx_orders_status(status)`.

### order_items
Line items belonging to an order. Composite primary key of (order_id, product_id).

| Column          | Type    | Constraints                                    |
|-----------------|---------|------------------------------------------------|
| order_id        | BIGINT  | NOT NULL, REFERENCES orders(id) ON DELETE CASCADE |
| product_id      | BIGINT  | NOT NULL, REFERENCES products(id)              |
| qty             | INTEGER | NOT NULL, CHECK (qty > 0)                      |
| unit_price_cents| INTEGER | NOT NULL, CHECK (unit_price_cents >= 0)        |

## Relationships

- A **customer** has many **orders**.
- An **order** has many **order_items**.
- A **product** has one **inventory** row.
- An **order_item** references one **product** and one **order**.
