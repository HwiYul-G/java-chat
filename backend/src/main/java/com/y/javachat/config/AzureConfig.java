package com.y.javachat.config;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${azure.queue.input.endpoint}")
    private String inputQueueEndpointName; // ai sever로 input되는 것
    @Value("${azure.queue.input.name}")
    private String inputQueueName;

    @Value("${azure.queue.output.endpoint}")
    private String outputQueueEndpointName; // ai sever에서 output되는 것
    @Value("${azure.queue.output.name}")
    private String outputQueueName;

    @Bean
    public QueueClient inputQueueClient() {
        return new QueueClientBuilder()
                .endpoint(inputQueueEndpointName)
                .queueName(inputQueueName)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    @Bean
    public QueueClient outputQueueClient() {
        return new QueueClientBuilder()
                .endpoint(outputQueueEndpointName)
                .queueName(outputQueueName)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

}
