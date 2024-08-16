package com.y.javachat.repository;

import com.y.javachat.model.PersonalChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalChatRepository extends JpaRepository<PersonalChat,Long> {

    List<PersonalChat> findAllByRoomId(Long roomId);

    @Query("SELECT pc FROM PersonalChat AS pc WHERE pc.roomId = :roomId ORDER BY pc.createdAt DESC")
    Optional<PersonalChat> findTopByRoomIdOrderByCreatedAtDesc(@Param("roomId") Long roomId);
}
