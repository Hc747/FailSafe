package com.github.hc747.failsafe.retry.execution.task;

import java.util.Objects;

@FunctionalInterface
public interface RunnableTask extends Task<Void> {

    void run() throws Throwable;

    @Override
    default Void execute() throws Throwable {
        run();
        return null;
    }

    static Task<Void> wrap(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        return () -> {
            runnable.run();
            return null;
        };
    }
}
