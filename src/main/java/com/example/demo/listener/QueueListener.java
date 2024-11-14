package com.example.demo.listener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.example.demo.service.MessageProcessingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.BatchAcknowledgement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QueueListener {

    @Autowired
    MessageProcessingService messageProcessingService;

    @SqsListener(value = "${queue-url}")
    public void listenToMessages(List<Message<String>> messages, BatchAcknowledgement<String> batchAcknowledgement) {
        if (!messages.isEmpty()) {
            List<Message<String>> acknowledgeMessages = null;
            try {
                acknowledgeMessages = messageProcessingService.processEvents(messages);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            batchAcknowledgement.acknowledgeAsync(acknowledgeMessages);
        }
    }
}
