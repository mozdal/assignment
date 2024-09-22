package com.insider.assignment.domain.service;

import com.insider.assignment.domain.exception.StateValidationException;
import com.insider.assignment.domain.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageDomainServiceTest {

    private MessageDomainService messageDomainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageDomainService = new MessageDomainService();
    }

    @Test
    void validateStatus_allowsValidTransition() {
        Message.Status currentStatus = Message.Status.NOT_SENT;
        Message.Status nextStatus = Message.Status.PROCESSING;

        messageDomainService.validateStatus(currentStatus, nextStatus);
    }

    @Test
    void validateStatus_throwsExceptionForInvalidTransition() {
        Message.Status currentStatus = Message.Status.SENT;
        Message.Status nextStatus = Message.Status.NOT_SENT;

        assertThrows(StateValidationException.class, () -> messageDomainService.validateStatus(currentStatus, nextStatus));
    }

    @Test
    void validateStatus_throwsExceptionForSameStatus() {
        Message.Status currentStatus = Message.Status.SENT;
        Message.Status nextStatus = Message.Status.SENT;

        assertThrows(StateValidationException.class, () -> messageDomainService.validateStatus(currentStatus, nextStatus));
    }
}