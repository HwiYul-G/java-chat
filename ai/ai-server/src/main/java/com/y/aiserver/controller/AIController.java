package com.y.aiserver.controller;

import ai.onnxruntime.OrtException;
import com.y.aiserver.bible.Result;
import com.y.aiserver.bible.StatusCode;
import com.y.aiserver.service.BertModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("${api.endpoint.base-url}/ai")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AIController {

    private final BertModelService bertModelService;

    @PostMapping("/predict")
    public Result predictBadWord(@RequestBody String inputText) throws OrtException {
        boolean isBadWord = bertModelService.predictBadWord(inputText);
        return new Result(true, StatusCode.SUCCESS, "비속어 탐지 완료", isBadWord);
    }
}
