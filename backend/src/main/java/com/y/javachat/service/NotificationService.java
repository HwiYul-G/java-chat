package com.y.javachat.service;

import com.y.javachat.dto.FriendInvitationRequestDto;
import com.y.javachat.dto.NotificationResponseDto;
import com.y.javachat.model.Notification;
import com.y.javachat.model.User;
import com.y.javachat.repository.NotificationRepository;
import com.y.javachat.repository.UserRepository;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationResponseDto createFriendRequestNotification(FriendInvitationRequestDto friendInvitationRequestDto) {
        User user = userRepository.findById(friendInvitationRequestDto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("user id", friendInvitationRequestDto.userId()));
        User friend = userRepository.findByEmail(friendInvitationRequestDto.friendEmail())
                .orElseThrow(() -> new ObjectNotFoundException("user email", friendInvitationRequestDto.friendEmail()));
        Notification notification = Notification.builder()
                .senderId(user.getId())
                .receiverId(friend.getId())
                .isRead(false)
                .status(Notification.NotificationStatus.PENDING)
                .type(Notification.NotificationType.FRIEND_REQUEST)
                .content(user.getEmail() + "님이 친구 신청을 합니다.")
                .build();
        return notificationRepository.save(notification).toNotificationResponseDto();
    }

    public NotificationResponseDto updateNotificationStatus(Long notificationId, boolean isAccept) {
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new ObjectNotFoundException("notification", notificationId));
        if (isAccept) {
            notification.setStatus(Notification.NotificationStatus.ACCEPTED);
            notification.setContent(notification.getContent() + "(수락 완료)");
            return notificationRepository.save(notification).toNotificationResponseDto();
        }
        notification.setStatus(Notification.NotificationStatus.DECLINED);
        notification.setContent(notification.getContent() + "(거절 완료)");
        return notificationRepository.save(notification).toNotificationResponseDto();
    }

}