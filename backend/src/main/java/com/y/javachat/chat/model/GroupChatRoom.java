package com.y.javachat.chat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "group_chat_room")
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "채팅방 이름이 필요합니다.")
    private String name;

    private LocalDateTime createdAt;

    private Long managerUserId;

}
