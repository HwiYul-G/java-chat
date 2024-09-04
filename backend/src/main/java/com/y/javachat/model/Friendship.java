package com.y.javachat.model;

import com.y.javachat.dto.FriendResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "friendship")
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    public FriendResponseDto toFriendResponseDto() {
        return new FriendResponseDto(
                this.friend.getId(),
                this.friend.getUsername(),
                this.friend.getEmail(),
                this.chatRoomId
        );
    }

}