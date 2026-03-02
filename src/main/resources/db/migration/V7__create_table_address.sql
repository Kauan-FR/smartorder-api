-- ========================
-- TABLE: tb_address
-- ========================
CREATE TABLE tb_address (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(200) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL DEFAULT 'Brasil',
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user(id)
            ON DELETE CASCADE
);