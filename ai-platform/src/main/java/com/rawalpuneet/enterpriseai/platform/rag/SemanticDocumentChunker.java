package com.rawalpuneet.enterpriseai.platform.rag;

import com.rawalpuneet.enterpriseai.domain.DocumentChunk;
import com.rawalpuneet.enterpriseai.domain.KnowledgeDocument;

import java.util.ArrayList;
import java.util.List;

public final class SemanticDocumentChunker {
    private final int maximumCharacters;

    public SemanticDocumentChunker(int maximumCharacters) {
        if (maximumCharacters < 100) throw new IllegalArgumentException("maximumCharacters must be at least 100");
        this.maximumCharacters = maximumCharacters;
    }

    public List<DocumentChunk> chunk(KnowledgeDocument document) {
        var sections = document.content().split("(?m)(?=^#{1,3}\\s)");
        var pieces = new ArrayList<String>();
        for (var section : sections) splitOversized(section.trim(), pieces);
        var result = new ArrayList<DocumentChunk>();
        for (int i = 0; i < pieces.size(); i++) {
            var id = document.metadata().documentId() + "-" + (i + 1);
            result.add(new DocumentChunk(id, document.metadata(), i + 1, pieces.get(i)));
        }
        return List.copyOf(result);
    }

    private void splitOversized(String text, List<String> target) {
        if (text.isBlank()) return;
        if (text.length() <= maximumCharacters) { target.add(text); return; }
        var paragraphs = text.split("\\R\\s*\\R");
        var current = new StringBuilder();
        for (var paragraph : paragraphs) {
            if (current.length() > 0 && current.length() + paragraph.length() + 2 > maximumCharacters) {
                target.add(current.toString());
                current.setLength(0);
            }
            if (paragraph.length() > maximumCharacters) {
                for (int start = 0; start < paragraph.length(); start += maximumCharacters) {
                    target.add(paragraph.substring(start, Math.min(paragraph.length(), start + maximumCharacters)));
                }
            } else {
                if (current.length() > 0) current.append("\n\n");
                current.append(paragraph);
            }
        }
        if (!current.isEmpty()) target.add(current.toString());
    }
}

