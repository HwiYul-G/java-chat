package com.y.javachat.app.service;

import com.y.javachat.app.event.FriendRequestGeneratedEvent;
import com.y.javachat.app.model.Notification;
import com.y.javachat.app.model.NotificationType;
import com.y.javachat.app.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<Notification> findAllByUserId(Long userId){
        return notificationRepository.findAllByUserId(userId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handleFriendRequestGeneratedEvent(FriendRequestGeneratedEvent event){
        Notification notification = Notification.builder()
                .createdAt(LocalDateTime.now())
                .userId(event.receiverId())
                .type(NotificationType.FRIEND_REQUEST)
                .content(event.senderName())
                .build();
        notificationRepository.save(notification);
    }

}
