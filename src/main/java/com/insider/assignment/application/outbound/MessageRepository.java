package com.insider.assignment.application.outbound;

import com.insider.assignment.domain.model.Message;

import java.util.List;

public interface MessageRepository {

    List<Message> findSentMessages();

    List<Message> findMessagesToProcess();
    Message save(Message message);

    void saveAll(List<Message> messages);

    Message findById(Long id);

    void setStatusById(Message.Status status, Long id);

    void setExternalMessageIdById(String externalMessageId, Long id);
}
