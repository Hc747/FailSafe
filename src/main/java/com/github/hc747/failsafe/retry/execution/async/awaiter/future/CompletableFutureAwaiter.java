package com.github.hc747.failsafe.retry.execution.async.awaiter.future;

import com.github.hc747.failsafe.retry.execution.ExecutionResult;
import com.github.hc747.failsafe.retry.execution.async.awaiter.ExecutionAwaiter;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class CompletableFutureAwaiter<R> implements ExecutionAwaiter<R> {

    protected final CompletableFuture<ExecutionResult<R, Throwable>> execution;

    public CompletableFutureAwaiter(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
        this.execution = Objects.requireNonNull(execution, "execution");
        attach(execution);
    }

    protected abstract void attach(CompletableFuture<ExecutionResult<R, Throwable>> execution);

    @Override
    public boolean cancel(boolean interrupt) {
        return execution.cancel(interrupt);
    }

    public static <T> CompletableFutureAwaiter<T> blocking(CompletableFuture<ExecutionResult<T, Throwable>> execution) {
        Objects.requireNonNull(execution, "execution");
        return new BlockingAwaiter<>(execution);
    }

    public static <T> CompletableFutureAwaiter<T> busy(CompletableFuture<ExecutionResult<T, Throwable>> execution) {
        Objects.requireNonNull(execution, "execution");
        return new BusyAwaiter<>(execution);
    }
}
