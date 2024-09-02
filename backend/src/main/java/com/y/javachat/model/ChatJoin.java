package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_join")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatJoin extends BaseModel {

    @NotNull(message = "사용자 정보가 필요합니다.")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    @Column(name = "user")
    private User user;

    @NotNull(message = "채팅방 정보가 필요합니다.")
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @Column(name = "chat_room")
    private ChatRoom chatRoom;
}
