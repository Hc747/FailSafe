package com.github.hc747.failsafe.retry.execution;

import com.github.hc747.failsafe.retry.execution.async.DeferredExecution;
import com.github.hc747.failsafe.retry.execution.async.awaiter.future.CompletableFutureAwaiter;
import com.github.hc747.failsafe.unsafe.Unsafe;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface ExecutionResult<R, T extends Throwable> {

    R result();

    T cause();

    boolean succeeded();

    default R unwrap() {
        if (succeeded()) {
            return result();
        }
        Unsafe.raise(cause());
        return null; // not reachable
    }

    default R unwrap_safely() throws T {
        return unwrap();
    }

    default boolean cancel(boolean interrupt) {
        return false;
    }

    default boolean async() {
        return false;
    }

    //TODO: extract factory methods
    static <X> ExecutionResult<X, Throwable> resolved(X result) {
        return new ResolvedExecution<>(result);
    }

    static <X, Y extends Throwable> ExecutionResult<X, Y> rejected(Y cause) {
        Objects.requireNonNull(cause, "cause");
        return new FailedExecution<>(cause);
    }

    static <X> ExecutionResult<X, Throwable> deferred(CompletableFuture<ExecutionResult<X, Throwable>> execution) {
        Objects.requireNonNull(execution, "execution");
        return new DeferredExecution<>(CompletableFutureAwaiter.blocking(execution));
    }
}
