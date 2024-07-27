package com.y.javachat.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name="friend_request")
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "userId가 필요합니다.")
    private Long senderId;

    @NotNull(message = "receiverId가 필요합니다.")
    private Long receiverId;

    private String senderName;

    private LocalDateTime createdAt;

}
