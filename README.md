# 🛒 FeirAssina
### Serviço de Assinatura de Feira com Entrega — Projeto de Software

---

## 📋 Sobre o Projeto

O **FeirAssina** é um sistema de assinatura de cestas de feira com entrega domiciliar. O assinante escolhe seu plano, monta sua cesta com frutas, legumes e verduras, informa o endereço e realiza o pagamento — tudo em um fluxo simples, seguro e automatizado.

> Projeto desenvolvido para a disciplina de **Projeto de Software** — Universidade Presbiteriana Mackenzie.

---

## 🏗️ Arquitetura

O sistema segue o padrão **MVC com separação por responsabilidade (UML)**, composto por cinco camadas:

| Estereótipo | Descrição |
|---|---|
| `<<boundary>>` | Interface / Tela (ponto de contato com o usuário) |
| `<<control>>` | Classes de Controle (lógica de aplicação) |
| `<<entity>>` | Entidades de Domínio (modelo de dados) |
| `<<service>>` | Serviços Externos (SMS, Gateway de Pagamento) |
| `<<repository>>` | Repositórios de Persistência (banco de dados) |

---

## 🔄 Fluxo Principal — Diagrama de Sequência

O fluxo completo de assinatura é dividido em **5 etapas**:

### 1️⃣ Autenticação
- Assinante informa o número de celular
- Sistema envia código via **SMS** (`SmsService`)
- Código é validado e os planos disponíveis são carregados do `AssinaturaRepository`

### 2️⃣ Seleção de Plano
- Assinante seleciona o plano desejado
- `AssinaturaController` define o plano na entidade `Assinatura`

### 3️⃣ Montagem da Cesta
- Assinante escolhe itens de **Frutas**, **Legumes** e **Verduras**
- Cada categoria é adicionada via `CestaController` e persistida no `CestaRepository`

### 4️⃣ Endereço e Confirmação
- Assinante confirma endereço de entrega
- Endereço é validado, cesta e assinatura são salvas
- Status da assinatura: `aguardando aprovação`

### 5️⃣ Pagamento
- Assinante informa dados do cartão
- `PagamentoController` processa via `GatewayPagamento` (serviço externo)
- Pagamento aprovado → assinatura marcada como **ativa**
- Assinante recebe número de protocolo

---

## 🧩 Componentes

```
TelaAssinatura          → Boundary (UI principal)
AuthController          → Autenticação via SMS
AssinaturaController    → Gerencia plano e assinatura
CestaController         → Gerencia itens da cesta
PagamentoController     → Orquestra pagamento

Assinatura              → Entidade principal
Plano                   → Planos disponíveis
Cesta                   → Itens selecionados
Pagamento               → Dados e status do pagamento
Endereco                → Endereço de entrega

SmsService              → Envio de SMS (externo)
GatewayPagamento        → Processamento de pagamento (externo)

AssinaturaRepository    → Persistência de assinaturas
CestaRepository         → Persistência de cestas
PagamentoRepository     → Persistência de pagamentos
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 11+
- Terminal / linha de comando

### Compilar

```bash
javac -d out -sourcepath src $(find src -name "*.java")
```

### Executar

```bash
java -cp out feira.Main
```

---

## 🧪 Cenários de Execução

O `Main` demonstra dois cenários automaticamente ao rodar:

### ✅ Cenário 1 — Assinatura bem-sucedida (PIX)

Fluxo completo com autenticação via OTP, seleção do plano **Plus**, montagem de cesta com 4 itens e pagamento aprovado via PIX:

```
[PagamentoService] Pagamento APROVADO — TXN-F4FCDE42-A50

╔══════════════════════════════════════════╗
║  ✓ Assinatura ativada com sucesso!       ║
║  Próxima entrega: 2026-05-24             ║
╚══════════════════════════════════════════╝
```

### ❌ Cenário 2 — Pagamento recusado (cartão inválido)

Fluxo com plano **Básico** e tentativa de pagamento via cartão de crédito inválido:

```
[PagamentoService] Pagamento RECUSADO. Tente novamente.
[Main] Resultado: RECUSADO — Usuário deve tentar outro método.
```

---

## 💳 Planos Disponíveis

| ID | Nome | Preço | Frequência |
|---|---|---|---|
| `p-001` | Básico | R$ 79,90 | 1x por semana |
| `p-002` | Plus | R$ 139,90 | 2x por semana |
| `p-003` | Premium | R$ 219,90 | 4x por semana |


---
