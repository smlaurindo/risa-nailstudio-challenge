-- Default admin user (email: admin@risanailstudio.com, password: Admin@123)
-- Default customer (email: customer@example.com, password: Customer@123)

INSERT INTO users (id, email, password_hash, role) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin@risanailstudio.com', '$2a$16$rhniAZ13B13WtuxQW5g1vuHrm.RFyTybFH4bM8YEZsCxcq6LY.542', 'ADMIN');

INSERT INTO admins (id, user_id, name) VALUES
('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', 'Administrador');

-- Demo customer (email: customer@example.com, password: Customer@123)
INSERT INTO users (id, email, password_hash, role) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'customer@example.com', '$2a$16$gvuEx0aT.6/.Lja/ve0useQMW2Kk60kTYOK87X9ZiXaqG6UT747ke', 'CUSTOMER');

INSERT INTO customers (id, user_id, name, photo) VALUES
('550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440010', 'Cliente Demo', 'https://i.pravatar.cc/300?img=1');

INSERT INTO services (id, name, duration_minutes, price_cents, icon) VALUES
('550e8400-e29b-41d4-a716-446655440100', 'Manicure Básica', 45, 3500, 'NAIL'),
('550e8400-e29b-41d4-a716-446655440101', 'Pedicure Básica', 50, 4000, 'FOOT'),
('550e8400-e29b-41d4-a716-446655440102', 'Manicure + Pedicure', 90, 7000, 'NAIL_FOOT'),
('550e8400-e29b-41d4-a716-446655440103', 'Esmaltação em Gel', 60, 5500, 'GEL'),
('550e8400-e29b-41d4-a716-446655440104', 'Unhas Decoradas', 75, 8000, 'ART'),
('550e8400-e29b-41d4-a716-446655440105', 'Alongamento de Unhas', 120, 12000, 'EXTENSION'),
('550e8400-e29b-41d4-a716-446655440106', 'Spa dos Pés', 60, 6500, 'SPA'),
('550e8400-e29b-41d4-a716-446655440107', 'Hidratação de Mãos', 30, 2500, 'HAND');

INSERT INTO appointments (id, customer_id, service_id, starts_at, ends_at, status, confirmed_at, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440200', 
 '550e8400-e29b-41d4-a716-446655440011',
 '550e8400-e29b-41d4-a716-446655440100',
 CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '10 hours',
 CURRENT_TIMESTAMP + INTERVAL '2 days' + INTERVAL '10 hours 45 minutes',
 'CONFIRMED',
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP);

INSERT INTO appointments (id, customer_id, service_id, starts_at, ends_at, status, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440201',
 '550e8400-e29b-41d4-a716-446655440011',
 '550e8400-e29b-41d4-a716-446655440103',
 CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '14 hours',
 CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '15 hours',
 'PENDING',
 CURRENT_TIMESTAMP);

INSERT INTO appointments (id, customer_id, service_id, starts_at, ends_at, status, cancelled_at, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440202',
 '550e8400-e29b-41d4-a716-446655440011',
 '550e8400-e29b-41d4-a716-446655440102',
 CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '16 hours',
 CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '17 hours 30 minutes',
 'CANCELLED',
 CURRENT_TIMESTAMP - INTERVAL '4 days',
 CURRENT_TIMESTAMP - INTERVAL '7 days');
