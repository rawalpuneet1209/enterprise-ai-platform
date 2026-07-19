package com.rawalpuneet.enterpriseai.platform.rag;
import com.rawalpuneet.enterpriseai.domain.SearchResult;
import java.util.Comparator;
import java.util.List;
public final class Reranker { public List<SearchResult> rerank(List<SearchResult> results,int limit){return results.stream().sorted(Comparator.comparingDouble(SearchResult::score).reversed()).limit(limit).toList();} }
