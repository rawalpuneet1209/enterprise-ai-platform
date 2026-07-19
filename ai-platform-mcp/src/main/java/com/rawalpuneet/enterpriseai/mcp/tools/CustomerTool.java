package com.rawalpuneet.enterpriseai.mcp.tools; import java.util.Map; import org.springframework.stereotype.Component;
@Component public class CustomerTool implements McpTool { public String name(){return "get_customer";} public String execute(Map<String,String> a){return "Customer "+a.getOrDefault("customerId","unknown");} }
