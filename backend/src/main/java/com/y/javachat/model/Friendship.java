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

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", length = 256)
    private FriendStatus status;

    public enum FriendStatus{
        PENDING,
        ACCEPTED,
        DECLINED
    }

}
