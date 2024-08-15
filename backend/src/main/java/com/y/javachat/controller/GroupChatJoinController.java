package com.y.javachat.controller;

import com.y.javachat.model.GroupChatJoin;
import com.y.javachat.service.GroupChatJoinService;
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
@RequestMapping("${api.endpoint.base-url}/group-chat-join")
public class GroupChatJoinController {

    private final GroupChatJoinService groupChatJoinService;

    @PostMapping()
    public Result addGroupChatJoin(@Valid @RequestBody GroupChatJoin groupChatJoin){
        GroupChatJoin savedGroupChatJoin = groupChatJoinService.save(groupChatJoin);
        return new Result(true, StatusCode.SUCCESS, "채팅 조인 성공", savedGroupChatJoin);
    }

}
