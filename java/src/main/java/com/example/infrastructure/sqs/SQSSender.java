package com.example.infrastructure.sqs;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.domain.gateway.MessageSender;

import java.util.HashMap;
import java.util.Map;

public class SQSSender implements MessageSender {

    private final AmazonSQS sqs;
    private final String queueUrl;

    public SQSSender() {
        this.sqs = AmazonSQSClientBuilder.defaultClient();
        this.queueUrl = System.getenv("ORIGINAL_QUEUE_URL");
    }

    @Override
    public void send(String body, Map<String, SQSEvent.MessageAttribute> attributes) {
        Map<String, MessageAttributeValue> msgAttributes = new HashMap<>();

        for (Map.Entry<String, SQSEvent.MessageAttribute> entry : attributes.entrySet()) {
            SQSEvent.MessageAttribute attr = entry.getValue();
            msgAttributes.put(entry.getKey(), new MessageAttributeValue()
                    .withDataType(attr.getDataType())
                    .withStringValue(attr.getStringValue()));
        }

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(body)
                .withMessageAttributes(msgAttributes);

        sqs.sendMessage(request);
    }
}
