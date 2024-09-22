package com.insider.assignment.infrastructure.adapter.rest.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class MessageServiceExternalResponse implements Serializable {
    private String messageId;
    private String message;
}
