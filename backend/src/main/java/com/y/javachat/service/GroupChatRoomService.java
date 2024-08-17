package com.y.javachat.service;

import com.y.javachat.dto.GroupChatRoomResponseDto;
import com.y.javachat.event.GroupChatRoomDeletedEvent;
import com.y.javachat.event.GroupChatRoomGeneratedEvent;
import com.y.javachat.model.GroupChat;
import com.y.javachat.model.GroupChatRoom;
import com.y.javachat.repository.GroupChatJoinRepository;
import com.y.javachat.repository.GroupChatRepository;
import com.y.javachat.repository.GroupChatRoomRepository;
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
    private final GroupChatJoinRepository groupChatJoinRepository;
    private final GroupChatRepository groupChatRepository;

    private final ApplicationEventPublisher eventPublisher;

    public List<GroupChatRoomResponseDto> findAllByUserId(Long userId) {
        return groupChatJoinRepository.findAllByUserId(userId)
                .stream()
                .map(groupChatJoin -> {
                    GroupChatRoom groupChatRoom = groupChatRoomRepository
                            .findById(groupChatJoin.getRoomId())
                            .orElseThrow(() -> new ObjectNotFoundException("chat room", groupChatJoin.getRoomId()));
                    String lastMessage = groupChatRepository
                            .findFirstByRoomIdOrderByCreatedAtDesc(groupChatJoin.getRoomId())
                            .map(GroupChat::getContent)
                            .orElse("");
                    return new GroupChatRoomResponseDto(groupChatJoin.getRoomId(), groupChatRoom.getName(), lastMessage);
                })
                .toList();
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
                new GroupChatRoomGeneratedEvent(groupChatRoom.getId(), groupChatRoom.getManagerUserId(), currentLocalDateTime)
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
