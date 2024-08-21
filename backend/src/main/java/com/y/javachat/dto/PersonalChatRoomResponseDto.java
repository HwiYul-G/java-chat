package com.y.javachat.dto;

public record PersonalChatRoomResponseDto(
        Long roomId,
        Long friendId,
        String friendName,
        String friendEmail,
        String lastMessage
) {
}