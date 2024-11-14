package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.example.demo.validator.ValidatorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageProcessingService {

    @Autowired
    ValidatorService validatorService;

    ObjectMapper objectMapper = new ObjectMapper();

    public List<Message<String>> processEvents(List<Message<String>> messages) throws JsonProcessingException {
        List<Message<String>> acknowledgedMessages = new ArrayList<>();
        for (Message<String> message : messages) {
            String messageId = Objects.requireNonNull(message.getHeaders().getId()).toString();
            JsonNode jsonNode = objectMapper.readTree(message.getPayload());
            Set<ValidationMessage> validMessages = validatorService.validateJson(
                    jsonNode.get("Message").asText());
            if (!validMessages.isEmpty()) {
                for (ValidationMessage validationMessage : validMessages) {
                    log.error("message with id {} validation failed with reason {} ",
                            messageId, validationMessage.getMessage());
                }
            } else {
                log.info("message with id {} successfully processed", messageId);
            }
            acknowledgedMessages.add(message);
        }
        return acknowledgedMessages;
    }

}
