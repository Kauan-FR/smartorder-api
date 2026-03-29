-- ========================
-- INDEXES: tb_favorite
-- ========================

-- Speeds up loading a user's favorites list
CREATE INDEX idx_favorite_user_id ON tb_favorite (user_id);

-- Speeds up checking if a product is favorited
CREATE INDEX idx_favorite_product_id ON tb_favorite (product_id);