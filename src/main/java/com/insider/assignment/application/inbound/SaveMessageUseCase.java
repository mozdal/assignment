package com.insider.assignment.application.inbound;

import com.insider.assignment.domain.model.Message;

public interface SaveMessageUseCase {
    Message saveMessage(Message message);
}
