-- ========================
-- TABLE: tb_review_like
-- ========================
CREATE TABLE tb_review_like (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    review_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_like_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_review_like_review
        FOREIGN KEY (review_id)
            REFERENCES tb_review (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_review_like_user_review
        UNIQUE (user_id, review_id)
);