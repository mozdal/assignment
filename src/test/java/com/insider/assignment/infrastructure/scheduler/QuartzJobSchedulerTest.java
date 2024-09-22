package com.insider.assignment.infrastructure.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class QuartzJobSchedulerTest {

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private QuartzJobScheduler quartzJobScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void scheduleOrderJob_schedulesJobsSuccessfully() throws Exception {
        quartzJobScheduler.scheduleOrderJob();

        verify(scheduler, times(2)).scheduleJob(any(), any());
    }

    @Test
    void startMessageJob_startsSchedulerWhenInStandbyMode() throws SchedulerException {
        when(scheduler.isInStandbyMode()).thenReturn(true);

        quartzJobScheduler.startMessageJob();

        verify(scheduler).start();
    }

    @Test
    void startMessageJob_throwsExceptionWhenSchedulerAlreadyStarted() throws SchedulerException {
        when(scheduler.isInStandbyMode()).thenReturn(false);

        assertThrows(SchedulerException.class, () -> quartzJobScheduler.startMessageJob());
    }

    @Test
    void stopMessageJob_stopsSchedulerWhenNotInStandbyMode() throws SchedulerException {
        when(scheduler.isInStandbyMode()).thenReturn(false);

        quartzJobScheduler.stopMessageJob();

        verify(scheduler).standby();
    }

    @Test
    void stopMessageJob_throwsExceptionWhenSchedulerAlreadyStopped() throws SchedulerException {
        when(scheduler.isInStandbyMode()).thenReturn(true);

        assertThrows(SchedulerException.class, () -> quartzJobScheduler.stopMessageJob());
    }
}