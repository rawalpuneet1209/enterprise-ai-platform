package com.rawalpuneet.enterpriseai.platform.rag;

import com.rawalpuneet.enterpriseai.domain.DocumentMetadata;
import com.rawalpuneet.enterpriseai.domain.KnowledgeDocument;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SemanticDocumentChunkerTest {
    @Test void preservesMetadataAndSectionBoundaries() {
        var metadata = new DocumentMetadata("POL-1", "Card Policy", "cards", "IN", "internal", "1.0", LocalDate.now(), Map.of());
        var chunks = new SemanticDocumentChunker(100).chunk(new KnowledgeDocument(metadata, "# First\nShort text.\n\n# Second\nMore text."));
        assertThat(chunks).hasSize(2).allMatch(chunk -> chunk.metadata().equals(metadata));
    }
}

