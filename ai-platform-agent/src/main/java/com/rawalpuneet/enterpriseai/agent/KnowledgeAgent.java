package com.rawalpuneet.enterpriseai.agent;
import org.springframework.stereotype.Component;
@Component public class KnowledgeAgent implements BankingAgent { public String capability(){return "knowledge";} public AgentResult execute(AgentTask task){return new AgentResult(capability(), "Relevant policy context requested", true);} }
