package com.cyriljcb.blindify.domain.blindtest.port;

public interface GameSchedulerPort {
    void schedule(double delayMiliSeconds, Runnable task);
}

