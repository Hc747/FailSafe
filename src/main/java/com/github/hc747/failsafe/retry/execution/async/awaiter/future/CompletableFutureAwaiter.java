package com.github.hc747.failsafe.retry.execution.async.awaiter.future;

import com.github.hc747.failsafe.retry.execution.ExecutionResult;
import com.github.hc747.failsafe.retry.execution.FailedExecution;
import com.github.hc747.failsafe.retry.execution.async.awaiter.ExecutionAwaiter;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

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

    @Override
    public String toString() {
        return execution.toString();
    }

    private static class BusyAwaiter<R> extends CompletableFutureAwaiter<R> {

        private static final String EXCEPTION_ALREADY_RESOLVED = "Execution has already completed.";
        private static final String EXCEPTION_INVALID_STATE = "Task completed without a result or exception.";

        BusyAwaiter(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
            super(execution);
        }

        private final AtomicReference<ExecutionResult<R, Throwable>> output = new AtomicReference<>(null);

        private void set(ExecutionResult<R, Throwable> result) {
            if (!output.compareAndSet(null, result)) {
                throw new IllegalStateException(EXCEPTION_ALREADY_RESOLVED);
            }
        }

        @Override
        protected void attach(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
            execution.whenComplete((resolved, rejected) -> {
                if (resolved != null) {
                    set(resolved);
                } else if (rejected != null) {
                    set(ExecutionResult.rejected(rejected));
                } else {
                    throw new IllegalStateException(EXCEPTION_INVALID_STATE);
                }
            });
        }

        @Override
        public ExecutionResult<R, Throwable> await() {
            for (;;) {
                final ExecutionResult<R, Throwable> result = output.get();
                if (result != null) {
                    return result;
                }
                //TODO: spin waiting
            }
        }
    }

    private static class BlockingAwaiter<R> extends CompletableFutureAwaiter<R> {

        BlockingAwaiter(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
            super(execution);
        }

        @Override
        protected void attach(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
            execution.exceptionally(FailedExecution::new);
        }

        @Override
        public ExecutionResult<R, Throwable> await() {
            try {
                return execution.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new IllegalStateException("Unreachable", e);
            }
        }
    }
}
