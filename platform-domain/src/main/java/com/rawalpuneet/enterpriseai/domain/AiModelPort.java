package com.rawalpuneet.enterpriseai.domain;

public interface AiModelPort {
    String generate(String systemPrompt, String userPrompt);
}

