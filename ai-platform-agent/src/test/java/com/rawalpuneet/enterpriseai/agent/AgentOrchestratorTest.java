package com.rawalpuneet.enterpriseai.agent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
class AgentOrchestratorTest { @Test void routesToSpecialists(){ var o=new AgentOrchestrator(new PlannerAgent(),List.of(new CustomerAgent(),new PaymentsAgent(),new FraudAgent(),new KnowledgeAgent())); assertEquals(4,o.execute("customer payment blocked").size()); } }
