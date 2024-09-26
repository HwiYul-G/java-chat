package com.y.javachat.service;


import com.azure.storage.queue.QueueClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y.javachat.dto.ChatMessageResponseDto;
import com.y.javachat.dto.external.InputQueueMessageDto;
import com.y.javachat.dto.external.OutputQueueResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final QueueClient inputQueueClient;
    private final QueueClient outputQueueClient;
    private final ObjectMapper objectMapper;

    public void insertInputQueue(ChatMessageResponseDto chatMessageResponseDto) {
        try {
            String inputQueueMessageAsString = objectMapper.writeValueAsString(
                    new InputQueueMessageDto(chatMessageResponseDto.id(), chatMessageResponseDto.content())
            );
            inputQueueClient.sendMessage(inputQueueMessageAsString);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException : {}", e.getMessage());
        }
    }

    public List<OutputQueueResultDto> getOutputQueueResult() {
        List<OutputQueueResultDto> result = new ArrayList<>();
        outputQueueClient.receiveMessages(5).forEach(
                receivedMessage -> {
                    try {
                        OutputQueueResultDto outputQueueResultDto = objectMapper.readValue(
                                receivedMessage.getBody().toString(), OutputQueueResultDto.class
                        );
                        result.add(outputQueueResultDto);
                        outputQueueClient.deleteMessage(receivedMessage.getMessageId(), receivedMessage.getPopReceipt());
                    } catch (JsonProcessingException e) {
                        log.error("JsonProcessingException: {}", e.getMessage());
                    }
                }
        );
        return result;
    }

}
