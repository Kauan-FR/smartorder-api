-- ========================
-- INDEXES: tb_cart_item
-- ========================

-- Speeds up loading a user's cart (most frequent query)
CREATE INDEX idx_cart_item_user_id ON tb_cart_item (user_id);

-- Speeds up checking if a specific product is in any cart
CREATE INDEX idx_cart_item_product_id ON tb_cart_item (product_id);