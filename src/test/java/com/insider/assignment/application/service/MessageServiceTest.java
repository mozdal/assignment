package com.insider.assignment.application.service;

import com.insider.assignment.application.outbound.MessageRepository;
import com.insider.assignment.domain.model.Message;
import com.insider.assignment.domain.service.MessageDomainService;
import com.insider.assignment.infrastructure.adapter.rest.client.ExternalMessageServiceClient;
import com.insider.assignment.infrastructure.adapter.rest.client.MessageServiceExternalRequest;
import com.insider.assignment.infrastructure.adapter.rest.client.MessageServiceExternalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageDomainService messageDomainService;

    @Mock
    private ExternalMessageServiceClient externalMessageServiceClient;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findMessagesToProcess_returnsMessages() {
        List<Message> messages = List.of(new Message());
        when(messageRepository.findMessagesToProcess()).thenReturn(messages);

        List<Message> result = messageService.findMessagesToProcess();

        assertEquals(messages, result);
    }

    @Test
    void saveMessage_validatesAndSavesMessage() {
        Message message = new Message();
        message.setId(1L);
        message.setStatus(Message.Status.NOT_SENT);
        when(messageRepository.findById(1L)).thenReturn(message);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageService.saveMessage(message);

        verify(messageDomainService).validateStatus(any(), any());
        assertEquals(message, result);
    }

    @Test
    void processMessages_updatesStatusAndMakesClientCall() {
        Message message1 = new Message();
        message1.setId(1L);
        message1.setStatus(Message.Status.NOT_SENT);

        Message message2 = new Message();
        message2.setId(1L);
        message2.setStatus(Message.Status.NOT_SENT);

        List<Message> messages = List.of(message1, message2);
        when(messageRepository.findMessagesToProcess()).thenReturn(messages);
        when(externalMessageServiceClient.postMessage(any(MessageServiceExternalRequest.class)))
                .thenReturn(new MessageServiceExternalResponse("externalId", "message"));

        messageService.processMessages(messages);

        verify(messageRepository, times(4)).setStatusById(any(), any());
        verify(externalMessageServiceClient, times(2)).postMessage(any(MessageServiceExternalRequest.class));
    }

    @Test
    void makeClientCall_successfulResponse_updatesStatusAndSetsRedisValue() {
        Message message = new Message();
        message.setId(1L);
        message.setStatus(Message.Status.PROCESSING);
        MessageServiceExternalResponse response = new MessageServiceExternalResponse("externalId", "message");
        when(externalMessageServiceClient.postMessage(any(MessageServiceExternalRequest.class)))
                .thenReturn(response);
        response.setMessageId("externalId");
        when(externalMessageServiceClient.postMessage(any(MessageServiceExternalRequest.class)))
                .thenReturn(response);

        CompletableFuture<MessageServiceExternalResponse> result = messageService.makeClientCall(message);

        assertEquals(response, result.join());
        verify(messageRepository).setStatusById(any(), any());
        verify(redisService).setValue(any(), any(), any());
    }

    @Test
    void findSentMessages_returnsSentMessagesWithProcessedTime() {
        Message message = new Message();
        message.setId(1L);
        message.setExternalMessageId("externalId");
        List<Message> messages = List.of(message);
        when(messageRepository.findSentMessages()).thenReturn(messages);
        when(redisService.getValue(any(), any())).thenReturn(Mono.just("processedTime"));

        List<Message> result = messageService.findSentMessages();

        assertEquals("processedTime", result.get(0).getProcessedTime());
    }

    @Test
    void createNewMessages_savesNewMessages() {
        messageService.createNewMessages();

        verify(messageRepository, times(1)).saveAll(any());
    }
}