package com.y.aiserver.service;

import ai.onnxruntime.OrtException;
import com.azure.storage.queue.QueueClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.y.aiserver.dto.InputQueueMessageDto;
import com.y.aiserver.dto.OutputQueueResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueueConsumerService {

    private final QueueClient inputQueueClient;
    private final QueueClient outputQueueClient;
    private final BertModelService bertModelService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 5000) // 5초마다 큐를 확인한다.
    public void processMessages() {
        inputQueueClient.receiveMessages(5).forEach(
                receivedMessage -> {
                    try {
                        InputQueueMessageDto inputQueueMessageDto = objectMapper.readValue(receivedMessage.getBody().toString(), InputQueueMessageDto.class);

                        boolean result = bertModelService.predictBadWord(inputQueueMessageDto.content());
                        String outputQueueResultDtoAsString = objectMapper.writeValueAsString(
                                new OutputQueueResultDto(inputQueueMessageDto.messageId(), result)
                        );
                        outputQueueClient.sendMessage(outputQueueResultDtoAsString);

                        inputQueueClient.deleteMessage(receivedMessage.getMessageId(), receivedMessage.getPopReceipt());
                    } catch (JsonProcessingException e) {
                        log.error("Json Parsing 오류: {}", e.getMessage());
                    } catch (OrtException e) {
                        log.error("OrtException 오류 : {}", e.getMessage());
                    }
                }
        );
    }
}
