# Enterprise AI Platform

Canonical cumulative code baseline for Days 1–5 of the Enterprise AI Architect Masterclass (banking edition).

## Modules

| Module | Purpose |
|---|---|
| `domain` | Provider-neutral AI, RAG, agent, and banking contracts |
| `ai-platform` | Section-aware chunking, hybrid retrieval, RAG orchestration, Ollama adapter |
| `banking-ai-service` | Document/RAG APIs and a human-approved card replacement agent |
| `banking-mcp-server` | JSON-RPC MCP server exposing governed tools, resources, and prompts |

## Requirements

- JDK 25
- Maven 3.9+
- Docker (optional, for Ollama)

## Build and test

```bash
mvn clean verify
```

## Run locally

```bash
docker compose up -d
docker compose exec ollama ollama pull llama3.2:3b
mvn -pl banking-ai-service -am spring-boot:run
```

In another terminal:

```bash
mvn -pl banking-mcp-server -am spring-boot:run
```

The banking API listens on `8080`; MCP listens on `8081/mcp`.

## Try RAG

Ingest an approved policy:

```bash
curl -X POST http://localhost:8080/api/v1/documents \
  -H 'Content-Type: application/json' \
  -d '{
    "documentId":"CARD-POL-001",
    "title":"Stolen Debit Card Policy",
    "domain":"cards",
    "country":"IN",
    "classification":"internal",
    "version":"1.0",
    "effectiveDate":"2026-07-01",
    "content":"# Stolen cards\nFreeze a stolen debit card immediately. Replacement requires verified identity and approval."
  }'
```

Ask a grounded question:

```bash
curl -X POST http://localhost:8080/api/v1/rag/ask \
  -H 'Content-Type: application/json' \
  -d '{"question":"What should happen to a stolen debit card?","domain":"cards","country":"IN"}'
```

## Try the governed agent

```bash
curl -X POST http://localhost:8080/api/v1/agents/cards/replacement \
  -H 'Content-Type: application/json' \
  -d '{"customerId":"C-100","cardId":"CARD-200"}'
```

Use the returned `executionId`; replacement is not executed until approval:

```bash
curl -X POST http://localhost:8080/api/v1/agents/cards/executions/EXECUTION_ID/approve \
  -H 'Content-Type: application/json' \
  -d '{"approver":"operations-user"}'
```

## Try MCP

```bash
curl -X POST http://localhost:8081/mcp \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

See [architecture](docs/architecture.md) and the [Day 1–5 changelog](docs/CHANGELOG-DAYS-01-05.md).

## Scope and production warning

This is an executable teaching baseline, not a certified banking system. The in-memory knowledge store and card adapter deliberately keep local setup small. Before production use, replace them with durable stores and add OIDC/ABAC, data-loss prevention, encrypted audit trails, policy enforcement, evaluation gates, resilience controls, and formal model-risk approval.
