package com.y.javachat.chat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "personal_chat")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "메시지를 보내는 사람 id가 필요합니다.")
    private Long senderId;

    @NotNull(message = "메시지를 보내는 방 id가 필요합니다.")
    private Long roomId;

    private String content;

    private LocalDateTime createdAt;

    private MessageType type;
}
