from services.sqs_client import SQSSender
from services.message_handler import DLQMessageHandler

def lambda_handler(event, context):
    sender = SQSSender()
    handler = DLQMessageHandler(sender)

    for record in event['Records']:
        try:
            handler.handle(record)
        except Exception as e:
            print(f"Erro ao processar mensagem: {e}")
            raise e
