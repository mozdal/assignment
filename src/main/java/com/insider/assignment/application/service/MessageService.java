package com.insider.assignment.application.service;

import com.insider.assignment.application.inbound.*;
import com.insider.assignment.application.outbound.MessageRepository;
import com.insider.assignment.domain.model.Message;
import com.insider.assignment.domain.service.MessageDomainService;
import com.insider.assignment.infrastructure.adapter.rest.client.ExternalMessageServiceClient;
import com.insider.assignment.infrastructure.adapter.rest.client.MessageServiceExternalRequest;
import com.insider.assignment.infrastructure.adapter.rest.client.MessageServiceExternalResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class MessageService implements
        FindMessagesToProcessUseCase,
        SaveMessageUseCase,
        ProcessMessagesUseCase,
        MakeClientCallUseCase,
        FindSentMessagesUseCase,
        CreateNewMessagesUseCase
{

    private final MessageRepository messageRepository;
    private final MessageDomainService messageDomainService;
    private final ExternalMessageServiceClient externalMessageServiceClient;
    private final RedisService redisService;

    @Override
    public List<Message> findMessagesToProcess() {
        return messageRepository.findMessagesToProcess();
    }

    @Override
    public Message saveMessage(Message message) {
        Message.Status currentStatus = messageRepository.findById(message.getId()).getStatus();
        messageDomainService.validateStatus(currentStatus, message.getStatus());
        return messageRepository.save(message);
    }

    @Override
    public void processMessages(List<Message> messagesToProcess) {
        log.info("Processing messages: {}", messagesToProcess);
        messagesToProcess.forEach(message -> messageRepository.setStatusById(message.markAsProcessing().getStatus(), message.getId()));

        List<CompletableFuture<MessageServiceExternalResponse>> futures = messagesToProcess.stream()
                        .map(this::makeClientCall).toList();

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allOfFuture.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .toList()).join();
    }

    @Async
    @Transactional
    public CompletableFuture<MessageServiceExternalResponse> makeClientCall(Message message) {
        log.info("Making external rest asynchronously... Message ID: " + message.getId());

        return CompletableFuture.supplyAsync(() -> {
            try {
                MessageServiceExternalResponse response =
                        externalMessageServiceClient.postMessage(MessageServiceExternalRequest.builder()
                            .content(message.getMessageContent())
                            .to(message.getPhoneNumber())
                            .build());
                log.info("External rest cal made successfully... Message ID: " + message.getId());

                messageRepository.setStatusById(message.markAsSent().getStatus(), message.getId());
                log.info("Message state updated to SENT... Message ID: " + message.getId());

                redisService.setValue(response.getMessageId(), Message.ENTITY_TYPE_NAME, String.valueOf(System.currentTimeMillis()));
                log.info("Redis value set... Message ID: " + message.getId());

                messageRepository.setExternalMessageIdById(response.getMessageId(), message.getId());
                log.info("External message ID set... Message ID: " + message.getId());

                return response;
            } catch (Exception e) {
                log.error("Error occurred while making external rest call... Message ID: " + message.getId(), e);
                messageRepository.setStatusById(message.markAsError().getStatus(), message.getId());
                return null;
            }
        });
    }

    @Override
    public List<Message> findSentMessages() {
        List<Message> sentMessages = messageRepository.findSentMessages();

        sentMessages.forEach(message -> {
            if(message.getExternalMessageId() != null) {
                String value = redisService.getValue(message.getExternalMessageId(), Message.ENTITY_TYPE_NAME).block();
                message.setProcessedTime(value);
            } else {
                log.error("External message ID is null for message ID: " + message.getId());
                sentMessages.remove(message);
            }
        });

        return sentMessages;
    }

    @Override
    public void createNewMessages() {
        List<Message> newMessages = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String messageContent = "Insider - " + RandomStringUtils.randomAlphanumeric(10);
            Random rand = new Random();
            int randomNumber = rand.nextInt(9000000) + 1000000;
            String phoneNumber = "+90532" + randomNumber;
            newMessages.add(Message.builder()
                    .messageContent(messageContent)
                    .phoneNumber(phoneNumber)
                    .status(Message.Status.NOT_SENT)
                    .build());
        }

        messageRepository.saveAll(newMessages);
    }
}
