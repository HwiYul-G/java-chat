package com.y.javachat.config;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${spring.cloud.azure.storage.queue.endpoint}")
    private String queueEndpointName;
    @Value("${spring.cloud.azure.storage.queue.input.name}")
    private String inputQueueName;
    @Value("${spring.cloud.azure.storage.queue.output.name}")
    private String outputQueueName;

    @Bean
    public QueueClient inputQueueClient() {
        return new QueueClientBuilder()
                .endpoint(queueEndpointName)
                .queueName(inputQueueName)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    @Bean
    public QueueClient outputQueueClient() {
        return new QueueClientBuilder()
                .endpoint(queueEndpointName)
                .queueName(outputQueueName)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

}
