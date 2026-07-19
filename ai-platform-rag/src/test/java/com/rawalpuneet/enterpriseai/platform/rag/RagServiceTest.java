package com.rawalpuneet.enterpriseai.platform.rag;

import com.rawalpuneet.enterpriseai.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RagServiceTest {
    @Test void returnsGroundedAnswerWithCitation() {
        var metadata = new DocumentMetadata("CARD-1", "Debit card policy", "cards", "IN", "internal", "2.0", LocalDate.now(), Map.of());
        var store = new InMemoryHybridKnowledgeStore();
        store.replaceDocument(List.of(new DocumentChunk("CARD-1-1", metadata, 1, "A stolen debit card must be frozen immediately.")));
        var service = new RagService(store, (system, user) -> "Freeze the card immediately.");
        var answer = service.ask("What happens to a stolen debit card?", new SearchFilter("cards", "IN", "internal"));
        assertThat(answer.answer()).contains("Freeze");
        assertThat(answer.citations()).extracting(Citation::documentId).containsExactly("CARD-1");
    }
}
