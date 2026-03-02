-- ========================
-- INDEXES: tb_address
-- ========================

-- Speeds up finding all addresses for a user
CREATE INDEX idx_address_user_id ON tb_address (user_id);

-- Speeds up filtering by city
CREATE INDEX idx_address_city ON tb_address (LOWER(city));

-- Speeds up filtering by state
CREATE INDEX idx_address_state ON tb_address (LOWER(state));

-- Speeds up search by zip code
CREATE INDEX idx_address_zip_code ON tb_address (zip_code);