package com.y.aiserver.config;

import com.azure.storage.common.StorageSharedKeyCredential;
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

    @Value("${spring.cloud.azure.storage.account.name}")
    private String accountName;
    @Value("${spring.cloud.azure.storage.account.key}")
    private String accountKey;

    @Bean
    public StorageSharedKeyCredential storageSharedKeyCredential() {
        return new StorageSharedKeyCredential(
                accountName, accountKey
        );
    }

    @Bean
    public QueueClient inputQueueClient() {
        return new QueueClientBuilder()
                .endpoint(queueEndpointName + "/" + inputQueueName)
                .queueName(inputQueueName)
                .credential(storageSharedKeyCredential())
                .buildClient();
    }

    @Bean
    public QueueClient outputQueueClient() {
        return new QueueClientBuilder()
                .endpoint(queueEndpointName + "/" + outputQueueName)
                .queueName(outputQueueName)
                .credential(storageSharedKeyCredential())
                .buildClient();
    }

}
