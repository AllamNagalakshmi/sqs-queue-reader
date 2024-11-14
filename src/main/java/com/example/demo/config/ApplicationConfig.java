package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.ListenerMode;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class ApplicationConfig {

    private final static String bonusPointSchema = "/schema/bonus-point-balance-changed-schema.json";
    final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {
        return SqsMessageListenerContainerFactory.builder()
                .configure(options -> options.listenerMode(ListenerMode.BATCH).maxMessagesPerPoll(10)
                        .pollTimeout(Duration.ofSeconds(10)).acknowledgementMode(AcknowledgementMode.MANUAL))
                .sqsAsyncClient(sqsAsyncClient).build();
    }

    @Bean("bonusPointSchema")
    public JsonSchema bonusPointSchema() throws IOException {
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        InputStream inputStream = getClass().getResourceAsStream(bonusPointSchema);
        JsonNode jsonNode = objectMapper.readTree(inputStream);
        JsonSchema jsonSchema = jsonSchemaFactory.getSchema(jsonNode);
        jsonSchema.initializeValidators();
        return jsonSchema;
    }
}
