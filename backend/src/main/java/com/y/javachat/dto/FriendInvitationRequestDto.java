package com.y.javachat.dto;

public record FriendInvitationRequestDto(
    Long userId,
    String friendEmail
) {
}
