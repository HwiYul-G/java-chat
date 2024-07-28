package com.y.javachat.chat.event;

public record PersonalChatRoomGeneratedEvent(
        Long roomId,
        Long userId1,
        Long userId2
) {
}
