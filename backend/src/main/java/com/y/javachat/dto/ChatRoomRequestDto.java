package com.y.javachat.dto;

public record ChatRoomRequestDto(
        boolean isGroup,
        Long userId,
        String roomName, // 그룹 채팅방 일 때 필요
        Long friendId   // 개인 채팅방 일 때 필요
) {
}
