package com.rawalpuneet.enterpriseai.platform.rag;
import com.rawalpuneet.enterpriseai.domain.*;
import java.util.List;
public final class VectorSearchService { private final InMemoryHybridKnowledgeStore store; public VectorSearchService(InMemoryHybridKnowledgeStore store){this.store=store;} public List<SearchResult> search(String query, SearchFilter filter, int limit){return store.search(query,filter,limit);} }
