package com.y.javachat.controller;

import ai.onnxruntime.OrtException;
import com.y.javachat.dto.*;
import com.y.javachat.service.BertModelService;
import com.y.javachat.service.ChatService;
import com.y.javachat.system.Result;
import com.y.javachat.system.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final BertModelService bertModelService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat-rooms/{roomId}")
    @SendTo("/sub/chat-rooms/{roomId}")
    public ChatMessageResponseDto sendMessage(@Payload ChatMessageRequestDto chatMessageRequestDto) throws OrtException {
        boolean isBadWord = bertModelService.predictBadWord(chatMessageRequestDto.content());
        if (isBadWord) {
            messagingTemplate.convertAndSend(
                    "/sub/warnings/users/" + chatMessageRequestDto.senderId(),
                    new WarningMessageDto(chatMessageRequestDto.senderId(), "비속어 사용이 감지되었습니다.")
            );
            return null;
        }
        return chatService.save(chatMessageRequestDto);
    }

    @GetMapping("${api.endpoint.base-url}/chat-rooms/{roomId}/messages")
    @ResponseBody
    public Result getAllMessagesByRoomId(@PathVariable Long roomId) {
        List<ChatMessageResponseDto> chats = chatService.findChatMessageByRoomId(roomId);
        return new Result(true, StatusCode.SUCCESS, "채팅 메시지 목록 조회 성공", chats);
    }

    @PostMapping("${api.endpoint.base-url}/chat-rooms")
    @ResponseBody
    public Result addChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        if (chatRoomRequestDto.isGroup()) {
            ChatRoomResponseDto chatRoom = chatService.createGroupChatRoom(chatRoomRequestDto);
            return new Result(true, StatusCode.SUCCESS, "그룹 채팅방 생성 성공", chatRoom);
        }
        ChatRoomResponseDto chatRoom = chatService.createPrivateChatRoom(chatRoomRequestDto);
        return new Result(true, StatusCode.SUCCESS, "개인 채팅방 생성 성공", chatRoom);
    }

    @PostMapping("${api.endpoint.base-url}/chat-rooms/{roomId}/enter")
    @ResponseBody
    public Result enterGroupChatRoom(@PathVariable Long roomId, @RequestBody EnterChatRoomRequestDto enterChatRoomRequestDto) {
        ChatRoomResponseDto chatRoom = chatService.enterGroupChatRoom(roomId, enterChatRoomRequestDto);
        return new Result(true, StatusCode.SUCCESS, "그룹 채팅방 들어가기 성공", chatRoom);
    }

}