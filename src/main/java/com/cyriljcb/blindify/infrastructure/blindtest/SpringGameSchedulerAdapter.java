package com.cyriljcb.blindify.infrastructure.blindtest;

import java.time.Instant;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.cyriljcb.blindify.domain.blindtest.port.GameSchedulerPort;

@Component
public class SpringGameSchedulerAdapter implements GameSchedulerPort {

    private final TaskScheduler taskScheduler;

    public SpringGameSchedulerAdapter(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void schedule(double delaySeconds, Runnable task) {
        long delayMillis = (long) (delaySeconds * 1000);
        
        taskScheduler.schedule(
            task,
            Instant.now().plusMillis(delayMillis)
        );
    }
}