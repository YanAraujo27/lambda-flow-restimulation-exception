package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.application.usecase.ForwardMessageUseCase;
import com.example.infrastructure.sqs.SQSSender;

public class Application implements RequestHandler<SQSEvent, Void> {

    private final ForwardMessageUseCase forwardMessageUseCase;

    public Application() {
        this.forwardMessageUseCase = new ForwardMessageUseCase(new SQSSender());
    }

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        event.getRecords().forEach(record -> {
            try {
                forwardMessageUseCase.execute(record);
            } catch (Exception e) {
                context.getLogger().log("Erro ao reenviar mensagem: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        return null;
    }
}
