package com.insider.assignment.application.inbound;

import com.insider.assignment.domain.model.Message;

import java.util.List;

public interface ProcessMessagesUseCase {
    void processMessages(List<Message> messagesToProces);
}
