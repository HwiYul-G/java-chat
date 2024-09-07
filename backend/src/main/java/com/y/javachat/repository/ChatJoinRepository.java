package com.y.javachat.repository;

import com.y.javachat.model.ChatJoin;
import com.y.javachat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Long> {
    List<ChatJoin> findAllByUser(User user);

    @Query("SELECT cj.user FROM ChatJoin AS cj WHERE cj.chatRoom.id = :chatRoomId AND cj.user.id <> :userId")
    Optional<User> findFriendInChatRoom(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

    Optional<ChatJoin> findByChatRoom_IdAndUser_Id(Long chatRoomId, Long UserId);

}