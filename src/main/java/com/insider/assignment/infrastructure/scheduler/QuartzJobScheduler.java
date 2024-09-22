package com.insider.assignment.infrastructure.scheduler;

import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobScheduler {

    private final Scheduler scheduler;

    public QuartzJobScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void scheduleOrderJob() throws Exception {
        JobDetail messageJobDetail = JobBuilder.newJob(MessageJob.class)
                .withIdentity("messageJob", "messageGroup")
                .build();

        Trigger messageJobTrigger = TriggerBuilder.newTrigger()
                .withIdentity("messageTrigger", "messageGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 * * * ?"))
                .build();

        scheduler.scheduleJob(messageJobDetail, messageJobTrigger);

        JobDetail messageCreateJobDetail = JobBuilder.newJob(MessageCreateJob.class)
                .withIdentity("messageCreateJob", "messageCreateGroup")
                .build();

        Trigger messageCreatetrigger = TriggerBuilder.newTrigger()
                .withIdentity("messageCreateTrigger", "messageCreateGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                .build();

        scheduler.scheduleJob(messageCreateJobDetail, messageCreatetrigger);
    }

    public void startMessageJob() throws SchedulerException {
        if (scheduler.isInStandbyMode()) {
            scheduler.start();
            System.out.println("Scheduler started.");
        } else {
            throw new SchedulerException("Scheduler already started!");
        }
    }

    public void stopMessageJob() throws SchedulerException {
        if (!scheduler.isInStandbyMode()) {
            scheduler.standby();
            System.out.println("Scheduler stopped.");
        } else {
            throw new SchedulerException("Scheduler already stopped!");
        }
    }
}
