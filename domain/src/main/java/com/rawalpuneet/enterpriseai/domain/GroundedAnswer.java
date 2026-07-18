package com.rawalpuneet.enterpriseai.domain;

import java.util.List;

public record GroundedAnswer(String answer, List<Citation> citations, String traceId) {
    public GroundedAnswer { citations = List.copyOf(citations); }
}

