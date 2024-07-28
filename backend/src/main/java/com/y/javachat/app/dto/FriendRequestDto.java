package com.y.javachat.app.dto;

import jakarta.validation.constraints.NotNull;

public record FriendRequestDto(
        @NotNull
        Long userId,

        @NotNull
        String friendEmail
) {
}
