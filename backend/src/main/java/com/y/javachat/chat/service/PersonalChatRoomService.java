package com.y.javachat.chat.service;

import com.y.javachat.chat.event.PersonalChatRoomGeneratedEvent;
import com.y.javachat.chat.model.PersonalChatRoom;
import com.y.javachat.chat.repository.PersonalChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalChatRoomService {

    private final PersonalChatRoomRepository personalChatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PersonalChatRoom createPersonalChatRoom(PersonalChatRoom personalChatRoom) {
        PersonalChatRoom savedPersonalChatRoom = personalChatRoomRepository.save(personalChatRoom);

        eventPublisher.publishEvent(
                new PersonalChatRoomGeneratedEvent(
                        savedPersonalChatRoom.getId(),
                        savedPersonalChatRoom.getUserId1(),
                        savedPersonalChatRoom.getUserId2())
        );

        return savedPersonalChatRoom;
    }

    public List<PersonalChatRoom> getAllPersonalChatRoomByUserId(Long userId) {
        return personalChatRoomRepository.findAllByUserId(userId);
    }

}
