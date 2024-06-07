package com.y.javachat.chat_room;

import com.y.javachat.chat.ChatRepository;
import com.y.javachat.chat_join.ChatJoinRepository;
import com.y.javachat.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAll() {
        return this.chatRoomRepository.findAll();
    }


    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ObjectNotFoundException("chat room", chatRoomId));
    }

    public ChatRoom save(ChatRoom newChatRoom) {
        newChatRoom.setCreatedAt(Timestamp.from(Instant.now()));
        // TODO : 채팅방을 만든 사람을 현재 채팅방에 join 시키는 작업이 필요하다.
        return chatRoomRepository.save(newChatRoom);
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
        // TODO : 현재 채팅방에 있는 모든 사람들을 제거한다.
        // TODO: 현재 채팅방에 있는 모든 채팅을 제거한다.
        chatRoomRepository.deleteById(chatRoomId);
    }
}
