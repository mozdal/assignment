package com.insider.assignment.application.inbound;

import com.insider.assignment.domain.model.Message;
import com.insider.assignment.infrastructure.adapter.rest.client.MessageServiceExternalResponse;

import java.util.concurrent.CompletableFuture;

public interface MakeClientCallUseCase {
    CompletableFuture<MessageServiceExternalResponse> makeClientCall(Message message);
}
