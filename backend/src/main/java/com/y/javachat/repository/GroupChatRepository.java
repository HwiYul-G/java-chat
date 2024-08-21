package com.y.javachat.repository;

import com.y.javachat.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    List<GroupChat> findAllByRoomId(Long chatRoomId);

    Optional<GroupChat> findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);
}
