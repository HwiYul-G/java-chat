package com.y.javachat.chat.repository;

import com.y.javachat.chat.model.GroupChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {
}
