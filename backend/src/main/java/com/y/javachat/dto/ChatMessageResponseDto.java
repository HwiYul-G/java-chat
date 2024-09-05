package com.y.javachat.dto;

import com.y.javachat.model.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponseDto(
        Long id,
        Long roomId,
        Long senderId,
        String senderName,
        String content,
        LocalDateTime createdAt,
        ChatMessage.MessageType type
) {
}