package com.y.javachat.service;


import com.y.javachat.system.Result;
import com.y.javachat.system.exception.ExternalAPIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final WebClient webClient;

    @Value("${ai.sever.base-url}")
    private String aiBaseUri;

    public Mono<Boolean> predictBadWord(String inputText) {
        return webClient.post()
                .uri(aiBaseUri + "/ai/predict")
                .bodyValue(inputText)
                .retrieve()
                .bodyToMono(Result.class)
                .handle((res, sink) -> {
                    if (res.isFlag()) {
                        sink.next((boolean) res.getData());
                        return;
                    }
                    sink.error(new ExternalAPIException("AI Server Error: " + res.getMessage(), res.getCode(), res.getMessage()));
                });
    }
}
