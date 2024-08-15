package com.y.javachat.chat.controller;

import com.y.javachat.chat.model.GroupChatRoom;
import com.y.javachat.chat.service.GroupChatRoomService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/group-chat-rooms")
public class GroupChatRoomController {
    private final GroupChatRoomService groupChatRoomService;

    @GetMapping("/{chatRoomId}")
    public Result findGroupChatRoomById(@PathVariable Long chatRoomId) {
        GroupChatRoom foundGroupChatRoom = this.groupChatRoomService.findById(chatRoomId);
        return new Result(true, StatusCode.SUCCESS, "채팅방 찾기 성공", foundGroupChatRoom);
    }

    @PostMapping
    public Result addGroupChatRoom(@Valid @RequestBody GroupChatRoom groupChatRoom) {
        GroupChatRoom savedGroupChatRoom = groupChatRoomService.save(groupChatRoom);
        return new Result(true, StatusCode.SUCCESS, "채팅방 생성 성공", savedGroupChatRoom);
    }

    @GetMapping("/users/{userId}")
    public Result findAllGroupChatRoomsByUserId(@PathVariable Long userId) {
        List<GroupChatRoom> groupChatRooms = groupChatRoomService.findAllByUserId(userId);
        return new Result(true, StatusCode.SUCCESS, "채팅방 목록 조회 성공", groupChatRooms);
    }

    @PutMapping("/{chatRoomId}")
    public Result updateGroupChatRoom(@PathVariable Long chatRoomId, @Valid @RequestBody GroupChatRoom groupChatRoom) {
        GroupChatRoom updatedGroupChatRoom = this.groupChatRoomService.update(chatRoomId, groupChatRoom);
        return new Result(true, StatusCode.SUCCESS, "채팅방 업데이트 성공", updatedGroupChatRoom);
    }

    @DeleteMapping("/{chatRoomId}")
    public Result deleteGroupChatRoom(@PathVariable Long chatRoomId) {
        this.groupChatRoomService.delete(chatRoomId);
        return new Result(true, StatusCode.SUCCESS, "채팅방 삭제 성공");
    }

}
