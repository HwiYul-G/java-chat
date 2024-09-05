package com.y.javachat.service;

import com.y.javachat.dto.*;
import com.y.javachat.model.*;
import com.y.javachat.repository.*;
import com.y.javachat.system.exception.DuplicationJoinException;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatJoinRepository chatJoinRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public ChatMessageResponseDto save(ChatMessageRequestDto chatMessageRequestDto) {
        User sender = userRepository.findById(chatMessageRequestDto.senderId())
                .orElseThrow(() -> new ObjectNotFoundException("user", chatMessageRequestDto.senderId()));

        return chatMessageRepository.save(ChatMessage.builder()
                .sender(sender)
                .roomId(chatMessageRequestDto.roomId())
                .content(chatMessageRequestDto.content())
                .messageType(chatMessageRequestDto.type())
                .build()
        ).toChatMessageResponseDto();
    }

    public List<ChatMessageResponseDto> findChatMessageByRoomId(Long roomId) {
        chatRoomRepository.findById(roomId).orElseThrow(() -> new ObjectNotFoundException("room id", roomId));
        return chatMessageRepository.findAllByRoomId(roomId).stream()
                .map(ChatMessage::toChatMessageResponseDto)
                .toList();
    }

    public ChatRoomResponseDto createGroupChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        User user = userRepository.findById(chatRoomRequestDto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("user id", chatRoomRequestDto.userId()));
        ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
                .isGroup(true)
                .roomName(chatRoomRequestDto.roomName())
                .managerId(chatRoomRequestDto.userId())
                .build());
        chatJoinRepository.save(ChatJoin.builder()
                .chatRoom(savedChatRoom)
                .user(user)
                .build());
        return new ChatRoomResponseDto(
                savedChatRoom.getId(),
                savedChatRoom.isGroup(),
                new ChatRoomResponseDto.ChatRoomInfo(savedChatRoom.getRoomName(), savedChatRoom.getId()),
                null,
                null
        );
    }

    public ChatRoomResponseDto createPrivateChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        User user = userRepository.findById(chatRoomRequestDto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("user id", chatRoomRequestDto.userId()));
        User friend = userRepository.findById(chatRoomRequestDto.friendId())
                .orElseThrow(() -> new ObjectNotFoundException("user id", chatRoomRequestDto.friendId()));

        Friendship friendship1 = friendshipRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new ObjectNotFoundException("friendship", user.getId() + " with " + friend.getId()));
        Friendship friendship2 = friendshipRepository.findByUserAndFriend(friend, user)
                .orElseThrow(() -> new ObjectNotFoundException("friendship", friend.getId() + " with " + user.getId()));

        if (friendship1.getChatRoomId() != null) {
            throw new DuplicationJoinException(chatRoomRequestDto.userId(), friendship1.getChatRoomId());
        }

        ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
                .isGroup(false)
                .build());
        chatJoinRepository.save(ChatJoin.builder()
                .user(user)
                .chatRoom(savedChatRoom)
                .build());
        chatJoinRepository.save(ChatJoin.builder()
                .user(friend)
                .chatRoom(savedChatRoom)
                .build());

        friendship1.setChatRoomId(savedChatRoom.getId());
        friendshipRepository.save(friendship1);
        friendship2.setChatRoomId(savedChatRoom.getId());
        friendshipRepository.save(friendship2);

        return new ChatRoomResponseDto(
                savedChatRoom.getId(),
                savedChatRoom.isGroup(),
                null,
                new ChatRoomResponseDto.FriendInfo(friend.getUsername(), friend.getEmail()),
                null
        );
    }

    public ChatRoomResponseDto enterGroupChatRoom(Long roomId, EnterChatRoomRequestDto requestDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ObjectNotFoundException("chatRoom", roomId));

        boolean isAlreadyJoined = chatJoinRepository.findByChatRoom_IdAndUser_Id(roomId, requestDto.userId());
        if (isAlreadyJoined) {
            throw new DuplicationJoinException(requestDto.userId(), roomId);
        }

        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new ObjectNotFoundException("user", requestDto.userId()));

        chatJoinRepository.save(ChatJoin.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build());

        ChatMessage lastMessage = getLastMessageByRoomId(roomId);

        return new ChatRoomResponseDto(
                chatRoom.getId(),
                chatRoom.isGroup(),
                new ChatRoomResponseDto.ChatRoomInfo(chatRoom.getRoomName(), chatRoom.getManagerId()),
                null,
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