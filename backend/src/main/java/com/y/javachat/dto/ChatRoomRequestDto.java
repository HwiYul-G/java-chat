package com.y.javachat.dto;

import jakarta.annotation.Nullable;

public record ChatRoomRequestDto(
        boolean isGroup,
        Long userId,
        @Nullable String roomName, // 그룹 채팅방 일 때 필요
        @Nullable Long friendId   // 개인 채팅방 일 때 필요
) {
}
