package com.github.hc747.failsafe.retry.execution;

import java.util.Objects;

public class FailedExecution<R, T extends Throwable> implements ExecutionResult<R, T> {

    private final T cause;

    public FailedExecution(T cause) {
        this.cause = Objects.requireNonNull(cause, "cause");
    }

    @Override
    public R result() {
        return null;
    }

    @Override
    public T cause() {
        return cause;
    }

    @Override
    public boolean succeeded() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + "[async: " + async() + "]" + "[failed: " + cause +"]";
    }
}
