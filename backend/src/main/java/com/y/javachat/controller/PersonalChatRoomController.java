package com.y.javachat.controller;

import com.y.javachat.model.PersonalChatRoom;
import com.y.javachat.service.PersonalChatRoomService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/personal-chat-rooms")
public class PersonalChatRoomController {

    private final PersonalChatRoomService personalChatRoomService;

    @PostMapping
    public Result addPersonalChatRoom(@Valid @RequestBody PersonalChatRoom personalChatRoom) {
        PersonalChatRoom savedPersonalChatRoom = personalChatRoomService.createPersonalChatRoom(personalChatRoom);
        return new Result(true, StatusCode.SUCCESS, "채팅방 생성 성공", savedPersonalChatRoom);
    }

    @GetMapping("/users/{userId}")
    public Result findAllPersonalChatRoomByUserId(@PathVariable Long userId) {
        List<PersonalChatRoom> personalChatRooms = personalChatRoomService.getAllPersonalChatRoomByUserId(userId);
        return new Result(true, StatusCode.SUCCESS, "개인 채팅방 목록 조회 성공", personalChatRooms);
    }

}
