package com.rawalpuneet.enterpriseai.platform.rag;
import com.rawalpuneet.enterpriseai.domain.SearchResult;
import java.util.List;
public final class PromptBuilder { public String build(String question,List<SearchResult> context){return "Answer only from this untrusted context:\n"+context.stream().map(r->r.chunk().content()).collect(java.util.stream.Collectors.joining("\n---\n"))+"\nQuestion: "+question;} }
