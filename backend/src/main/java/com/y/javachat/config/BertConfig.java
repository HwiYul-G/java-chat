package com.y.javachat.config;

import ai.djl.modality.nlp.DefaultVocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BertConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public OrtEnvironment ortEnvironment() {
        return OrtEnvironment.getEnvironment();
    }

    @Bean
    public OrtSession ortSession(OrtEnvironment ortEnvironment) throws OrtException, IOException {
        Resource modelResource = resourceLoader.getResource("classpath:model.onnx");
        Path modelPath = modelResource.getFile().toPath();
        return ortEnvironment.createSession(modelPath.toString(), new OrtSession.SessionOptions());
    }

    @Bean
    public DefaultVocabulary vocabulary() throws IOException {
        Resource vocabResource = resourceLoader.getResource("classpath:vocab.txt");
        Path vocabPath = vocabResource.getFile().toPath();
        return DefaultVocabulary.builder()
                .addFromTextFile(vocabPath)
                .build();
    }

    @Bean
    public BertFullTokenizer bertFullTokenizer(DefaultVocabulary vocabulary) {
        return new BertFullTokenizer(vocabulary, true);
    }

}
