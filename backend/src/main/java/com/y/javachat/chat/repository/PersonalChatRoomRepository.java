package com.y.javachat.chat.repository;

import com.y.javachat.chat.model.PersonalChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalChatRoomRepository extends JpaRepository<PersonalChatRoom, Long> {
}
