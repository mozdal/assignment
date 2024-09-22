package com.insider.assignment.infrastructure.adapter.rest.controller;

import com.insider.assignment.infrastructure.scheduler.QuartzJobScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "State API", description = "Operations pertaining to the state of the scheduler")
@RestController
@RequestMapping(path = "/state")
public class StateRestController {

    private final QuartzJobScheduler quartzJobScheduler;

    public StateRestController(QuartzJobScheduler quartzJobScheduler) {
        this.quartzJobScheduler = quartzJobScheduler;
    }

    @Operation(summary = "Start Scheduler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping(path = "/start")
    public ResponseEntity<?> startScheduler() {
        try {
            quartzJobScheduler.startMessageJob();
            return ResponseEntity.ok().build();
        } catch (SchedulerException e) {
            return ResponseEntity.internalServerError().body(String.format("Could not start quartz job! Exception: %s", e));

        }
    }

    @Operation(summary = "Stop Scheduler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Accepted",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping(path = "/stop")
    public ResponseEntity<?> stopScheduler() {
        try {
            quartzJobScheduler.stopMessageJob();
            return ResponseEntity.ok().build();
        } catch (SchedulerException e) {
            return ResponseEntity.internalServerError().body(String.format("Could not stop quartz job! Exception: %s", e));
        }
    }
}
