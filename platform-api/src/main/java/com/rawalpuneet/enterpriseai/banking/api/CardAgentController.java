package com.rawalpuneet.enterpriseai.banking.api;

import com.rawalpuneet.enterpriseai.banking.agent.CardAgentService;
import com.rawalpuneet.enterpriseai.domain.AgentExecution;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agents/cards")
public class CardAgentController {
    private final CardAgentService agent;
    public CardAgentController(CardAgentService agent) { this.agent = agent; }
    @PostMapping("/replacement") AgentExecution request(@Valid @RequestBody ReplacementRequest request) { return agent.requestReplacement(request.customerId(), request.cardId()); }
    @PostMapping("/executions/{id}/approve") AgentExecution approve(@PathVariable String id, @Valid @RequestBody ApprovalRequest request) { return agent.approve(id, request.approver()); }
    public record ReplacementRequest(@NotBlank String customerId, @NotBlank String cardId) { }
    public record ApprovalRequest(@NotBlank String approver) { }
}

