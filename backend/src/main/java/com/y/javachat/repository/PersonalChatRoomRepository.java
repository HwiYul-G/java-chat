package com.y.javachat.repository;

import com.y.javachat.model.PersonalChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonalChatRoomRepository extends JpaRepository<PersonalChatRoom, Long> {

    @Query("SELECT p FROM PersonalChatRoom as p WHERE p.userId1 = ?1 or p.userId2 = ?1")
    List<PersonalChatRoom> findAllByUserId(Long userId);

}
