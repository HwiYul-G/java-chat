package com.y.javachat.service;

import com.y.javachat.dto.ChatRoomResponseDto;
import com.y.javachat.dto.FriendResponseDto;
import com.y.javachat.dto.NotificationResponseDto;
import com.y.javachat.model.*;
import com.y.javachat.repository.*;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendshipRepository friendshipRepository;
    private final NotificationRepository notificationRepository;
    private final ChatJoinRepository chatJoinRepository;
    private final ChatMessageRepository chatMessageRepository;


    public User save(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public User update(Long userId, User update) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        oldUser.setUsername(update.getUsername());
        oldUser.setEmail(update.getEmail());
        oldUser.setEnabled(update.isEnabled());
        oldUser.setRoles(update.getRoles());
        return userRepository.save(oldUser);
    }

    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userRepository.deleteById(userId);
    }

    public List<FriendResponseDto> findFriendsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        return friendshipRepository.findAllByUser(user).stream()
                .map(friendship -> new FriendResponseDto(
                        friendship.getFriend().getId(),
                        friendship.getFriend().getUsername(),
                        friendship.getFriend().getEmail(),
                        friendship.getChatRoomId()
                )).toList();
    }

    public List<NotificationResponseDto> getAllNotifications(Long receiverId) {
        userRepository.findById(receiverId).orElseThrow(() -> new ObjectNotFoundException("user id", receiverId));
        return notificationRepository.findByReceiverId(receiverId)
                .stream().map(Notification::toNotificationResponseDto)
                .toList();
    }

    public List<ChatRoomResponseDto> findChatRoomsByUserId(Long userId, boolean isGroup) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user id", userId));
        List<ChatJoin> chatJoins = chatJoinRepository.findAllByUser(user);
        if (isGroup) {
            return chatJoins.stream().filter(chatJoin -> chatJoin.getChatRoom().isGroup())
                    .map(chatJoin -> chatJoinToChatRoomResponseDtoForGroup(chatJoin.getChatRoom()))
                    .toList();
        }
        return chatJoins.stream().filter(chatJoin -> !chatJoin.getChatRoom().isGroup())
                .map(chatJoin -> chatJoinToChatRoomResponseDtoForPersonal(chatJoin.getChatRoom(), userId))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("userEmail " + userEmail + " 을 찾을 수 없습니다.."));
    }

    private ChatRoomResponseDto chatJoinToChatRoomResponseDtoForGroup(ChatRoom chatRoom) {
        ChatMessage lastMessage = getLastMessageByRoomId(chatRoom.getId());
        return new ChatRoomResponseDto(
                chatRoom.getId(),
                new ChatRoomResponseDto.ChatRoomInfo(chatRoom.getRoomName(), chatRoom.getManagerId()),
                null,
                new ChatRoomResponseDto.LastMessageInfo(lastMessage.getContent(), lastMessage.getCreatedAt())
        );
    }

    private ChatRoomResponseDto chatJoinToChatRoomResponseDtoForPersonal(ChatRoom chatRoom, Long userId) {
        User friend = chatJoinRepository.findFriendInChatRoom(chatRoom.getId(), userId)
                .orElseThrow(() -> new ObjectNotFoundException("채팅방 정보 조회 시 발생", userId + "의 친구를 찾을 수 없습니다."));
        ChatMessage lastMessage = getLastMessageByRoomId(chatRoom.getId());
        return new ChatRoomResponseDto(
                chatRoom.getId(),
                null,
                new ChatRoomResponseDto.FriendInfo(friend.getUsername(), friend.getEmail()),
                new ChatRoomResponseDto.LastMessageInfo(lastMessage.getContent(), lastMessage.getCreatedAt())
        );
    }

    private ChatMessage getLastMessageByRoomId(Long roomId) {
        return chatMessageRepository
                .findFirstByRoomIdAndMessageTypeOrderByCreatedAtDesc(roomId, ChatMessage.MessageType.CHAT)
                .orElse(ChatMessage.builder()
                        .roomId(roomId)
                        .content("")
                        .messageType(ChatMessage.MessageType.CHAT)
                        .build()
                );
    }

}