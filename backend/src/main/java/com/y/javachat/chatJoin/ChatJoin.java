package com.y.javachat.chatJoin;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "chat_join")
public class ChatJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "조인할 사용자 아이디가 필요합니다.")
    private Long userId;

    @NotNull(message = "조인할 방 아이디가 필요합니다.")
    private Long roomId;

    private Timestamp createdAt;
}
