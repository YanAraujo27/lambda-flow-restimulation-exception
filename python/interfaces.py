from abc import ABC, abstractmethod

class IMessageSender(ABC):
    @abstractmethod
    def send(self, message_body: str, attributes: dict) -> None:
        pass

class IMessageHandler(ABC):
    @abstractmethod
    def handle(self, message: dict) -> None:
        pass
