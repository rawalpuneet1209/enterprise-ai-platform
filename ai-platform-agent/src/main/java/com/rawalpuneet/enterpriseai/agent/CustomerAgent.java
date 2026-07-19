package com.rawalpuneet.enterpriseai.agent;
import org.springframework.stereotype.Component;
@Component public class CustomerAgent implements BankingAgent { public String capability(){return "customer";} public AgentResult execute(AgentTask task){return new AgentResult(capability(), "Customer context checked", true);} }
