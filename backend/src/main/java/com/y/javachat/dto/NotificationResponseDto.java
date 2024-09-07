package com.y.javachat.dto;

import com.y.javachat.model.Notification;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        String content,
        boolean isRead,
        LocalDateTime createdAt,
        Notification.NotificationStatus status,
        Notification.NotificationType type
) {
}
