package com.rawalpuneet.enterpriseai.domain;

public record DocumentChunk(String chunkId, DocumentMetadata metadata, int sequence, String content) { }

