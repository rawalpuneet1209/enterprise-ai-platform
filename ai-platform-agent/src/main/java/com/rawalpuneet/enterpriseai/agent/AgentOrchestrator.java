package com.rawalpuneet.enterpriseai.agent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AgentOrchestrator {
    private final PlannerAgent planner;
    private final Map<String, BankingAgent> agents;
    public AgentOrchestrator(PlannerAgent planner, List<BankingAgent> agents) {
        this.planner = planner;
        this.agents = agents.stream().collect(Collectors.toUnmodifiableMap(BankingAgent::capability, Function.identity()));
    }
    public List<AgentResult> execute(String request) {
        return planner.plan(request).stream().map(task -> agents.get(task.capability()).execute(task)).toList();
    }
}
