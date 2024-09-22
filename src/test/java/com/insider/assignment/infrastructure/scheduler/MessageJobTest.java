package com.insider.assignment.infrastructure.scheduler;

import com.insider.assignment.application.service.MessageService;
import com.insider.assignment.domain.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MessageJobTest {

    @Mock
    private MessageService messageService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private MessageJob messageJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_noMessages_logsNoMessagesToProcess() {
        when(messageService.findMessagesToProcess()).thenReturn(Collections.emptyList());

        messageJob.execute(jobExecutionContext);

        verify(messageService, never()).processMessages(anyList());
    }

    @Test
    void execute_withMessages_processesMessages() {
        List<Message> messages = List.of(new Message());
        when(messageService.findMessagesToProcess()).thenReturn(messages);

        messageJob.execute(jobExecutionContext);

        verify(messageService).processMessages(messages);
    }

    @Test
    void execute_exceptionThrown_logsError() {
        when(messageService.findMessagesToProcess()).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> messageJob.execute(jobExecutionContext));
    }
}