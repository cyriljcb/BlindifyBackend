package com.cyriljcb.blindify.domain.blindtest.port;

public interface GameSchedulerPort {
    void schedule(int delaySeconds, Runnable task);
}

