-- ========================
-- INDEXES: tb_review_like
-- ========================

-- Speeds up counting likes for a review
CREATE INDEX idx_review_like_review_id ON tb_review_like (review_id);

-- Speeds up checking if a user already liked a review
CREATE INDEX idx_review_like_user_id ON tb_review_like (user_id);