package com.y.javachat.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat-rooms/{roomId}")
    @SendTo("/sub/chat-rooms/{roomId}")
    public Chat sendMessage(@Payload Chat chat) {
        chat.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return chatService.save(chat);
    }

}
