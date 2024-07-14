package com.y.javachat.chat;

import com.y.javachat.chatRoom.event.ChatRoomDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public List<Chat> findAllByRoomId(Long roomId) {
        return chatRepository.findAllByRoomId(roomId);
    }

    public Chat save(Chat newChat) {
        return chatRepository.save(newChat);
    }

    @Async
    @EventListener
    public void handleChatRoomDeletedEvent(ChatRoomDeletedEvent event) {
        List<Chat> chats = chatRepository.findAllByRoomId(event.chatRoomId());
        chatRepository.deleteAll(chats);
    }
}
