package com.y.javachat.dto;

import jakarta.validation.constraints.NotNull;

public record FriendRequestDto(
        @NotNull
        Long userId,

        @NotNull
        String friendEmail
) {
}
