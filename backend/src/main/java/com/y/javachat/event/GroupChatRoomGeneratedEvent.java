package com.y.javachat.event;

import java.time.LocalDateTime;

public record GroupChatRoomGeneratedEvent(
        Long chatRoomId,
        Long managerId,
        LocalDateTime currentTIme
) {
}
