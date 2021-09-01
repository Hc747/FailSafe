package com.github.hc747.failsafe.retry.execution.async;

import com.github.hc747.failsafe.retry.execution.ExecutionResult;
import com.github.hc747.failsafe.retry.execution.async.awaiter.ExecutionAwaiter;

import java.util.Objects;

public class DeferredExecution<R> implements ExecutionResult<R, Throwable> {

    private final ExecutionAwaiter<R> awaiter;

    public DeferredExecution(ExecutionAwaiter<R> awaiter) {
        this.awaiter = Objects.requireNonNull(awaiter, "awaiter");
    }

    @Override
    public R result() {
        return await().result();
    }

    @Override
    public Throwable cause() {
        return await().cause();
    }

    @Override
    public boolean succeeded() {
        return await().succeeded();
    }

    @Override
    public boolean async() {
        return true;
    }

    @Override
    public boolean cancel(boolean interrupt) {
        return awaiter.cancel(interrupt);
    }

    private ExecutionResult<R, Throwable> await() {
        return awaiter.await();
    }

    @Override
    public String toString() {
        return super.toString() + "[async: " + async() + "]" + "[deferred: " + awaiter +"]";
    }
}
