package com.y.javachat.chatRoom.event;

import java.sql.Timestamp;

public record ChatRoomGeneratedEvent(Long chatRoomId, Long managerId,Timestamp currentTIme) {

}
