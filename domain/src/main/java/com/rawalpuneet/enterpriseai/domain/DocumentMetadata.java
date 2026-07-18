package com.rawalpuneet.enterpriseai.domain;

import java.time.LocalDate;
import java.util.Map;

public record DocumentMetadata(
        String documentId,
        String title,
        String domain,
        String country,
        String classification,
        String version,
        LocalDate effectiveDate,
        Map<String, String> attributes) {

    public DocumentMetadata {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}

