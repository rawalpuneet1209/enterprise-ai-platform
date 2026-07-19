package com.rawalpuneet.enterpriseai.banking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawalpuneet.enterpriseai.domain.AiModelPort;
import com.rawalpuneet.enterpriseai.platform.model.OllamaModelAdapter;
import com.rawalpuneet.enterpriseai.platform.rag.InMemoryHybridKnowledgeStore;
import com.rawalpuneet.enterpriseai.platform.rag.RagService;
import com.rawalpuneet.enterpriseai.platform.rag.SemanticDocumentChunker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {
    @Bean InMemoryHybridKnowledgeStore knowledgeStore() { return new InMemoryHybridKnowledgeStore(); }
    @Bean SemanticDocumentChunker chunker(@Value("${app.rag.chunk-size:1200}") int size) { return new SemanticDocumentChunker(size); }
    @Bean AiModelPort aiModel(@Value("${app.ollama.base-url:http://localhost:11434}") String baseUrl,
                              @Value("${app.ollama.model:llama3.2:3b}") String model, ObjectMapper mapper) {
        return new OllamaModelAdapter(baseUrl, model, mapper);
    }
    @Bean RagService ragService(InMemoryHybridKnowledgeStore store, AiModelPort model) { return new RagService(store, model); }
}

