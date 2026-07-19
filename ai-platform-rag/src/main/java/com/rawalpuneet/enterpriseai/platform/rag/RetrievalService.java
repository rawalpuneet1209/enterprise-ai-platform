package com.rawalpuneet.enterpriseai.platform.rag;
import com.rawalpuneet.enterpriseai.domain.*;
import java.util.List;
public final class RetrievalService { private final VectorSearchService search; private final Reranker reranker; public RetrievalService(VectorSearchService search,Reranker reranker){this.search=search;this.reranker=reranker;} public List<SearchResult> retrieve(String question,SearchFilter filter){return reranker.rerank(search.search(question,filter,20),5);} }
