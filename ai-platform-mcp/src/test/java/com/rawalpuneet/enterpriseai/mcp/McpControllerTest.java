package com.rawalpuneet.enterpriseai.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class McpControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final McpController controller = new McpController(mapper);

    @Test void listsGovernedBankingTools() throws Exception {
        var request = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree("""
                {"jsonrpc":"2.0","id":1,"method":"tools/list"}
                """);
        var response = controller.handle(request);
        assertThat(response.path("result").path("tools")).hasSize(2);
        assertThat(response.path("result").path("tools").get(1).path("name").asText()).isEqualTo("freeze_card");
    }
}

