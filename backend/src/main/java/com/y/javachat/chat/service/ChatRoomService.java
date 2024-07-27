package com.y.javachat.chat.service;

import com.y.javachat.chat.model.ChatRoom;
import com.y.javachat.chat.repository.ChatRoomRepository;
import com.y.javachat.chat.event.ChatRoomDeletedEvent;
import com.y.javachat.chat.event.ChatRoomGeneratedEvent;
import com.y.javachat.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ApplicationEventPublisher eventPublisher;

    public List<ChatRoom> findAll() {
        return this.chatRoomRepository.findAll();
    }


    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));
    }

    public ChatRoom save(ChatRoom newChatRoom) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        newChatRoom.setCreatedAt(currentLocalDateTime);
        ChatRoom chatRoom = chatRoomRepository.save(newChatRoom);

        eventPublisher.publishEvent(
                new ChatRoomGeneratedEvent(chatRoom.getId(), chatRoom.getManagerUserId(), currentLocalDateTime)
        );

        return chatRoom;
    }


    public ChatRoom update(Long chatRoomId, ChatRoom update) {
        ChatRoom oldChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));
        oldChatRoom.setNoticeChatId(update.getNoticeChatId());
        oldChatRoom.setName(update.getName());
        return chatRoomRepository.save(oldChatRoom);
    }

    public void delete(Long chatRoomId) {
        chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));

        eventPublisher.publishEvent(new ChatRoomDeletedEvent(chatRoomId));

        chatRoomRepository.deleteById(chatRoomId);
    }
}
