package com.y.aiserver.service;

import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.modality.nlp.bert.BertToken;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// docs: https://javadoc.io/doc/ai.djl/api/latest/ai/djl/modality/nlp/bert/package-summary.html
@Slf4j
@Service
@RequiredArgsConstructor
public class BertModelService {

    private final OrtEnvironment ortEnvironment;
    private final OrtSession ortSession;
    private final BertFullTokenizer tokenizer;

    public boolean predictBadWord(String inputText) throws OrtException {
        BertToken token = tokenizer.encode(inputText, "", 128);

        List<String> tokens = token.getTokens();
        List<Long> tokenTypes = token.getTokenTypes();
        List<Long> attentionMask = token.getAttentionMask();

        List<Long> inputIds = tokens.stream()
                .map(tokenizer.getVocabulary()::getIndex)  // 토큰을 vocab index로 변환
                .toList();

        // 입력 데이터를 long 배열로 변환 (ONNX 텐서 입력 형식에 맞게)
        long[][] inputIdsArray = new long[][]{inputIds.stream().mapToLong(i -> i).toArray()};
        long[][] attentionMaskArray = new long[][]{attentionMask.stream().mapToLong(i -> i).toArray()};
        long[][] tokenTypesArray = new long[][]{tokenTypes.stream().mapToLong(i -> i).toArray()};

        // ONNX 모델에 전달할 입력 텐서 생성
        Map<String, OnnxTensor> inputs = Map.of(
                "input_ids", OnnxTensor.createTensor(ortEnvironment, inputIdsArray),
                "attention_mask", OnnxTensor.createTensor(ortEnvironment, attentionMaskArray),
                "token_type_ids", OnnxTensor.createTensor(ortEnvironment, tokenTypesArray)
        );

        // ONNX 모델 실행
        OrtSession.Result results = ortSession.run(inputs);

        // 출력 결과(logits) 가져오기
        float[][] logits = (float[][]) results.get(0).getValue();
        results.close();

        log.info("logits: {}, {}", logits[0][0], logits[0][1]);

        // 로짓을 바탕으로 비속어 여부 판단 (예: 0번 인덱스가 비속어 확률)
        return logits[0][0] < logits[0][1];
    }

}
