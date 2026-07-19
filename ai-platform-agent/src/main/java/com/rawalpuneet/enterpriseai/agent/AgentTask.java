package com.rawalpuneet.enterpriseai.agent;

import java.util.Map;
public record AgentTask(String capability, String instruction, Map<String, String> inputs) {
    public AgentTask { inputs = Map.copyOf(inputs); }
}
