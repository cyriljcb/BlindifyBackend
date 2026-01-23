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
    public void schedule(int delaySeconds, Runnable task) {
        taskScheduler.schedule(
            task,
            Instant.now().plusSeconds(delaySeconds)
        );
    }
}
