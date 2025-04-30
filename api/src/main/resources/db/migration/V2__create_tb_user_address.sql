CREATE TABLE tb_user_address (
    id UUID PRIMARY KEY,
    postal_code VARCHAR(20) NOT NULL,
    number VARCHAR(10) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES tb_users(id)
        ON DELETE CASCADE
);