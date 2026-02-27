-- ========================
-- INDEXES: tb_user
-- ========================

-- Speeds up login and user lookup by email (most frequent query)
CREATE INDEX idx_user_email ON tb_user (LOWER(email));

-- Speeds up filtering users by role (ADMIN vs CUSTOMER)
CREATE INDEX idx_user_role ON tb_user (role);

-- Speeds up search by username
CREATE INDEX idx_user_name ON tb_user (LOWER(name));