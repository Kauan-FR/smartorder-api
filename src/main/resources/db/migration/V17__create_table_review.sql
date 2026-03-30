-- ========================
-- TABLE: tb_review
-- ========================
CREATE TABLE tb_review (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating INTEGER NOT NULL,
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_review_product
        FOREIGN KEY (product_id)
            REFERENCES tb_product (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_review_user_product
        UNIQUE (user_id, product_id),

    CONSTRAINT ck_review_rating
        CHECK (rating >= 1 AND rating <= 5)
);