package com.rawalpuneet.enterpriseai.banking.api;

import com.rawalpuneet.enterpriseai.banking.rag.DocumentIngestionService;
import com.rawalpuneet.enterpriseai.domain.*;
import com.rawalpuneet.enterpriseai.platform.rag.RagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RagController {
    private final RagService ragService;
    private final DocumentIngestionService ingestion;

    public RagController(RagService ragService, DocumentIngestionService ingestion) {
        this.ragService = ragService;
        this.ingestion = ingestion;
    }

    @PostMapping("/rag/ask") GroundedAnswer ask(@Valid @RequestBody AskRequest request) {
        return ragService.ask(request.question(), new SearchFilter(request.domain(), request.country(), request.maximumClassification()));
    }

    @PostMapping("/documents") @ResponseStatus(HttpStatus.CREATED) IngestionResponse ingest(@Valid @RequestBody IngestRequest request) {
        var metadata = new DocumentMetadata(request.documentId(), request.title(), request.domain(), request.country(), request.classification(), request.version(), request.effectiveDate(), request.attributes());
        return new IngestionResponse(request.documentId(), ingestion.ingest(new KnowledgeDocument(metadata, request.content())));
    }

    public record AskRequest(@NotBlank String question, String domain, String country, String maximumClassification) { }
    public record IngestRequest(@NotBlank String documentId, @NotBlank String title, @NotBlank String domain,
            @NotBlank String country, @NotBlank String classification, @NotBlank String version,
            @NotNull LocalDate effectiveDate, Map<String, String> attributes, @NotBlank String content) { }
    public record IngestionResponse(String documentId, int chunkCount) { }
}

