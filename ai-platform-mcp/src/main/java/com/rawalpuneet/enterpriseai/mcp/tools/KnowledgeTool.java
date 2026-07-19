package com.rawalpuneet.enterpriseai.mcp.tools; import java.util.Map; import org.springframework.stereotype.Component;
@Component public class KnowledgeTool implements McpTool { public String name(){return "search_knowledge";} public String execute(Map<String,String> a){return "Knowledge search accepted";} }
