package com.y.javachat.chat.event;

import java.time.LocalDateTime;

public record GroupChatRoomGeneratedEvent(
        Long chatRoomId,
        Long managerId,
        LocalDateTime currentTIme
) {
}
