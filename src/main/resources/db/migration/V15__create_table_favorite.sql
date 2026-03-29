-- ========================
-- TABLE: tb_favorite
-- ========================
CREATE TABLE IF NOT EXISTS tb_favorite (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    added_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_favorite_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_favorite_product
        FOREIGN KEY (product_id)
            REFERENCES tb_product (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_favorite_user_product
        UNIQUE (user_id, product_id)
);