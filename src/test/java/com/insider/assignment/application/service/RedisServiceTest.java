package com.insider.assignment.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RedisServiceTest {

    @Mock
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Mock
    private ReactiveValueOperations<String, String> reactiveValueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(reactiveStringRedisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
    }

    @Test
    void setValue_savesValueInRedis() {
        when(reactiveValueOperations.set(anyString(), anyString())).thenReturn(Mono.just(true));

        redisService.setValue("externalId", "entityType", "value");

        verify(reactiveValueOperations, times(1)).set(anyString(), anyString());
    }

    @Test
    void setValue_handlesException() {
        when(reactiveValueOperations.set(anyString(), anyString())).thenThrow(new RuntimeException("Redis error"));

        redisService.setValue("externalId", "entityType", "value");

        verify(reactiveValueOperations, times(1)).set(anyString(), anyString());
    }

    @Test
    void getValue_returnsValueFromRedis() {
        when(reactiveValueOperations.get(anyString())).thenReturn(Mono.just("value"));

        Mono<String> result = redisService.getValue("externalId", "entityType");

        StepVerifier.create(result)
                .expectNext("value")
                .verifyComplete();
    }

    @Test
    void getValue_handlesException() {
        when(reactiveValueOperations.get(anyString())).thenThrow(new RuntimeException("Redis error"));

        Mono<String> result = redisService.getValue("externalId", "entityType");

        StepVerifier.create(result)
                .verifyComplete();
    }
}