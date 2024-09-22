package com.insider.assignment.domain.exception;

import com.insider.assignment.domain.model.Message;

public class StateValidationException extends RuntimeException {
    public StateValidationException(Message.Status currentStatus, Message.Status nextStatus) {
        super(String.format(
                "Status %s not allowed to switch to status: %s",
                currentStatus.getValue(),
                nextStatus.getValue()
            )
        );
    }
}
