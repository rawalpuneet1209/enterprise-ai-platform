package com.rawalpuneet.enterpriseai.banking.agent;

import com.rawalpuneet.enterpriseai.domain.AgentExecution;
import com.rawalpuneet.enterpriseai.domain.CardPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CardAgentService {
    private final CardPort cardPort;
    private final ConcurrentHashMap<String, PendingAction> pending = new ConcurrentHashMap<>();

    public CardAgentService(CardPort cardPort) { this.cardPort = cardPort; }

    public AgentExecution requestReplacement(String customerId, String cardId) {
        var id = UUID.randomUUID().toString();
        pending.put(id, new PendingAction(customerId, cardId));
        return new AgentExecution(id, "AWAITING_HUMAN_APPROVAL", List.of(
                new AgentExecution.Step("validate", "COMPLETED", "Request validated"),
                new AgentExecution.Step("replace-card", "PENDING_APPROVAL", "Sensitive action requires approval")), Instant.now());
    }

    public AgentExecution approve(String executionId, String approver) {
        if (approver == null || approver.isBlank()) throw new IllegalArgumentException("approver is required");
        var action = pending.remove(executionId);
        if (action == null) throw new IllegalArgumentException("No pending execution: " + executionId);
        var result = cardPort.replace(action.customerId(), action.cardId(), executionId);
        return new AgentExecution(executionId, "COMPLETED", List.of(
                new AgentExecution.Step("human-approval", "COMPLETED", "Approved by " + approver),
                new AgentExecution.Step("replace-card", "COMPLETED", result.message())), Instant.now());
    }

    private record PendingAction(String customerId, String cardId) { }
}

