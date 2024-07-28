package com.y.javachat.chat.controller;

import com.y.javachat.chat.model.GroupChatRoom;
import com.y.javachat.chat.model.PersonalChatRoom;
import com.y.javachat.chat.service.PersonalChatRoomService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/personal-chat-rooms")
public class PersonalChatRoomController {

    private final PersonalChatRoomService personalChatRoomService;

    @PostMapping
    public Result addPersonalChatRoom(@Valid @RequestBody PersonalChatRoom personalChatRoom){
        PersonalChatRoom savedPersonalChatRoom = personalChatRoomService.createPersonalChatRoom(personalChatRoom);
        return new Result(true, StatusCode.SUCCESS, "채팅방 생성 성공", savedPersonalChatRoom);
    }

}
