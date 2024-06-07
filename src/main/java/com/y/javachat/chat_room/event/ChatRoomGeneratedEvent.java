package com.y.javachat.chat_room.event;

import java.sql.Timestamp;

public record ChatRoomGeneratedEvent(Long chatRoomId, Timestamp currentTIme) {

}
