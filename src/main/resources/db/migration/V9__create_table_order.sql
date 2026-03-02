-- ========================
-- TABLE: tb_order
-- ========================
CREATE TABLE tb_order (
    id BIGSERIAL PRIMARY KEY,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    user_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,

    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user(id)
            ON DELETE RESTRICT ,

    CONSTRAINT fk_order_address
        FOREIGN KEY (address_id)
            REFERENCES tb_address(id)
            ON DELETE RESTRICT
);