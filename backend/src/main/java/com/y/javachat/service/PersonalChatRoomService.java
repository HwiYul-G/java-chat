package com.y.javachat.service;

import com.y.javachat.dto.PersonalChatRoomResponseDto;
import com.y.javachat.event.PersonalChatRoomGeneratedEvent;
import com.y.javachat.model.PersonalChatRoom;
import com.y.javachat.repository.PersonalChatRepository;
import com.y.javachat.repository.PersonalChatRoomRepository;
import com.y.javachat.repository.UserRepository;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalChatRoomService {

    private final PersonalChatRoomRepository personalChatRoomRepository;
    private final PersonalChatRepository personalChatRepository;
    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    public PersonalChatRoom createPersonalChatRoom(PersonalChatRoom personalChatRoom) {
        PersonalChatRoom savedPersonalChatRoom = personalChatRoomRepository.save(personalChatRoom);
        personalChatRoomRepository.save(
                PersonalChatRoom.builder()
                        .userId(personalChatRoom.getFriendId())
                        .friendId(personalChatRoom.getUserId())
                        .build());

        eventPublisher.publishEvent(
                new PersonalChatRoomGeneratedEvent(
                        savedPersonalChatRoom.getId(),
                        savedPersonalChatRoom.getUserId(),
                        savedPersonalChatRoom.getFriendId())
        );

        return savedPersonalChatRoom;
    }

    public List<PersonalChatRoomResponseDto> getAllPersonalChatRoomByUserId(Long userId) {
        List<PersonalChatRoom> personalChatRooms = personalChatRoomRepository.findAllByUserId(userId);
        return personalChatRooms.stream().map(personalChatRoom -> {
            String friendName = userRepository.findById(personalChatRoom.getFriendId())
                    .orElseThrow(() -> new ObjectNotFoundException("user id", personalChatRoom.getFriendId()))
                    .getUsername();
            String lastMessage = personalChatRepository
                    .findTopByRoomIdOrderByCreatedAtDesc(personalChatRoom.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("chat at roomId", personalChatRoom.getId()))
                    .getContent();
            return new PersonalChatRoomResponseDto(personalChatRoom.getId(), personalChatRoom.getFriendId(), friendName, lastMessage);
        }).toList();
    }

}
