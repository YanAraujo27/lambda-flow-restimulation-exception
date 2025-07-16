package model

import "github.com/aws/aws-lambda-go/events"

type MessageSender interface {
    Send(body string, attributes map[string]events.SQSMessageAttribute) error
}
