package com.y.aiserver.dto;

public record PredictResponseDto(
        boolean isBadWord,
        String content
) {
}
