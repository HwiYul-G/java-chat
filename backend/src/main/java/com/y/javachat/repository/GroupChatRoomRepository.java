package com.y.javachat.repository;

import com.y.javachat.model.GroupChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {
}
