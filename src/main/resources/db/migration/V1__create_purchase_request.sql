CREATE TABLE purchase_request (id UUID PRIMARY KEY,
    business_key TEXT NOT NULL UNIQUE,
    status TEXT NOT NULL,
    product_name TEXT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    manager_approved BOOLEAN NOT NULL DEFAULT FALSE,
    stock_available BOOLEAN NOT NULL DEFAULT FALSE,
    delivery_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT purchase_request_status_known
    CHECK (status IN ('DRAFT','SUBMITTED','APPROVED','ORDERED','COMPLETED')));