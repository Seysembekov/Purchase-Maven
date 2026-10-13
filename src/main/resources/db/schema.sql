CREATE TABLE purchase_request
(id uuid PRIMARY KEY,business_key text NOT NULL UNIQUE,status text NOT NULL, product_name text NOT NULL, quantity integer NOT NULL CHECK (quantity > 0), price        numeric(12, 2) NOT NULL CHECK (price >= 0), created_at timestamptz NOT NULL DEFAULT now(),
 CONSTRAINT purchase_request_status_known
     CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'ORDERED','COMPLETED'))
);