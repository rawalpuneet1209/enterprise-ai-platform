package com.rawalpuneet.enterpriseai.banking.rag;

import com.rawalpuneet.enterpriseai.domain.KnowledgeDocument;
import com.rawalpuneet.enterpriseai.platform.rag.InMemoryHybridKnowledgeStore;
import com.rawalpuneet.enterpriseai.platform.rag.SemanticDocumentChunker;
import org.springframework.stereotype.Service;

@Service
public class DocumentIngestionService {
    private final SemanticDocumentChunker chunker;
    private final InMemoryHybridKnowledgeStore store;

    public DocumentIngestionService(SemanticDocumentChunker chunker, InMemoryHybridKnowledgeStore store) {
        this.chunker = chunker;
        this.store = store;
    }

    public int ingest(KnowledgeDocument document) {
        var chunks = chunker.chunk(document);
        store.replaceDocument(chunks);
        return chunks.size();
    }
}

