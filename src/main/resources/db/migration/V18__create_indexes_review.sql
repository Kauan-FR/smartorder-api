-- ========================
-- INDEXES: tb_review
-- ========================

-- Speeds up loading all reviews for a product (most frequent query)
CREATE INDEX idx_review_product_id ON tb_review (product_id);

-- Speeds up loading all reviews by a user
CREATE INDEX idx_review_user_id ON tb_review (user_id);

-- Speeds up sorting/filtering by rating
CREATE INDEX idx_review_rating ON tb_review (rating);