package com.y.javachat.service;

import com.y.javachat.model.PersonalChat;
import com.y.javachat.repository.PersonalChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalChatService {

    private final PersonalChatRepository personalChatRepository;

    public List<PersonalChat> findAllByRoomId(Long roomId) {
        return personalChatRepository.findAllByRoomId(roomId);
    }

    public PersonalChat save(PersonalChat newChat) {
        return personalChatRepository.save(newChat);
    }

}
