package com.y.javachat.chatRoom;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

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

    private Timestamp createdAt;

    @NotNull(message = "채팅방 관리자 아이디가 필요합니다.")
    private Long managerUserId;

    private Boolean isGroup;

}
