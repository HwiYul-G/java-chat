package com.y.javachat.chat.repository;

import com.y.javachat.chat.model.PersonalChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalChatRepository extends JpaRepository<PersonalChat,Long> {
}
