package com.insider.assignment.infrastructure.adapter.persistence.repository;

import com.insider.assignment.application.outbound.MessageRepository;
import com.insider.assignment.domain.model.Message;
import com.insider.assignment.infrastructure.adapter.persistence.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class JpaMessageRepository implements MessageRepository {

    private final SpringDataMessageRepository repository;


    @Override
    public List<Message> findSentMessages() {
        return repository.findAllByStatus(
                Message.Status.SENT).stream().map(messageEntity -> Message.builder()
                        .id(messageEntity.getId())
                        .messageContent(messageEntity.getMessageContent())
                        .phoneNumber(messageEntity.getPhoneNumber())
                        .status(messageEntity.getStatus())
                        .externalMessageId(messageEntity.getExternalMessageId())
                        .build())
                .toList();
    }

    @Override
    public List<Message> findMessagesToProcess() {
        log.info("Finding messages to process");
        Pageable topTwo = PageRequest.of(0,2);
        return repository.findAllByStatusOrderByIdAsc(
                Message.Status.NOT_SENT,
                topTwo
        ).stream().map(messageEntity -> Message.builder()
                .id(messageEntity.getId())
                .messageContent(messageEntity.getMessageContent())
                .phoneNumber(messageEntity.getPhoneNumber())
                .status(messageEntity.getStatus())
                .build()).toList();
    }

    @Override
    public Message save(Message message) {
        log.info("Saving message: {}", message);
        MessageEntity messageEntity = MessageEntity.builder()
                .messageContent(message.getMessageContent())
                .phoneNumber(message.getPhoneNumber())
                .status(message.getStatus())
                .build();

        MessageEntity savedEntity = repository.save(messageEntity);

        log.info("Message saved with id: {}", savedEntity.getId());

        return Message.builder()
                .id(messageEntity.getId())
                .messageContent(savedEntity.getMessageContent())
                .phoneNumber(savedEntity.getPhoneNumber())
                .status(savedEntity.getStatus())
                .build();
    }

    @Override
    public void saveAll(List<Message> messages) {
        repository.saveAll(
                messages.stream().map(message -> MessageEntity.builder()
                        .messageContent(message.getMessageContent())
                        .phoneNumber(message.getPhoneNumber())
                        .status(message.getStatus())
                        .build()).toList()
        );
    }

    @Override
    public Message findById(Long id) {
        log.info("Finding message by id: {}", id);
        return repository.findById(id)
            .map(
                messageEntity -> Message.builder()
                        .id(messageEntity.getId())
                        .messageContent(messageEntity.getMessageContent())
                        .phoneNumber(messageEntity.getPhoneNumber())
                        .status(messageEntity.getStatus())
                    .build()
                ).orElse(null);
    }

    @Override
    public void setStatusById(Message.Status status, Long id) {
        repository.setStatusById(status, id);
    }

    @Override
    public void setExternalMessageIdById(String externalMessageId, Long id) {
        repository.setExternalMessageIdById(externalMessageId, id);
    }
}
