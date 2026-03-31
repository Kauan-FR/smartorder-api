-- ========================
-- INDEXES: tb_chat_message
-- ========================

-- Speeds up loading messages sent by a user
CREATE INDEX idx_chat_message_sender_id ON tb_chat_message (sender_id);

-- Speeds up loading messages received by a user
CREATE INDEX idx_chat_message_receiver_id ON tb_chat_message (receiver_id);

-- Speeds up loading chat history for a specific product
CREATE INDEX idx_chat_message_product_id ON tb_chat_message (product_id);

-- Speeds up loading conversation between two users about a product (most frequent query)
CREATE INDEX idx_chat_message_conversation ON tb_chat_message (sender_id, receiver_id, product_id);

-- Speeds up filtering unread messages
CREATE INDEX idx_chat_message_is_read ON tb_chat_message (receiver_id, is_read);