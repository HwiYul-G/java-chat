package com.y.javachat.app.converter;

import com.y.javachat.app.dto.FriendRequestDto;
import com.y.javachat.app.model.FriendRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestDtoToFriendRequestConverter implements Converter<FriendRequestDto, FriendRequest> {

    @Override
    public FriendRequest convert(FriendRequestDto source) {
        return FriendRequest.builder()
                .receiverId(source.receiverId())
                .senderId(source.senderId())
                .senderName(source.senderName())
                .build();
    }
}
