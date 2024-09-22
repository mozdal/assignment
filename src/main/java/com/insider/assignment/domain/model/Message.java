package com.insider.assignment.domain.model;

import com.insider.assignment.domain.exception.StateValidationException;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private Long id;
    private String messageContent;
    private Status status;
    private String phoneNumber;
    private String externalMessageId;
    private String processedTime;

    public static final String ENTITY_TYPE_NAME = "message";

    public Message markAsProcessing() {
        validateState(Status.PROCESSING);
        this.status = Status.PROCESSING;
        return this;
    }

    public Message markAsSent() {
        validateState(Status.SENT);
        this.status = Status.SENT;
        return this;
    }

    public Message markAsError() {
        validateState(Status.ERROR);
        this.status = Status.ERROR;
        return this;
    }

    public enum Status {
        NOT_SENT("NOT_SENT", "PROCESSING"),
        PROCESSING("PROCESSING", "SENT"),
        ERROR("ERROR", null),
        SENT("SENT", null);

        private final String value;
        private final String nextValue;

        Status(String value, String nextValue) {
            this.value = value;
            this.nextValue = nextValue;
        }

        public String getValue(Status this) {
            return this.value;
        }
        public String getNextValue(Status this) { return this.nextValue; }

    }

    private void validateState(Status nextStatus) {
        switch (this.status) {
            case NOT_SENT -> {
                if (nextStatus != Status.PROCESSING) {
                    throw new StateValidationException(this.status, nextStatus);
                }
            }
            case PROCESSING -> {
                if (nextStatus != Status.SENT && nextStatus != Status.ERROR) {
                    throw new StateValidationException(this.status, nextStatus);
                }
            }
            case ERROR ->
                    throw new StateValidationException(this.status, nextStatus);
            case SENT ->
                    throw new StateValidationException(this.status, nextStatus);
        }
    }
}
