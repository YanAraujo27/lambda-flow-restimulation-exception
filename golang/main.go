package main

import (
    "context"
    "log"

    "github.com/aws/aws-lambda-go/events"
    "github.com/aws/aws-lambda-go/lambda"
    "golang_dlq_forwarder/controller"
    "golang_dlq_forwarder/service"
)

func main() {
    sender := service.NewSQSSender()
    handler := controller.NewDLQController(sender)

    lambda.Start(func(ctx context.Context, sqsEvent events.SQSEvent) error {
        for _, message := range sqsEvent.Records {
            if err := handler.Handle(message); err != nil {
                log.Printf("Erro ao processar mensagem: %v", err)
                return err
            }
        }
        return nil
    })
}
