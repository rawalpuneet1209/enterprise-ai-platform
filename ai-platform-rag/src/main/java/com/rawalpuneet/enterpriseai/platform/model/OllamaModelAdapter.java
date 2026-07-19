package com.rawalpuneet.enterpriseai.platform.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawalpuneet.enterpriseai.domain.AiModelPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public final class OllamaModelAdapter implements AiModelPort {
    private final RestClient client;
    private final ObjectMapper objectMapper;
    private final String model;

    public OllamaModelAdapter(String baseUrl, String model, ObjectMapper objectMapper) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
        this.model = model;
        this.objectMapper = objectMapper;
    }

    @Override public String generate(String systemPrompt, String userPrompt) {
        var body = Map.of("model", model, "stream", false, "prompt", systemPrompt + "\n\n" + userPrompt);
        var json = client.post().uri("/api/generate").contentType(MediaType.APPLICATION_JSON).body(body).retrieve().body(String.class);
        try { return objectMapper.readTree(json).path("response").asText(); }
        catch (Exception exception) { throw new IllegalStateException("Invalid Ollama response", exception); }
    }
}

