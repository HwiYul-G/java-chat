package com.y.javachat.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name="friends")
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "userId가 필요합니다.")
    private Long userId;

    @NotNull(message = "friendId가 필요합니다.")
    private Long friendId;

    private FriendStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
