CREATE TABLE tb_chat_members (
    user_id UUID NOT NULL,
    chat_id UUID NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(50),
    PRIMARY KEY (user_id, chat_id),
    CONSTRAINT fk_member_user
        FOREIGN KEY (user_id)
        REFERENCES tb_users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_member_chat
        FOREIGN KEY (chat_id)
        REFERENCES tb_chats(id)
        ON DELETE CASCADE
);