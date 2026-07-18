package com.rawalpuneet.enterpriseai.banking.agent;

import com.rawalpuneet.enterpriseai.banking.cards.InMemoryCardAdapter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardAgentServiceTest {
    @Test void replacementRequiresExplicitApproval() {
        var service = new CardAgentService(new InMemoryCardAdapter());
        var pending = service.requestReplacement("C-1", "CARD-1");
        assertThat(pending.status()).isEqualTo("AWAITING_HUMAN_APPROVAL");
        assertThat(service.approve(pending.executionId(), "operations-user").status()).isEqualTo("COMPLETED");
    }
}
