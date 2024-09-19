package com.y.javachat.dto;

public record WarningMessageDto(
        long senderId,
        String warningMessage
) {
}
