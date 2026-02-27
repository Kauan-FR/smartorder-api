-- ========================
-- INDEXES: tb_category (retroactive)
-- ========================

-- Speeds up search by category name
CREATE INDEX idx_category_name ON tb_category (LOWER(name));