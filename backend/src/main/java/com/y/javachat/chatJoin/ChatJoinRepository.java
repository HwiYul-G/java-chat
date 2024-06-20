package com.y.javachat.chatJoin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Long> {
    List<ChatJoin> findAllByRoomId(Long chatRoomId);

    Optional<ChatJoin> findByUserIdAndRoomId(Long userId, Long roomId);
}
