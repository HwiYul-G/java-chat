package com.y.javachat.notification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name="notification")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "알림을 받을 사용자 id가 필요합니다.")
    private Long userId;

    @NotNull(message = "알림 종류가 필요합니다.")
    private NotificationType type;

    private String content;

    private LocalDateTime createdAt;

    private NotificationStatus status;
}
