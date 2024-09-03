package com.y.javachat.dto;

import java.time.LocalDateTime;

public record ChatRoomResponseDto(
        Long roomId,
        ChatRoomInfo chatRoomInfo, // 단체 채팅방 필요한 정보
        FriendInfo friendInfo,  // 개인 채팅 방 필요 정보
        LastMessageInfo lastMessageInfo
) {
    public record ChatRoomInfo(
            String roomName,
            Long managerId
    ) {
    }

    public record FriendInfo(
            String name,
            String email
    ) {
    }

    public record LastMessageInfo(
            String content,
            LocalDateTime createdAt
    ) {
    }
}