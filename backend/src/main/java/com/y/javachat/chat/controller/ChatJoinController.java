package com.y.javachat.chat.controller;

import com.y.javachat.chat.model.ChatJoin;
import com.y.javachat.chat.service.ChatJoinService;
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
@RequestMapping("${api.endpoint.base-url}/chat-join")
public class ChatJoinController {

    private final ChatJoinService chatJoinService;

    @PostMapping()
    public Result addChatJoin(@Valid @RequestBody ChatJoin chatJoin){
        ChatJoin savedChatJoin = chatJoinService.save(chatJoin);
        return new Result(true, StatusCode.SUCCESS, "채팅 조인 성공", savedChatJoin);
    }

}
