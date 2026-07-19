package com.rawalpuneet.enterpriseai.mcp.tools; import java.util.Map; import org.springframework.stereotype.Component;
@Component public class FraudTool implements McpTool { public String name(){return "get_fraud_signals";} public String execute(Map<String,String> a){return "Fraud signals retrieved";} }
