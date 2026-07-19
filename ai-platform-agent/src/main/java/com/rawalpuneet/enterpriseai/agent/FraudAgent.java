package com.rawalpuneet.enterpriseai.agent;
import org.springframework.stereotype.Component;
@Component public class FraudAgent implements BankingAgent { public String capability(){return "fraud";} public AgentResult execute(AgentTask task){return new AgentResult(capability(), "Fraud signals checked", true);} }
