package com.rawalpuneet.enterpriseai.agent;

public interface BankingAgent {
    String capability();
    AgentResult execute(AgentTask task);
}
