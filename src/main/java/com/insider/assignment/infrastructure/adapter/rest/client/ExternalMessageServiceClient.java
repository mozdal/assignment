package com.insider.assignment.infrastructure.adapter.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "externalMessageServiceClient", url = "${spring.cloud.openfeign.client.config.externalMessageServiceClient.url}")
public interface ExternalMessageServiceClient {

    @PostMapping("/message")
    MessageServiceExternalResponse postMessage(@RequestBody MessageServiceExternalRequest request);
}
