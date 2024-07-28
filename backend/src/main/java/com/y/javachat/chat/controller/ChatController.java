package com.y.javachat.chat.controller;

import com.y.javachat.chat.model.GroupChat;
import com.y.javachat.chat.model.PersonalChat;
import com.y.javachat.chat.service.GroupChatService;
import com.y.javachat.chat.service.PersonalChatService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final GroupChatService groupChatService;
    private final PersonalChatService personalChatService;

    @MessageMapping("/group-chat-rooms/{roomId}")
    @SendTo("/sub/group-chat-rooms/{roomId}")
    public GroupChat sendMessage(@Payload GroupChat groupChat) {
        groupChat.setCreatedAt(LocalDateTime.now());
        return groupChatService.save(groupChat);
    }

    @MessageMapping("/personal-chat-rooms/{roomId}")
    @SendTo("/sub/personal-chat-rooms/{roomId}")
    public PersonalChat sendPersonalMessage(@Payload PersonalChat personalChat){
        personalChat.setCreatedAt(LocalDateTime.now());
        return personalChatService.save(personalChat);
    }

    @GetMapping("${api.endpoint.base-url}/group-chat-rooms/{roomId}/messages")
    @ResponseBody
    public Result getAllMessagesByRoomId(@PathVariable Long roomId){
        List<GroupChat> chats = groupChatService.findAllByRoomId(roomId);
        return new Result(true, StatusCode.SUCCESS, "채팅 리스트 조회 성공", chats);
    }

    @GetMapping("${api.endpoint.base-url}/personal-chat-rooms/{roomId}/messages")
    @ResponseBody
    public Result getAllPersonalChatsByRoomId(@PathVariable Long roomId){
        List<PersonalChat> chats = personalChatService.findAllByRoomId(roomId);
        return new Result(true, StatusCode.SUCCESS,"채팅 리스트 조회 성공", chats);
    }

}
