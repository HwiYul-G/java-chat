package com.y.javachat.chatRoom.event;

import java.time.LocalDateTime;

public record ChatRoomGeneratedEvent(Long chatRoomId, Long managerId, LocalDateTime currentTIme) {

}
