package com.example.application.usecase;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.domain.gateway.MessageSender;

public class ForwardMessageUseCase {
    private final MessageSender sender;

    public ForwardMessageUseCase(MessageSender sender) {
        this.sender = sender;
    }

    public void execute(SQSEvent.SQSMessage message) throws Exception {
        sender.send(message.getBody(), message.getMessageAttributes());
    }
}
