package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsPublisherService {

    private final SnsClient snsClient;

    @Value("${topic-arn}")
    private String topicArn;

    public SnsPublisherService() {
        this.snsClient = SnsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public void publishMessage(String message) {
        try {
            // Create a publish request
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .build();

            // Publish the message
            PublishResponse response = snsClient.publish(request);

            // Output the message ID of the successfully published message
            System.out.println("Message published with ID: " + response.messageId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to publish message to SNS", e);
        }
    }

}
