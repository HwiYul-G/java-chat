package com.y.aiserver.dto;

public record InputQueueMessageDto(
        Long messageId,
        String content
) {
}
