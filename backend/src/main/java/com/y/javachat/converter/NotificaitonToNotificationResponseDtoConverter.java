package com.y.javachat.converter;

import com.y.javachat.dto.NotificationResponseDto;
import com.y.javachat.model.Notification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificaitonToNotificationResponseDtoConverter implements Converter<Notification, NotificationResponseDto> {

    @Override
    public NotificationResponseDto convert(Notification source) {
        return new NotificationResponseDto(
                source.getId(),
                source.getContent(),
                source.isRead(),
                source.getCreatedAt(),
                source.getStatus(),
                source.getType()
        );
    }
}