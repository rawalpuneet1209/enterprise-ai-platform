package com.rawalpuneet.enterpriseai.integration;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.rawalpuneet.enterpriseai.agent.*;
import java.util.List;
import org.junit.jupiter.api.Test;
class PlatformFlowTest { @Test void executesAPlannedBankingFlow(){var orchestrator=new AgentOrchestrator(new PlannerAgent(),List.of(new CustomerAgent(),new PaymentsAgent(),new FraudAgent(),new KnowledgeAgent()));assertFalse(orchestrator.execute("investigate blocked payment").isEmpty());} }
