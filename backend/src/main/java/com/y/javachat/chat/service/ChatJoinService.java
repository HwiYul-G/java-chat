package com.y.javachat.chat.service;

import com.y.javachat.chat.dto.LeaveChatJoinDto;
import com.y.javachat.chat.model.ChatJoin;
import com.y.javachat.chat.repository.ChatJoinRepository;
import com.y.javachat.chat.event.ChatRoomDeletedEvent;
import com.y.javachat.chat.event.ChatRoomGeneratedEvent;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatJoinService {

    private final ChatJoinRepository chatJoinRepository;

    public ChatJoin save(ChatJoin newChatJoin) {
        return chatJoinRepository.save(newChatJoin);
    }

    public List<ChatJoin> findAll() {
        return this.chatJoinRepository.findAll();
    }

    public void delete(LeaveChatJoinDto dto) {
        ChatJoin chatJoin = chatJoinRepository.findByUserIdAndRoomId(dto.userId(), dto.roomId())
                .orElseThrow(() -> new ObjectNotFoundException("chat-join", "user id : " + dto.userId() + "room id : " + dto.roomId()));
        // TODO: 방의 관리자가 나가는 경우를 처리한다.
        chatJoinRepository.delete(chatJoin);
    }


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handleChatRoomGeneratedEvent(ChatRoomGeneratedEvent event) {
        chatJoinRepository.save(ChatJoin.builder()
                .roomId(event.chatRoomId())
                .userId(event.managerId())
                .createdAt(event.currentTIme())
                .build()
        );
    }

    @Async
    @EventListener
    public void handleChatRoomDeletedEvent(ChatRoomDeletedEvent event) {
        List<ChatJoin> chatJoins = chatJoinRepository.findAllByRoomId(event.chatRoomId());
        chatJoinRepository.deleteAll(chatJoins);
    }
}
