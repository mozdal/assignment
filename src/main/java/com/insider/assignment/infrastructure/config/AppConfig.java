package com.insider.assignment.infrastructure.config;

import com.insider.assignment.application.outbound.MessageRepository;
import com.insider.assignment.application.service.MessageService;
import com.insider.assignment.application.service.RedisService;
import com.insider.assignment.domain.service.MessageDomainService;
import com.insider.assignment.infrastructure.adapter.rest.client.ExternalMessageServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MessageDomainService messageDomainService() {
        return new MessageDomainService();
    }

    @Bean
    public MessageService messageService(
            MessageRepository messageRepository,
            MessageDomainService messageDomainService,
            ExternalMessageServiceClient externalMessageServiceClient,
            RedisService redisService
    ) {
        return new MessageService(messageRepository, messageDomainService, externalMessageServiceClient, redisService);
    }
}
