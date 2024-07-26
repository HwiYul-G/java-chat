package com.y.javachat.chat;

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

    private final ChatService chatService;

    @MessageMapping("/chat-rooms/{roomId}")
    @SendTo("/sub/chat-rooms/{roomId}")
    public Chat sendMessage(@Payload Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        return chatService.save(chat);
    }

    @GetMapping("${api.endpoint.base-url}/chat-rooms/{roomId}/messages")
    @ResponseBody
    public Result getAllMessagesByRoomId(@PathVariable Long roomId){
        List<Chat> chats = chatService.findAllByRoomId(roomId);
        return new Result(true, StatusCode.SUCCESS, "채팅 리스트 조회 성공", chats);
    }

}
