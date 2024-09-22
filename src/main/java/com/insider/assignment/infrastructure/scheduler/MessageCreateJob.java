package com.insider.assignment.infrastructure.scheduler;

import com.insider.assignment.application.service.MessageService;
import com.insider.assignment.domain.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageCreateJob implements Job {

    private final MessageService messageService;

    public MessageCreateJob(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        List<Message> messagesToProcess = messageService.findMessagesToProcess();
        if(messagesToProcess.isEmpty()) {
            log.info("No messages to process, creating new ones.");
            messageService.createNewMessages();
        }

    }
}
