package com.rawalpuneet.enterpriseai.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class McpController {
    private static final String PROTOCOL_VERSION = "2025-06-18";
    private final ObjectMapper mapper;
    private final Set<String> frozenCards = ConcurrentHashMap.newKeySet();

    public McpController(ObjectMapper mapper) { this.mapper = mapper; }

    @PostMapping(path = "/mcp", consumes = "application/json", produces = "application/json")
    JsonNode handle(@RequestBody ObjectNode request) {
        var id = request.get("id");
        var method = request.path("method").asText();
        try {
            return success(id, switch (method) {
                case "initialize" -> initialize();
                case "ping" -> mapper.createObjectNode();
                case "tools/list" -> tools();
                case "tools/call" -> callTool(request.path("params"));
                case "resources/list" -> resources();
                case "resources/read" -> readResource(request.path("params"));
                case "prompts/list" -> prompts();
                case "prompts/get" -> getPrompt(request.path("params"));
                default -> throw new McpException(-32601, "Method not found: " + method);
            });
        } catch (McpException exception) {
            return error(id, exception.code, exception.getMessage());
        } catch (Exception exception) {
            return error(id, -32603, "Internal error");
        }
    }

    private ObjectNode initialize() {
        var result = mapper.createObjectNode();
        result.put("protocolVersion", PROTOCOL_VERSION);
        result.set("capabilities", mapper.createObjectNode().set("tools", mapper.createObjectNode().put("listChanged", false)));
        result.set("serverInfo", mapper.createObjectNode().put("name", "banking-cards-mcp").put("version", "1.0.0"));
        return result;
    }

    private ObjectNode tools() {
        var tools = mapper.createArrayNode();
        tools.add(tool("get_card_status", "Read a customer's card status; never changes card state", false));
        tools.add(tool("freeze_card", "Freeze a card after customer identity has been verified", true));
        return mapper.createObjectNode().set("tools", tools);
    }

    private ObjectNode tool(String name, String description, boolean destructive) {
        var schema = mapper.createObjectNode().put("type", "object");
        schema.set("properties", mapper.createObjectNode()
                .set("cardId", mapper.createObjectNode().put("type", "string").put("description", "Opaque card identifier")));
        schema.set("required", mapper.createArrayNode().add("cardId"));
        var tool = mapper.createObjectNode().put("name", name).put("description", description);
        tool.set("inputSchema", schema);
        tool.set("annotations", mapper.createObjectNode().put("destructiveHint", destructive));
        return tool;
    }

    private ObjectNode callTool(JsonNode params) {
        var name = required(params, "name");
        var cardId = required(params.path("arguments"), "cardId");
        String message;
        if ("get_card_status".equals(name)) message = frozenCards.contains(cardId) ? "FROZEN" : "ACTIVE";
        else if ("freeze_card".equals(name)) { frozenCards.add(cardId); message = "FROZEN"; }
        else throw new McpException(-32602, "Unknown tool: " + name);
        var content = mapper.createArrayNode().add(mapper.createObjectNode().put("type", "text").put("text", message));
        return mapper.createObjectNode().set("content", content);
    }

    private ObjectNode resources() {
        var values = mapper.createArrayNode().add(mapper.createObjectNode().put("uri", "bank://policies/cards/replacement")
                .put("name", "Card replacement policy").put("mimeType", "text/plain"));
        return mapper.createObjectNode().set("resources", values);
    }

    private ObjectNode readResource(JsonNode params) {
        var uri = required(params, "uri");
        if (!"bank://policies/cards/replacement".equals(uri)) throw new McpException(-32602, "Unknown resource");
        var values = mapper.createArrayNode().add(mapper.createObjectNode().put("uri", uri).put("mimeType", "text/plain")
                .put("text", "Freeze a stolen card immediately. Replacement requires verified identity and human approval."));
        return mapper.createObjectNode().set("contents", values);
    }

    private ObjectNode prompts() {
        var args = mapper.createArrayNode().add(mapper.createObjectNode().put("name", "cardId").put("required", true));
        var values = mapper.createArrayNode().add(mapper.createObjectNode().put("name", "card_incident_review")
                .put("description", "Prepare a governed card incident review").set("arguments", args));
        return mapper.createObjectNode().set("prompts", values);
    }

    private ObjectNode getPrompt(JsonNode params) {
        if (!"card_incident_review".equals(required(params, "name"))) throw new McpException(-32602, "Unknown prompt");
        var cardId = required(params.path("arguments"), "cardId");
        var messages = mapper.createArrayNode().add(mapper.createObjectNode().put("role", "user")
                .set("content", mapper.createObjectNode().put("type", "text").put("text", "Review card incident for " + cardId + ". Do not execute changes without approval.")));
        return mapper.createObjectNode().put("description", "Governed incident review").set("messages", messages);
    }

    private ObjectNode success(JsonNode id, JsonNode result) {
        var response = mapper.createObjectNode().put("jsonrpc", "2.0");
        response.set("id", id);
        response.set("result", result);
        return response;
    }
    private ObjectNode error(JsonNode id, int code, String message) {
        var response = mapper.createObjectNode().put("jsonrpc", "2.0");
        response.set("id", id);
        response.set("error", mapper.createObjectNode().put("code", code).put("message", message));
        return response;
    }
    private String required(JsonNode node, String field) {
        var value = node.path(field).asText();
        if (value.isBlank()) throw new McpException(-32602, "Missing required field: " + field);
        return value;
    }
    private static final class McpException extends RuntimeException {
        private final int code;
        McpException(int code, String message) { super(message); this.code = code; }
    }
}
