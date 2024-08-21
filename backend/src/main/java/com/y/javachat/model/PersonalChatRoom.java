package com.y.javachat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "personal_chat_room")
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "user1 id가 필요합니다.")
    private Long userId1;

    @NotNull(message = "user2 id가 필요합니다.")
    private Long userId2;

}
