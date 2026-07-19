package com.rawalpuneet.enterpriseai.mcp.tools; import java.util.Map; import org.springframework.stereotype.Component;
@Component public class PaymentsTool implements McpTool { public String name(){return "get_payment";} public String execute(Map<String,String> a){return "Payment "+a.getOrDefault("paymentId","unknown");} }
