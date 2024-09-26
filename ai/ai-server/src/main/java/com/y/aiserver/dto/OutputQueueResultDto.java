package com.y.aiserver.dto;

public record OutputQueueResultDto(
        Long messageId,
        boolean isBadWord
) {
}
