package com.github.hc747.failsafe.retry.execution.async.awaiter.future;

import com.github.hc747.failsafe.retry.execution.ExecutionResult;
import com.github.hc747.failsafe.retry.execution.FailedExecution;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class BlockingAwaiter<R> extends CompletableFutureAwaiter<R> {

    public BlockingAwaiter(CompletableFuture<ExecutionResult<R, Throwable>> execution) {
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
