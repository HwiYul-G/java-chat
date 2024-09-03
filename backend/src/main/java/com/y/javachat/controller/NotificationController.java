package com.y.javachat.controller;

import com.y.javachat.converter.NotificaitonToNotificationResponseDtoConverter;
import com.y.javachat.dto.FriendInvitationRequestDto;
import com.y.javachat.dto.NotificationResponseDto;
import com.y.javachat.service.NotificationService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${api.endpoint.base-url}/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificaitonToNotificationResponseDtoConverter notificaitonToNotificationResponseDtoConverter;

    @PostMapping("/notifications/friend-invitation")
    public Result addNotification(@RequestBody FriendInvitationRequestDto friendInvitationRequestDto) {
        NotificationResponseDto notification =
                notificaitonToNotificationResponseDtoConverter.convert(
                        notificationService.createFriendRequestNotification(friendInvitationRequestDto)
                );
        return new Result(true, StatusCode.SUCCESS, "알림 생성 성공", notification);
    }

    @PutMapping("/notifications/{notificationId}")
    public Result updateNotificationStatus(@PathVariable Long notificationId, @RequestParam boolean isAccept) {
        NotificationResponseDto notification =
                notificaitonToNotificationResponseDtoConverter.convert(
                        notificationService.updateNotificationStatus(notificationId, isAccept)
                );
        return new Result(true, StatusCode.SUCCESS, "알림 상태 변경 성공", notification);
    }

}