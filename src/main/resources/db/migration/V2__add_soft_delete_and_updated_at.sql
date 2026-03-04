ALTER TABLE customers ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ;
ALTER TABLE customers ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMPTZ;
ALTER TABLE products  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ;

CREATE INDEX IF NOT EXISTS idx_customers_deleted_at ON customers(deleted_at)
    WHERE deleted_at IS NULL;
