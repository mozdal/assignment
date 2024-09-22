package com.insider.assignment.infrastructure.adapter.persistence.repository;

import com.insider.assignment.domain.model.Message;
import com.insider.assignment.infrastructure.adapter.persistence.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpringDataMessageRepository extends CrudRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByStatus(Message.Status status);
    List<MessageEntity> findAllByStatusOrderByIdAsc(Message.Status status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE message m SET m.status = :status WHERE m.id = :id")
    void setStatusById(Message.Status status, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE message m SET m.externalMessageId = :externalMessageId WHERE m.id = :id")
    void setExternalMessageIdById(String externalMessageId, Long id);
}
