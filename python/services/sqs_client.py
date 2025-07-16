import boto3
from interfaces import IMessageSender
from config import Config

class SQSSender(IMessageSender):
    def __init__(self, queue_url: str = Config.ORIGINAL_QUEUE_URL):
        self.sqs = boto3.client("sqs")
        self.queue_url = queue_url

    def send(self, message_body: str, attributes: dict = None) -> None:
        attributes = attributes or {}

        self.sqs.send_message(
            QueueUrl=self.queue_url,
            MessageBody=message_body,
            MessageAttributes=attributes
        )
