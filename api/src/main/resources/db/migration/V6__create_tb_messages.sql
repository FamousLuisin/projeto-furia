CREATE TABLE tb_messages (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    edited_at TIMESTAMP WITH TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE,
    chat_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    is_bot BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_message_chat
        FOREIGN KEY (chat_id)
        REFERENCES tb_chats(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id)
        REFERENCES tb_users(id)
        ON DELETE SET NULL
);