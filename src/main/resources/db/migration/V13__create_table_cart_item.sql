-- ========================
-- TABLE: tb_cart_item
-- ========================
CREATE TABLE tb_cart_item (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_item_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_cart_item_product
        FOREIGN KEY (product_id)
            REFERENCES tb_product (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_cart_item_user_product
        UNIQUE (user_id, product_id)
)
