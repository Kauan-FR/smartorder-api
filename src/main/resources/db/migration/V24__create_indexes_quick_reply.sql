-- ========================
-- INDEXES: tb_quick_reply
-- ========================

-- Speeds up loading a vendor's quick replies
CREATE INDEX idx_quick_reply_user_id ON tb_quick_reply (user_id);