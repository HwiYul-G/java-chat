CREATE TABLE chat_join
(
    id          BIGINT   NOT NULL,
    created_at  datetime NOT NULL,
    modified_at datetime NULL,
    user_id     BIGINT   NOT NULL,
    room_id     BIGINT   NOT NULL,
    CONSTRAINT pk_chat_join PRIMARY KEY (id)
);

ALTER TABLE chat_join
    ADD CONSTRAINT FK_CHAT_JOIN_ON_ROOM FOREIGN KEY (room_id) REFERENCES chat_room (id);

ALTER TABLE chat_join
    ADD CONSTRAINT FK_CHAT_JOIN_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (id);

CREATE TABLE chat_message
(
    id           BIGINT        NOT NULL,
    created_at   datetime      NOT NULL,
    modified_at  datetime      NULL,
    sender_id    BIGINT        NOT NULL,
    room_id      BIGINT        NOT NULL,
    content      VARCHAR(2024) NULL,
    message_type VARCHAR(256)  NULL,
    CONSTRAINT pk_chat_message PRIMARY KEY (id)
);

ALTER TABLE chat_message
    ADD CONSTRAINT FK_CHAT_MESSAGE_ON_SENDER FOREIGN KEY (sender_id) REFERENCES app_user (id);

CREATE TABLE chat_room
(
    id          BIGINT       NOT NULL,
    created_at  datetime     NOT NULL,
    modified_at datetime     NULL,
    room_name   VARCHAR(256) NULL,
    manager_id  BIGINT       NULL,
    is_group    BIT(1)       NULL,
    CONSTRAINT pk_chat_room PRIMARY KEY (id)
);

CREATE TABLE friendship
(
    id           BIGINT   NOT NULL,
    created_at   datetime NOT NULL,
    modified_at  datetime NULL,
    user_id      BIGINT   NULL,
    friend_id    BIGINT   NULL,
    chat_room_id BIGINT   NULL,
    CONSTRAINT pk_friendship PRIMARY KEY (id)
);

ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_FRIEND FOREIGN KEY (friend_id) REFERENCES app_user (id);

ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_USER FOREIGN KEY (user_id) REFERENCES app_user (id);

CREATE TABLE notification
(
    id                BIGINT       NOT NULL,
    created_at        datetime     NOT NULL,
    modified_at       datetime     NULL,
    receiver_id       BIGINT       NULL,
    sender_id         BIGINT       NULL,
    content           VARCHAR(512) NULL,
    is_read           BIT(1)       NULL,
    status            VARCHAR(256) NULL,
    notification_type VARCHAR(256) NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);

CREATE TABLE app_user
(
    id          BIGINT       NOT NULL,
    created_at  datetime     NOT NULL,
    modified_at datetime     NULL,
    email       VARCHAR(256) NULL,
    username    VARCHAR(256) NULL,
    password    VARCHAR(256) NULL,
    enabled     BIT(1)       NULL,
    roles       VARCHAR(256) NULL,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);