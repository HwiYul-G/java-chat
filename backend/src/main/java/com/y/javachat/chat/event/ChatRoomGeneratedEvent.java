package com.y.javachat.chat.event;

import java.time.LocalDateTime;

public record ChatRoomGeneratedEvent(Long chatRoomId, Long managerId, LocalDateTime currentTIme) {

}
