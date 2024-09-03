package com.y.javachat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseModel {
    @Column(name = "room_name", length = 256)
    private String roomName;    // 그룹 채팅방의 경우만 존재

    @Column(name = "manager_id")
    private Long managerId; // 그룹 채팅방의 경우만 존재

    @Column(name = "is_group")
    private boolean isGroup;
}
