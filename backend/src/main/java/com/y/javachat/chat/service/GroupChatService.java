package com.y.javachat.chat.service;

import com.y.javachat.chat.event.GroupChatRoomDeletedEvent;
import com.y.javachat.chat.model.GroupChat;
import com.y.javachat.chat.repository.GroupChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatService {
    private final GroupChatRepository groupChatRepository;

    public List<GroupChat> findAllByRoomId(Long roomId) {
        return groupChatRepository.findAllByRoomId(roomId);
    }

    public GroupChat save(GroupChat newChat) {
        return groupChatRepository.save(newChat);
    }

    @Async
    @EventListener
    public void handleChatRoomDeletedEvent(GroupChatRoomDeletedEvent event) {
        List<GroupChat> chats = groupChatRepository.findAllByRoomId(event.chatRoomId());
        groupChatRepository.deleteAll(chats);
    }
}