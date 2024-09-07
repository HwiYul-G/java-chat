package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "chat_join")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatJoin extends BaseModel {

    @NotNull(message = "사용자 정보가 필요합니다.")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @NotNull(message = "채팅방 정보가 필요합니다.")
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;
}
