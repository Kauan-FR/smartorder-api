-- ========================
-- TABLE: tb_chat_message
-- ========================
CREATE TABLE tb_chat_message (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_chat_message_sender
        FOREIGN KEY (sender_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_chat_message_receiver
        FOREIGN KEY (receiver_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_chat_message_product
        FOREIGN KEY (product_id)
            REFERENCES tb_product (id)
            ON DELETE CASCADE
);