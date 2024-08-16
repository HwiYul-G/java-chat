package com.y.javachat.dto;

public record GroupChatRoomResponseDto(
        Long groupChatRoomId,
        String groupChatRoomName,
        String lastMessage
) {
}
