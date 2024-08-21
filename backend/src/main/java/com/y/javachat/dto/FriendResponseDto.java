package com.y.javachat.dto;

public record FriendResponseDto(
        Long userId,
        String name,
        String email,
        Long chatRoomId
) {
}
