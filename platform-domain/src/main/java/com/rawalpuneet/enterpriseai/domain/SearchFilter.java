package com.rawalpuneet.enterpriseai.domain;

public record SearchFilter(String domain, String country, String maximumClassification) {
    public static SearchFilter unrestricted() { return new SearchFilter(null, null, null); }
}

