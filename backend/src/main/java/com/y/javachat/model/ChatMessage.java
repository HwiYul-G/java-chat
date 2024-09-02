package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseModel {
    @NotNull(message = "메시지를 보내는 사람 id가 필요합니다.")
    @Column(name = "sender_id")
    private Long senderId; // User와 연관지을지 혹은 senderName을 추가할 지 정의

    @NotNull(message = "채팅방 id가 필요합니다.")
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "content", length = 2024)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 256)
    private MessageType messageType;

    public enum MessageType {
        CONNECT,
        DISCONNECT,
        CHAT,
        DATE
    }
}
