package com.rawalpuneet.enterpriseai.domain;

import java.time.Instant;
import java.util.List;

public record AgentExecution(String executionId, String status, List<Step> steps, Instant createdAt) {
    public AgentExecution { steps = List.copyOf(steps); }
    public record Step(String name, String status, String detail) { }
}
