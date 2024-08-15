package com.y.javachat.repository;

import com.y.javachat.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    List<GroupChat> findAllByRoomId(Long chatRoomId);
}
