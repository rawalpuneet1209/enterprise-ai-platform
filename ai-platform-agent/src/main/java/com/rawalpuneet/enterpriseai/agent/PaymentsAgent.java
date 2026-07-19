package com.rawalpuneet.enterpriseai.agent;
import org.springframework.stereotype.Component;
@Component public class PaymentsAgent implements BankingAgent { public String capability(){return "payments";} public AgentResult execute(AgentTask task){return new AgentResult(capability(), "Payment state checked", true);} }
