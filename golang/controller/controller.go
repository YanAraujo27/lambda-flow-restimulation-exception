package controller

import (
    "github.com/aws/aws-lambda-go/events"
    "golang_dlq_forwarder/model"
)

type DLQController struct {
    sender model.MessageSender
}

func NewDLQController(sender model.MessageSender) *DLQController {
    return &DLQController{sender: sender}
}

func (c *DLQController) Handle(message events.SQSMessage) error {
    return c.sender.Send(message.Body, message.MessageAttributes)
}
