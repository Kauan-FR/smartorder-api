CREATE TABLE tb_product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    category_id BIGINT NOT NULL,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
            REFERENCES tb_category (id)
            ON DELETE RESTRICT
);