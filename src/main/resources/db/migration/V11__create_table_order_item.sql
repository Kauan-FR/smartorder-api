-- ========================
-- TABLE: tb_order_item
-- ========================
CREATE TABLE tb_order_item (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
            REFERENCES tb_order(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_order_item_product
        FOREIGN KEY (product_id)
            REFERENCES tb_product(id)
            ON DELETE RESTRICT
);