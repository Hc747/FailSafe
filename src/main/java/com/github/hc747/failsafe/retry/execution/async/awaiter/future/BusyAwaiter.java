package com.github.hc747.failsafe.retry.execution.async.awaiter.future;

import com.github.hc747.failsafe.retry.execution.ExecutionResult;
import com.github.hc747.failsafe.retry.execution.FailedExecution;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

class BusyAwaiter<R> extends CompletableFutureAwaiter<R> {

    private static final String EXCEPTION_ALREADY_RESOLVED = "Execution has already completed.";
    private static final String EXCEPTION_INVALID_STATE = "Task completed without a result or exception.";

    public BusyAwaiter(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
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
