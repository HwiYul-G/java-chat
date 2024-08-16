package com.y.javachat.repository;

import com.y.javachat.model.PersonalChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonalChatRoomRepository extends JpaRepository<PersonalChatRoom, Long> {

    List<PersonalChatRoom> findAllByUserId(Long userId);

}
