package com.insider.assignment.application.inbound;

import com.insider.assignment.domain.model.Message;

import java.util.List;

public interface FindSentMessagesUseCase {
    List<Message> findSentMessages();
}
