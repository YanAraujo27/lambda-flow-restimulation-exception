# 🔁 DLQ Forwarder – Lambda para Reprocessamento de Mensagens (SQS)

Este projeto implementa uma **função AWS Lambda** que consome mensagens de uma **DLQ (Dead Letter Queue)** do **Amazon SQS** e **reencaminha as mensagens para a fila original**.  

A mesma lógica foi implementada em **três linguagens (Python, Go e Java)** utilizando boas práticas de arquitetura de software (SOLID, MVC, Clean Architecture) para comparar:

- Design
- Desempenho
- Manutenibilidade
- Custo computacional

---

## 📦 Funcionalidade

1. A Lambda é acionada por mensagens em uma fila DLQ (Dead Letter Queue).
2. Cada mensagem é lida e reencaminhada à fila original de onde ela veio.
3. A mensagem é mantida exatamente como chegou (body e atributos).

---

## 🧠 Arquiteturas Aplicadas

### ✅ Python – SOLID + Clean Code

- Camadas bem definidas: handler, services, interfaces
- Princípios SOLID aplicados (injeção de dependência, abstrações, SRP)
- Facilita testes unitários e substituição de componentes

📁 Estrutura:
| Linguagem  | Pasta / Arquivo               | Função na Arquitetura                                                     |
| ---------- | ----------------------------- | ------------------------------------------------------------------------- |
| **Python** | `app.py`                      | Ponto de entrada da Lambda. Instancia os serviços e processa o evento.    |
|            | `config.py`                   | Carrega variáveis de ambiente e configurações globais.                    |
|            | `interfaces.py`               | Define interfaces (abstrações) para desacoplar lógica e facilitar testes. |
|            | `services/message_handler.py` | Classe responsável por processar as mensagens da DLQ.                     |
|            | `services/sqs_client.py`      | Serviço de envio de mensagens para a fila SQS original.                   |
|            | `requirements.txt`            | Lista de dependências da função (boto3, etc.).                            |
---

### 🚀 Golang – MVC

- Simples e direto, com separação clara entre Model, Controller e Service
- Ideal para baixo overhead e alta performance
- Leve, rápido e com baixo tempo de cold start na AWS Lambda

📁 Estrutura:
| Linguagem | Pasta / Arquivo            | Função na Arquitetura                                      |
| --------- | -------------------------- | ---------------------------------------------------------- |
| **Go**    | `main.go`                  | Ponto de entrada da aplicação, define o handler da Lambda. |
|           | `controller/controller.go` | Controlador que recebe o evento e orquestra os serviços.   |
|           | `service/sqs_sender.go`    | Serviço responsável por enviar mensagens à fila original.  |
|           | `model/interfaces.go`      | Interface que define contrato de envio de mensagens.       |
|           | `go.mod`                   | Gerenciador de dependências do projeto Go.                 |
---

### ☕ Java – Clean Architecture

- Separação clara entre domínio, casos de uso e infraestrutura
- Ideal para aplicações com regras de negócio mais complexas
- Requer mais configuração e cuidado com tamanho do deploy

📁 Estrutura:
| Linguagem | Pasta / Arquivo                                  | Função na Arquitetura                                     |
| --------- | ------------------------------------------------ | --------------------------------------------------------- |
| **Java**  | `Application.java`                               | Ponto de entrada da Lambda, lida com o evento SQS.        |
|           | `application/usecase/ForwardMessageUseCase.java` | Caso de uso: reencaminhar a mensagem.                     |
|           | `domain/gateway/MessageSender.java`              | Interface do domínio: abstrai o envio de mensagens.       |
|           | `infrastructure/sqs/SQSSender.java`              | Implementação concreta do envio de mensagens via AWS SDK. |
|           | `pom.xml`                                        | Gerenciador de dependências Maven.                        |

---

## ⚖️ Comparativo entre as linguagens

| Critério                   | Python (SOLID)     | Go (MVC)             | Java (Clean Arch)     |
|---------------------------|--------------------|----------------------|------------------------|
| 🧠 Arquitetura aplicada    | SOLID + Serviços    | MVC                  | Clean Architecture     |
| 🚀 Tempo de cold start     | Médio (~200ms)     | Baixíssimo (~40ms)   | Alto (>600ms)          |
| 🧪 Facilidade de teste     | Alta               | Alta                 | Alta                   |
| 🛠 Complexidade do código  | Média              | Baixa                | Alta                   |
| 📦 Tamanho do deploy       | Pequeno            | Muito pequeno        | Grande (JAR > 10MB)    |
| 🧩 Integração com AWS SDK  | Ótima (boto3)      | Boa                  | Ótima                  |
| 🧰 Curva de aprendizado    | Baixa              | Média                | Alta                   |
| 🧼 Manutenção e clareza    | Alta               | Alta                 | Alta                   |
| 💸 Custo em execução       | Médio              | Baixo                | Alto                   |

---

## 🧪 Benchmark Estimado

Esses benchmarks são estimativas para **funções simples com mensagens pequenas** (<10 KB), testadas em ambiente de desenvolvimento:

| Linguagem | Cold Start (ms) | Execução (ms) | Deploy (MB) |
|-----------|------------------|----------------|-------------|
| Go        | ~40 ms           | ~20 ms         | ~3 MB       |
| Python    | ~200 ms          | ~30–50 ms      | ~6 MB       |
| Java      | ~600 ms          | ~50–100 ms     | ~15 MB      |

---

## ✅ Qual a melhor para esta aplicação?

**Recomendado: GO (Golang)**

- Melhor performance e menor tempo de cold start
- Ideal para funções simples e de alta concorrência
- Baixo custo operacional e de memória

**Alternativas:**
- Use **Python** se o time já domina boto3 e quer produtividade com flexibilidade.
- Use **Java** apenas se o reprocessamento envolver lógica de negócio mais complexa.

---

## 🛠 Como rodar localmente

### Python

```bash
cd lambda_dlq_forwarder
pip install -r requirements.txt -t .
zip -r function.zip .
```
---
### Go
```bash
cd golang_dlq_forwarder
GOOS=linux GOARCH=amd64 go build -o bootstrap main.go
zip function.zip bootstrap
```
---
### Go
```bash
cd java_dlq_forwarder
mvn clean package
cp target/dlq-forwarder.jar .
zip function.zip dlq-forwarder.jar
```
📬 Variável de Ambiente necessária
ORIGINAL_QUEUE_URL → URL da fila original para onde as mensagens serão reencaminhadas.
