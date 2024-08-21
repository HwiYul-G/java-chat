package com.y.javachat.repository;

import com.y.javachat.model.PersonalChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonalChatRoomRepository extends JpaRepository<PersonalChatRoom, Long> {

    @Query("SELECT p FROM PersonalChatRoom p WHERE p.userId1 = :userId OR p.userId2 = :userId")
    List<PersonalChatRoom> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM PersonalChatRoom p WHERE (p.userId1 = :userId1 AND p.userId2 = :userId2)"
            + "OR (p.userId1 = :userId2 AND p.userId2 = :userId1)")
    Optional<PersonalChatRoom> findByUserId1AndUserId2(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

}
