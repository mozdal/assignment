package com.insider.assignment.infrastructure.adapter.rest.controller;

import com.insider.assignment.application.inbound.FindSentMessagesUseCase;
import com.insider.assignment.domain.model.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Message API", description = "Operations pertaining to messages")
@RestController
@RequestMapping(path = "/message")
public class MessageRestController {

    private final FindSentMessagesUseCase findSentMessagesUseCase;

    public MessageRestController(FindSentMessagesUseCase findSentMessagesUseCase) {
        this.findSentMessagesUseCase = findSentMessagesUseCase;
    }

    @Operation(summary = "Get Sent Messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Sent messages retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))
                    }),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping(path = "/sent")
    public ResponseEntity<List<Message>> getSentMessages() {
        return ResponseEntity.ok(findSentMessagesUseCase.findSentMessages());
    }
}
