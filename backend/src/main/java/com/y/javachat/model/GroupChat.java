package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "group_chat")
@NoArgsConstructor
@AllArgsConstructor
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "메시지를 보내는 사람 id가 필요합니다.")
    private Long senderId;

    @NotNull(message = "메시지를 보내는 방 id가 필요합니다.")
    private Long roomId;

    @NotNull(message = "메시지를 보내느 사람 이름이 필요합니다.")
    private String senderName;

    private String content;

    private LocalDateTime createdAt;

    private MessageType type;
}
