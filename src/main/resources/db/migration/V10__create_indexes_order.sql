-- ========================
-- INDEXES: tb_order
-- ========================

-- Speeds up finding all orders for a user
CREATE INDEX idx_order_user_id ON tb_order (user_id);

-- Speeds up filtering orders by status
CREATE INDEX idx_order_status ON tb_order (status);

-- Speeds up finding orders by address
CREATE INDEX idx_order_address_id ON tb_order (address_id);

-- Speeds up sorting and filtering orders by date
CREATE INDEX idx_order_date ON tb_order (order_date DESC);

-- Composite: user's orders by status (e.g., "my pending orders")
CREATE INDEX idx_order_user_status ON tb_order (user_id, status);