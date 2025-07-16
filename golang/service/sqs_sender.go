package service

import (
    "os"

    "github.com/aws/aws-sdk-go/aws"
    "github.com/aws/aws-sdk-go/aws/session"
    "github.com/aws/aws-sdk-go/service/sqs"
    "github.com/aws/aws-lambda-go/events"
    "golang_dlq_forwarder/model"
)

type SQSSender struct {
    client   *sqs.SQS
    queueURL string
}

func NewSQSSender() *SQSSender {
    sess := session.Must(session.NewSession())
    client := sqs.New(sess)

    return &SQSSender{
        client:   client,
        queueURL: os.Getenv("ORIGINAL_QUEUE_URL"),
    }
}

func (s *SQSSender) Send(body string, attributes map[string]events.SQSMessageAttribute) error {
    msgAttributes := make(map[string]*sqs.MessageAttributeValue)

    for key, attr := range attributes {
        msgAttributes[key] = &sqs.MessageAttributeValue{
            DataType:    aws.String(attr.DataType),
            StringValue: attr.StringValue,
        }
    }

    _, err := s.client.SendMessage(&sqs.SendMessageInput{
        QueueUrl:          aws.String(s.queueURL),
        MessageBody:       aws.String(body),
        MessageAttributes: msgAttributes,
    })

    return err
}
