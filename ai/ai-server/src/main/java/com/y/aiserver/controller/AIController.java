package com.y.aiserver.controller;

import ai.onnxruntime.OrtException;
import com.y.aiserver.bible.Result;
import com.y.aiserver.dto.PredictResponseDto;
import com.y.aiserver.service.BertModelService;
import com.y.aiserver.bible.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("${api.endpoint.base-url}/ai")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AIController {

    private final BertModelService bertModelService;

    @GetMapping("/predict")
    public Result predictBadWord(@RequestBody String inputText) throws OrtException {
        boolean isBadWord = bertModelService.predictBadWord(inputText);
        PredictResponseDto dto = new PredictResponseDto(isBadWord, isBadWord ? "비속어가 탐지 되었습니다." : "비속어가 아닙니다.");
        return new Result(true, StatusCode.SUCCESS, "비속어 탐지 완료", dto);
    }
}
