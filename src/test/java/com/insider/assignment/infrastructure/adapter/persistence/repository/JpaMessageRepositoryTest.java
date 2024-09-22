package com.insider.assignment.infrastructure.adapter.persistence.repository;

import com.insider.assignment.domain.model.Message;
import com.insider.assignment.infrastructure.adapter.persistence.entity.MessageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JpaMessageRepositoryTest {

    @Mock
    private SpringDataMessageRepository repository;

    @InjectMocks
    private JpaMessageRepository jpaMessageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findSentMessages_returnsSentMessages() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setStatus(Message.Status.SENT);
        when(repository.findAllByStatus(Message.Status.SENT)).thenReturn(List.of(messageEntity));

        List<Message> result = jpaMessageRepository.findSentMessages();

        assertEquals(1, result.size());
        assertEquals(Message.Status.SENT, result.get(0).getStatus());
    }

    @Test
    void findSentMessages_returnsEmptyListWhenNoMessages() {
        when(repository.findAllByStatus(Message.Status.SENT)).thenReturn(Collections.emptyList());

        List<Message> result = jpaMessageRepository.findSentMessages();

        assertEquals(0, result.size());
    }

    @Test
    void findMessagesToProcess_returnsMessagesToProcess() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setStatus(Message.Status.NOT_SENT);
        Pageable topTwo = PageRequest.of(0, 2);
        when(repository.findAllByStatusOrderByIdAsc(Message.Status.NOT_SENT, topTwo)).thenReturn(List.of(messageEntity));

        List<Message> result = jpaMessageRepository.findMessagesToProcess();

        assertEquals(1, result.size());
        assertEquals(Message.Status.NOT_SENT, result.get(0).getStatus());
    }

    @Test
    void save_savesMessage() {
        Message message = Message.builder().messageContent("content").phoneNumber("1234567890").status(Message.Status.NOT_SENT).build();
        MessageEntity messageEntity = MessageEntity.builder().messageContent("content").phoneNumber("1234567890").status(Message.Status.NOT_SENT).build();
        when(repository.save(any(MessageEntity.class))).thenReturn(messageEntity);

        Message result = jpaMessageRepository.save(message);

        assertEquals("content", result.getMessageContent());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals(Message.Status.NOT_SENT, result.getStatus());
    }

    @Test
    void findById_returnsMessageWhenFound() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(messageEntity));

        Message result = jpaMessageRepository.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findById_returnsNullWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Message result = jpaMessageRepository.findById(1L);

        assertNull(result);
    }

    @Test
    void setStatusById_updatesStatus() {
        doNothing().when(repository).setStatusById(Message.Status.SENT, 1L);

        jpaMessageRepository.setStatusById(Message.Status.SENT, 1L);

        verify(repository).setStatusById(Message.Status.SENT, 1L);
    }

    @Test
    void setExternalMessageIdById_updatesExternalMessageId() {
        doNothing().when(repository).setExternalMessageIdById("externalId", 1L);

        jpaMessageRepository.setExternalMessageIdById("externalId", 1L);

        verify(repository).setExternalMessageIdById("externalId", 1L);
    }
}