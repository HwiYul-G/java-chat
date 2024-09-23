package com.y.javachat.controller;

import com.y.javachat.dto.*;
import com.y.javachat.model.ChatMessage;
import com.y.javachat.service.AIService;
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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final AIService aiService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat-rooms/{roomId}")
    @SendTo("/sub/chat-rooms/{roomId}")
    public Mono<ChatMessageResponseDto> sendMessage(@Payload ChatMessageRequestDto chatMessageRequestDto) {
        // STEP1: 모든 사용자에게 "비속어 탐지 중" 메시지를 보냄
        ChatMessageResponseDto detectingMessage = new ChatMessageResponseDto(
                null,  // id는 null로 설정하여 일시적으로 사용
                chatMessageRequestDto.roomId(),
                null,  // 시스템 메시지이므로 senderId는 null
                "SYSTEM",  // senderName은 "SYSTEM"
                "비속어 탐지 중...",  // 메시지 내용
                LocalDateTime.now(),
                ChatMessage.MessageType.SYSTEM  // 메시지 타입은 SYSTEM으로 설정
        );
        messagingTemplate.convertAndSend("/sub/chat-rooms/" + chatMessageRequestDto.roomId(), detectingMessage);

        // STEP2 & STEP3: 비속어 탐지
        return aiService.predictBadWord(chatMessageRequestDto.content())
                .publishOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(isBadWord -> {
                    if (isBadWord) {
                        // 비속어가 포함된 경우
                        ChatMessageResponseDto warningMessage = new ChatMessageResponseDto(
                                null,
                                chatMessageRequestDto.roomId(),
                                null,
                                "SYSTEM",
                                "비속어가 포함된 메시지입니다.",
                                LocalDateTime.now(),
                                ChatMessage.MessageType.SYSTEM
                        );
                        // 비속어 경고 메시지 전송
                        messagingTemplate.convertAndSend("/sub/warnings/users/" + chatMessageRequestDto.senderId(), warningMessage);
                        return Mono.empty();  // 원래 메시지 전송하지 않음
                    }
                    // 비속어가 없는 경우, 원래 메시지를 저장하고 전송
                    return Mono.fromCallable(() -> chatService.save(chatMessageRequestDto))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnTerminate(() -> {
                    // "비속어 탐지 중" 메시지를 제거하기 위한 시스템 메시지 전송 가능
                    ChatMessageResponseDto emptyMessage = new ChatMessageResponseDto(
                            null,
                            chatMessageRequestDto.roomId(),
                            null,
                            "SYSTEM",
                            "",  // 빈 메시지로 비속어 탐지 중 상태 해제
                            LocalDateTime.now(),
                            ChatMessage.MessageType.SYSTEM
                    );
                    messagingTemplate.convertAndSend("/sub/chat-rooms/" + chatMessageRequestDto.roomId(), emptyMessage);
                });
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