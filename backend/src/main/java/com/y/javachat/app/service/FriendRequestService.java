package com.y.javachat.app.service;

import com.y.javachat.app.event.FriendRequestGeneratedEvent;
import com.y.javachat.app.model.FriendRequest;
import com.y.javachat.app.repository.FriendRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
@Transactional
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FriendRequest createFriendRequest(FriendRequest friendRequest){
        friendRequest.setCreatedAt(LocalDateTime.now());
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        eventPublisher.publishEvent(
                new FriendRequestGeneratedEvent(friendRequest.getSenderId(), friendRequest.getReceiverId(), friendRequest.getSenderName())
        );

        return savedFriendRequest;
    }

}
