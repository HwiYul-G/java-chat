package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "friendship")
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Column(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @Column(name = "friend")
    private User friend;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

}
