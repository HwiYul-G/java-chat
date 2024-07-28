package com.y.javachat.app.dto;

public record FriendResponseDto(
        Long userId,
        String name,
        String email,
        Long chatRoomId
) {
}
