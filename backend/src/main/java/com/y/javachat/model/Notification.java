package com.y.javachat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseModel{
    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "content", length = 512)
    private String content;

    private boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 256)
    private NotificationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 256)
    private NotificationType type;

    public enum NotificationStatus{
        PENDING,
        ACCEPTED,
        DECLINED,
        NONE
    }

    public enum NotificationType {
        FRIEND_REQUEST
    }

}
