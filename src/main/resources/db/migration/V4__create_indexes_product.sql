-- ========================
-- INDEXES: tb_product
-- ========================

-- Speeds up product listing filtered by category (very frequent query)
CREATE INDEX idx_product_category_id ON tb_product (category_id);

-- Speeds up search by product name (partial and case-insensitive)
CREATE INDEX idx_product_name ON tb_product (LOWER(name));

-- Speeds up filtering active/inactive products
CREATE INDEX idx_product_active ON tb_product (active);

-- Composite index: active products by category (common query in e-commerce)
CREATE INDEX idx_product_category_active ON tb_product (category_id, active);

-- Speeds up price range filtering and sorting
CREATE INDEX idx_product_price ON tb_product (price);