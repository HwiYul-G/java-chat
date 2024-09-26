package com.y.javachat.dto.external;

public record OutputQueueResultDto(
        Long messageId,
        boolean isBadWord
) {
}
