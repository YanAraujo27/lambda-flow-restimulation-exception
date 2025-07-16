from interfaces import IMessageHandler, IMessageSender

class DLQMessageHandler(IMessageHandler):
    def __init__(self, sender: IMessageSender):
        self.sender = sender

    def handle(self, message: dict) -> None:
        body = message['body']
        attributes = message.get('messageAttributes', {})
        self.sender.send(body, attributes)
