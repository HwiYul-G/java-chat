package com.y.javachat.dto;

public record EnterChatRoomRequestDto(
        Long userId,
        boolean isGroup
) {
}
