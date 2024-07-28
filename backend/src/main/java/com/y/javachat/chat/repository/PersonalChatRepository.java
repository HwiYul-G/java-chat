package com.y.javachat.chat.repository;

import com.y.javachat.chat.model.PersonalChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalChatRepository extends JpaRepository<PersonalChat,Long> {

    List<PersonalChat> findAllByRoomId(Long roomId);
}
