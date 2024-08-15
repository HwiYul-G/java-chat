package com.y.javachat.chat.service;

import com.y.javachat.chat.dto.LeaveChatJoinDto;
import com.y.javachat.chat.model.GroupChatJoin;
import com.y.javachat.chat.repository.GroupChatJoinRepository;
import com.y.javachat.chat.event.GroupChatRoomDeletedEvent;
import com.y.javachat.chat.event.GroupChatRoomGeneratedEvent;
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
public class GroupChatJoinService {

    private final GroupChatJoinRepository chatJoinRepository;

    public GroupChatJoin save(GroupChatJoin newGroupChatJoin) {
        return chatJoinRepository.save(newGroupChatJoin);
    }

    public void delete(LeaveChatJoinDto dto) {
        GroupChatJoin groupChatJoin = chatJoinRepository.findByUserIdAndRoomId(dto.userId(), dto.roomId())
                .orElseThrow(() -> new ObjectNotFoundException("chat-join", "user id : " + dto.userId() + "room id : " + dto.roomId()));
        // TODO: 방의 관리자가 나가는 경우를 처리한다.
        chatJoinRepository.delete(groupChatJoin);
    }


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handleGroupChatRoomGeneratedEvent(GroupChatRoomGeneratedEvent event) {
        chatJoinRepository.save(GroupChatJoin.builder()
                .roomId(event.chatRoomId())
                .userId(event.managerId())
                .createdAt(event.currentTIme())
                .build()
        );
    }

    @Async
    @EventListener
    public void handleGroupChatRoomDeletedEvent(GroupChatRoomDeletedEvent event) {
        List<GroupChatJoin> groupChatJoins = chatJoinRepository.findAllByRoomId(event.chatRoomId());
        chatJoinRepository.deleteAll(groupChatJoins);
    }
}
