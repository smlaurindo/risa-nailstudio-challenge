CREATE TABLE services (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    price_cents INTEGER NOT NULL,
    icon VARCHAR(50) NOT NULL,
    CONSTRAINT chk_duration_positive CHECK (duration_minutes > 0),
    CONSTRAINT chk_price_positive CHECK (price_cents >= 0)
);