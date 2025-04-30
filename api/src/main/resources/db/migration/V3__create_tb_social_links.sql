CREATE TABLE tb_social_links (
    id UUID PRIMARY KEY,
    platform VARCHAR(50) NOT NULL,
    url VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_social_user
        FOREIGN KEY (user_id)
        REFERENCES tb_users(id)
        ON DELETE CASCADE
);