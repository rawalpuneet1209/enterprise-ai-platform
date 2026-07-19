package com.rawalpuneet.enterpriseai.platform.rag;

import com.rawalpuneet.enterpriseai.domain.DocumentChunk;
import com.rawalpuneet.enterpriseai.domain.SearchFilter;
import com.rawalpuneet.enterpriseai.domain.SearchResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public final class InMemoryHybridKnowledgeStore {
    private final List<DocumentChunk> chunks = new CopyOnWriteArrayList<>();

    public void replaceDocument(List<DocumentChunk> newChunks) {
        if (newChunks.isEmpty()) return;
        var id = newChunks.getFirst().metadata().documentId();
        chunks.removeIf(chunk -> chunk.metadata().documentId().equals(id));
        chunks.addAll(newChunks);
    }

    public List<SearchResult> search(String query, SearchFilter filter, int limit) {
        var queryTerms = terms(query);
        var results = new ArrayList<SearchResult>();
        for (var chunk : chunks) {
            if (!allowed(chunk, filter)) continue;
            var contentTerms = terms(chunk.content());
            long overlap = queryTerms.stream().filter(contentTerms::contains).count();
            double keywordScore = queryTerms.isEmpty() ? 0 : (double) overlap / queryTerms.size();
            double phraseBoost = chunk.content().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) ? .35 : 0;
            double metadataBoost = chunk.metadata().title().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) ? .15 : 0;
            double score = Math.min(1, keywordScore + phraseBoost + metadataBoost);
            if (score > 0) results.add(new SearchResult(chunk, score));
        }
        return results.stream().sorted(Comparator.comparingDouble(SearchResult::score).reversed()).limit(limit).toList();
    }

    public int size() { return chunks.size(); }

    private boolean allowed(DocumentChunk chunk, SearchFilter filter) {
        return (filter.domain() == null || filter.domain().equalsIgnoreCase(chunk.metadata().domain()))
                && (filter.country() == null || filter.country().equalsIgnoreCase(chunk.metadata().country()));
    }

    private HashSet<String> terms(String value) {
        var terms = new HashSet<String>();
        for (var term : value.toLowerCase(Locale.ROOT).split("[^a-z0-9]+")) if (term.length() > 2) terms.add(term);
        return terms;
    }
}

