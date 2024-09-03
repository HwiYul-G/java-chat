package com.y.javachat.repository;

import com.y.javachat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId(Long roomId);

    Optional<ChatMessage> findFirstByRoomIdAndMessageTypeOrderByCreatedAtDesc(Long roomId, ChatMessage.MessageType messageType);
}
