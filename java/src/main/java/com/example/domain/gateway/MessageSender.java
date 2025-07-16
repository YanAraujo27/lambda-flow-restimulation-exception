package com.example.domain.gateway;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.Map;

public interface MessageSender {
    void send(String body, Map<String, SQSEvent.MessageAttribute> attributes) throws Exception;
}
