package com.insider.assignment.infrastructure.adapter.persistence.entity;

import com.insider.assignment.domain.model.Message;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String externalMessageId;
    private String messageContent;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Message.Status status;
}
