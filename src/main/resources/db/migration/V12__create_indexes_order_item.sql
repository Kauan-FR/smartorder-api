-- ========================
-- INDEXES: tb_order_item
-- ========================

-- Speeds up finding all items in an order
CREATE INDEX idx_order_item_order_id ON tb_order_item (order_id);

-- Speeds up finding which orders contain a specific product
CREATE INDEX idx_order_item_product_id ON tb_order_item (product_id);