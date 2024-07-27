package com.y.javachat.app.event;

public record FriendRequestGeneratedEvent(Long senderId, Long receiverId, String senderName) {
}
