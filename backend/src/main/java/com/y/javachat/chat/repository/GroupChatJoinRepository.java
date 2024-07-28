package com.y.javachat.chat.repository;

import com.y.javachat.chat.model.GroupChatJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatJoinRepository extends JpaRepository<GroupChatJoin, Long> {
    List<GroupChatJoin> findAllByRoomId(Long chatRoomId);

    Optional<GroupChatJoin> findByUserIdAndRoomId(Long userId, Long roomId);
}
