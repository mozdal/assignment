package com.insider.assignment.infrastructure.adapter.rest.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageServiceExternalRequest {
    private String to;
    private String content;
}
