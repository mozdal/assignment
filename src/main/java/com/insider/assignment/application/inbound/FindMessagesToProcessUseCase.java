package com.insider.assignment.application.inbound;

import com.insider.assignment.domain.model.Message;

import java.util.List;

public interface FindMessagesToProcessUseCase {
    List<Message> findMessagesToProcess();
}
