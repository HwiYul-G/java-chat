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

    @NotNull(message = "사용자의 id가 필요합니다.")
    private Long userId;

    @NotNull(message = "친구의 id가 필요합니다.")
    private Long friendId;

}
