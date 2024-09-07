package com.y.javachat.dto;

import com.y.javachat.model.ChatMessage;

public record ChatMessageRequestDto(
        Long roomId,
        Long senderId,
        String senderName,
        String content,
        ChatMessage.MessageType type
) {
}
