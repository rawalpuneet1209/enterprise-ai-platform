package com.rawalpuneet.enterpriseai.agent;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlannerAgent {
    public List<AgentTask> plan(String request) {
        var text = request.toLowerCase();
        var tasks = new java.util.ArrayList<AgentTask>();
        if (text.contains("customer")) tasks.add(new AgentTask("customer", request, java.util.Map.of()));
        if (text.contains("payment") || text.contains("transfer")) tasks.add(new AgentTask("payments", request, java.util.Map.of()));
        if (text.contains("fraud") || text.contains("blocked")) tasks.add(new AgentTask("fraud", request, java.util.Map.of()));
        tasks.add(new AgentTask("knowledge", request, java.util.Map.of()));
        return List.copyOf(tasks);
    }
}
