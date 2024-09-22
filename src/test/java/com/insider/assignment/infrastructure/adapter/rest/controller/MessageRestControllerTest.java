package com.insider.assignment.infrastructure.adapter.rest.controller;

import com.insider.assignment.application.inbound.FindSentMessagesUseCase;
import com.insider.assignment.domain.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MessageRestControllerTest {

    @Mock
    private FindSentMessagesUseCase findSentMessagesUseCase;

    @InjectMocks
    private MessageRestController messageRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSentMessages_returnsSentMessages() {
        List<Message> messages = List.of(new Message());
        when(findSentMessagesUseCase.findSentMessages()).thenReturn(messages);

        ResponseEntity<List<Message>> response = messageRestController.getSentMessages();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(messages, response.getBody());
    }

    @Test
    void getSentMessages_returnsEmptyListWhenNoMessages() {
        when(findSentMessagesUseCase.findSentMessages()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Message>> response = messageRestController.getSentMessages();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void getSentMessages_handlesException() {
        when(findSentMessagesUseCase.findSentMessages()).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> messageRestController.getSentMessages());
    }
}