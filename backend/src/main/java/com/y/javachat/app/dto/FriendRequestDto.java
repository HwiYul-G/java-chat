package com.y.javachat.app.dto;

import jakarta.validation.constraints.NotNull;

public record FriendRequestDto(
        @NotNull(message = "senderId가 필요합니다.")
        Long senderId,

        @NotNull(message = "receiverId가 필요합니다.")
        Long receiverId,

        @NotNull(message = "sender의 name이 필요합니다.")
        String senderName
) {
}