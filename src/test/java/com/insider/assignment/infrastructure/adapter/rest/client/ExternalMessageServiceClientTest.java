package com.insider.assignment.infrastructure.adapter.rest.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExternalMessageServiceClientTest {

    @Mock
    private ExternalMessageServiceClient externalMessageServiceClient;

    @InjectMocks
    private ExternalMessageServiceClientTest externalMessageServiceClientTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postMessage_returnsResponse() {
        MessageServiceExternalRequest request = new MessageServiceExternalRequest("to", "content");
        MessageServiceExternalResponse response = new MessageServiceExternalResponse("messageId", "message");
        when(externalMessageServiceClient.postMessage(any(MessageServiceExternalRequest.class))).thenReturn(response);

        MessageServiceExternalResponse result = externalMessageServiceClient.postMessage(request);

        assertEquals(response, result);
    }

    @Test
    void postMessage_handlesNullRequest() {
        when(externalMessageServiceClient.postMessage(null)).thenThrow(new IllegalArgumentException("Request cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> externalMessageServiceClient.postMessage(null));
    }

    @Test
    void postMessage_handlesException() {
        MessageServiceExternalRequest request = new MessageServiceExternalRequest("to", "content");
        when(externalMessageServiceClient.postMessage(any(MessageServiceExternalRequest.class))).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> externalMessageServiceClient.postMessage(request));
    }
}