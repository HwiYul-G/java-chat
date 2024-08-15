package com.y.javachat.event;

public record PersonalChatRoomGeneratedEvent(
        Long roomId,
        Long userId1,
        Long userId2
) {
}
