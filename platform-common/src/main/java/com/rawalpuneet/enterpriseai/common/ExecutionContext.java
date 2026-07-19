package com.rawalpuneet.enterpriseai.common;

import java.util.Map;

public record ExecutionContext(String correlationId, String customerId, Map<String, String> attributes) {
    public ExecutionContext { attributes = Map.copyOf(attributes); }
}
