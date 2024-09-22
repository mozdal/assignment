package com.insider.assignment.infrastructure.adapter.rest.controller;

import com.insider.assignment.infrastructure.scheduler.QuartzJobScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class StateRestControllerTest {

    @Mock
    private QuartzJobScheduler quartzJobScheduler;

    @InjectMocks
    private StateRestController stateRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startScheduler_startsSuccessfully() throws SchedulerException {
        ResponseEntity<?> response = stateRestController.startScheduler();

        assertEquals(200, response.getStatusCodeValue());
        verify(quartzJobScheduler).startMessageJob();
    }

    @Test
    void startScheduler_handlesSchedulerException() throws SchedulerException {
        doThrow(new SchedulerException("Scheduler error")).when(quartzJobScheduler).startMessageJob();

        ResponseEntity<?> response = stateRestController.startScheduler();

        assertEquals(500, response.getStatusCodeValue());
        verify(quartzJobScheduler).startMessageJob();
    }

    @Test
    void stopScheduler_stopsSuccessfully() throws SchedulerException {
        ResponseEntity<?> response = stateRestController.stopScheduler();

        assertEquals(200, response.getStatusCodeValue());
        verify(quartzJobScheduler).stopMessageJob();
    }

    @Test
    void stopScheduler_handlesSchedulerException() throws SchedulerException {
        doThrow(new SchedulerException("Scheduler error")).when(quartzJobScheduler).stopMessageJob();

        ResponseEntity<?> response = stateRestController.stopScheduler();

        assertEquals(500, response.getStatusCodeValue());
        verify(quartzJobScheduler).stopMessageJob();
    }
}