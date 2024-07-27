package com.y.javachat.chat.controller;

import com.y.javachat.chat.model.ChatRoom;
import com.y.javachat.chat.service.ChatRoomService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/chat-rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/{chatRoomId}")
    public Result findChatRoomById(@PathVariable Long chatRoomId) {
        ChatRoom foundChatRoom = this.chatRoomService.findById(chatRoomId);
        return new Result(true, StatusCode.SUCCESS, "채팅방 찾기 성공", foundChatRoom);
    }

    @PostMapping
    public Result addChatRoom(@Valid @RequestBody ChatRoom chatRoom) {
        ChatRoom savedChatRoom = chatRoomService.save(chatRoom);
        return new Result(true, StatusCode.SUCCESS, "채팅방 생성 성공", savedChatRoom);
    }

    @GetMapping
    public Result findAllChatRooms() {
        List<ChatRoom> chatRoomPage = this.chatRoomService.findAll();
        return new Result(true, StatusCode.SUCCESS, "채팅방 목록 조회 성공", chatRoomPage);
    }

    @PutMapping("/{chatRoomId}")
    public Result updateChatRoom(@PathVariable Long chatRoomId, @Valid @RequestBody ChatRoom chatRoom) {
        ChatRoom updatedChatRoom = this.chatRoomService.update(chatRoomId, chatRoom);
        return new Result(true, StatusCode.SUCCESS, "채팅방 업데이트 성공", updatedChatRoom);
    }

    @DeleteMapping("/{chatRoomId}")
    public Result deleteChatRoom(@PathVariable Long chatRoomId) {
        this.chatRoomService.delete(chatRoomId);
        return new Result(true, StatusCode.SUCCESS, "채팅방 삭제 성공");
    }


}
