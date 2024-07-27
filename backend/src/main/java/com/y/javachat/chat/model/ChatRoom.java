package com.y.javachat.chat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "chat_room")
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "채팅방 이름이 필요합니다.")
    private String name;

    private Long noticeChatId;

    private LocalDateTime createdAt;

    private Long managerUserId;

    @NotNull(message = "그룹 채팅방 여부 설정이 필요합니다.")
    private Boolean isGroup;

}
