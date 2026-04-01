-- ========================
-- TABLE: tb_quick_reply
-- ========================
CREATE TABLE tb_quick_reply (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_quick_reply_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE
);