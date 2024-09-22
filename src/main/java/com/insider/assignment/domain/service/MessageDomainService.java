package com.insider.assignment.domain.service;

import com.insider.assignment.domain.exception.StateValidationException;
import com.insider.assignment.domain.model.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDomainService {

    public void validateStatus(Message.Status currentStatus, Message.Status nextStatus) {
        log.info("Validating status transition from {} to {}", currentStatus, nextStatus);
        if(!nextStatus.getValue().equals(currentStatus.getNextValue())) {
            log.error("Invalid status transition from {} to {}", currentStatus, nextStatus);
            throw new StateValidationException(currentStatus, nextStatus);
        }
    }
}
