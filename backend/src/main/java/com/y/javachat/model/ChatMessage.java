package com.y.javachat.model;

import com.y.javachat.dto.ChatMessageResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseModel {
    @NotNull(message = "메시지를 보내는 사람 id가 필요합니다.")
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @NotNull(message = "채팅방 id가 필요합니다.")
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "content", length = 2024)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 256)
    private MessageType messageType;

    @Column(name = "is_detected")
    private boolean isDetected;

    @Column(name = "is_badword")
    private boolean isBadWord;

    public enum MessageType {
        CONNECT,
        DISCONNECT,
        CHAT,
        DATE
    }

    public ChatMessageResponseDto toChatMessageResponseDto() {
        return new ChatMessageResponseDto(
                this.getId(),
                this.getRoomId(),
                this.getSender().getId(),
                this.getSender().getUsername(),
                this.getContent(),
                this.isDetected,
                this.isBadWord,
                this.getCreatedAt(),
                this.getMessageType()
        );
    }
}