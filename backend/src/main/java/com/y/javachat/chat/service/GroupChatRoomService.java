package com.y.javachat.chat.service;

import com.y.javachat.chat.event.GroupChatRoomDeletedEvent;
import com.y.javachat.chat.event.GroupChatRoomGeneratedEvent;
import com.y.javachat.chat.model.GroupChatRoom;
import com.y.javachat.chat.repository.GroupChatRoomRepository;
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
public class GroupChatRoomService {

    private final GroupChatRoomRepository groupChatRoomRepository;

    private final ApplicationEventPublisher eventPublisher;

    public List<GroupChatRoom> findAll() {
        return this.groupChatRoomRepository.findAll();
    }


    public GroupChatRoom findById(Long chatRoomId) {
        return groupChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));
    }

    public GroupChatRoom save(GroupChatRoom newGroupChatRoom) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        newGroupChatRoom.setCreatedAt(currentLocalDateTime);
        GroupChatRoom groupChatRoom = groupChatRoomRepository.save(newGroupChatRoom);

        eventPublisher.publishEvent(
                new GroupChatRoomGeneratedEvent(groupChatRoom.getId(), groupChatRoom.getManagerUserId(),currentLocalDateTime)
        );

        return groupChatRoom;
    }


    public GroupChatRoom update(Long chatRoomId, GroupChatRoom update) {
        GroupChatRoom oldGroupChatRoom = groupChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));
        oldGroupChatRoom.setName(update.getName());
        return groupChatRoomRepository.save(oldGroupChatRoom);
    }

    public void delete(Long chatRoomId) {
        groupChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));

        eventPublisher.publishEvent(new GroupChatRoomDeletedEvent(chatRoomId));

        groupChatRoomRepository.deleteById(chatRoomId);
    }
}
