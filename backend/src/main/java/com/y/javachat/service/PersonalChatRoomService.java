package com.y.javachat.service;

import com.y.javachat.dto.PersonalChatRoomResponseDto;
import com.y.javachat.event.PersonalChatRoomGeneratedEvent;
import com.y.javachat.model.PersonalChatRoom;
import com.y.javachat.model.User;
import com.y.javachat.repository.PersonalChatRepository;
import com.y.javachat.repository.PersonalChatRoomRepository;
import com.y.javachat.repository.UserRepository;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalChatRoomService {

    private final PersonalChatRoomRepository personalChatRoomRepository;
    private final PersonalChatRepository personalChatRepository;
    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    public PersonalChatRoom createPersonalChatRoom(PersonalChatRoom personalChatRoom) {

        Optional<PersonalChatRoom> existingRoom = personalChatRoomRepository
                .findByUserId1AndUserId2(personalChatRoom.getUserId1(), personalChatRoom.getUserId2());

        if(existingRoom.isPresent())
            return existingRoom.get();

        PersonalChatRoom savedPersonalChatRoom = personalChatRoomRepository.save(personalChatRoom);

        eventPublisher.publishEvent(
                new PersonalChatRoomGeneratedEvent(
                        savedPersonalChatRoom.getId(),
                        savedPersonalChatRoom.getUserId1(),
                        savedPersonalChatRoom.getUserId2())
        );

        return savedPersonalChatRoom;
    }

    public List<PersonalChatRoomResponseDto> getAllPersonalChatRoomByUserId(Long userId) {
        List<PersonalChatRoom> personalChatRooms = personalChatRoomRepository.findAllByUserId(userId);
        return personalChatRooms.stream().map(personalChatRoom -> {
            Long friendId = !personalChatRoom.getUserId1().equals(userId) ?
                    personalChatRoom.getUserId1() : personalChatRoom.getUserId2();
            User friend = userRepository.findById(friendId)
                    .orElseThrow(() -> new ObjectNotFoundException("user id", friendId));
            String lastMessage = personalChatRepository
                    .findTopByRoomIdOrderByCreatedAtDesc(personalChatRoom.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("chat at roomId", personalChatRoom.getId()))
                    .getContent();
            return new PersonalChatRoomResponseDto(personalChatRoom.getId(), personalChatRoom.getUserId2(), friend.getUsername(), friend.getEmail(),lastMessage);
        }).toList();
    }

}
