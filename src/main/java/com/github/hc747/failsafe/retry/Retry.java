package com.github.hc747.failsafe.retry;

import com.github.hc747.failsafe.retry.condition.RetryCondition;
import com.github.hc747.failsafe.retry.execution.ExecutionResult;
import com.github.hc747.failsafe.retry.execution.task.CallableTask;
import com.github.hc747.failsafe.retry.execution.task.RunnableTask;
import com.github.hc747.failsafe.retry.execution.task.Task;

import java.util.Objects;
import java.util.concurrent.*;

public final class Retry {

    /** Synchronous implementations */
    public static ExecutionResult<Void, Throwable> sync(RunnableTask task) {
        return synchronous(task, null, null);
    }

    public static ExecutionResult<Void, Throwable> sync(RunnableTask task, RetryCondition<? super Throwable> rejected) {
        return synchronous(task, rejected, null);
    }

    public static ExecutionResult<Void, Throwable> sync(RunnableTask task, RetryCondition<? super Throwable> rejected, RetryCondition<? super Void> resolved) {
        return synchronous(task, rejected, resolved);
    }

    public static <R> ExecutionResult<R, Throwable> sync(CallableTask<R> task) {
        return synchronous(task, null, null);
    }

    public static <R> ExecutionResult<R, Throwable> sync(CallableTask<R> task, RetryCondition<? super Throwable> rejected) {
        return synchronous(task, rejected, null);
    }

    public static <R> ExecutionResult<R, Throwable> sync(CallableTask<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return synchronous(task, rejected, resolved);
    }

    private static <R> ExecutionResult<R, Throwable> synchronous(Task<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return execute(task, rejected, resolved);
    }

    /** Asynchronous implementations */
    public static CompletableFuture<ExecutionResult<Void, Throwable>> async(ExecutorService executor, RunnableTask task) {
        return asynchronous(executor, task, null, null);
    }

    public static CompletableFuture<ExecutionResult<Void, Throwable>> async(ExecutorService executor, RunnableTask task, RetryCondition<? super Throwable> rejected) {
        return asynchronous(executor, task, rejected, null);
    }

    public static CompletableFuture<ExecutionResult<Void, Throwable>> async(ExecutorService executor, RunnableTask task, RetryCondition<? super Throwable> rejected, RetryCondition<? super Void> resolved) {
        return asynchronous(executor, task, rejected, resolved);
    }

    public static <R> CompletableFuture<ExecutionResult<R, Throwable>> async(ExecutorService executor, CallableTask<R> task) {
        return asynchronous(executor, task, null, null);
    }

    public static <R> CompletableFuture<ExecutionResult<R, Throwable>> async(ExecutorService executor, CallableTask<R> task, RetryCondition<? super Throwable> rejected) {
        return asynchronous(executor, task, rejected, null);
    }

    public static <R> CompletableFuture<ExecutionResult<R, Throwable>> async(ExecutorService executor, CallableTask<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return asynchronous(executor, task, rejected, resolved);
    }

    public static CompletableFuture<ExecutionResult<Void, Throwable>> async(RunnableTask task) {
        return asynchronous(null, task, null, null);
    }

    public static CompletableFuture<ExecutionResult<Void, Throwable>> async(RunnableTask task, RetryCondition<? super Throwable> rejected) {
        return asynchronous(null, task, rejected, null);
    }

    public static CompletableFuture<ExecutionResult<Void, Throwable>> async(RunnableTask task, RetryCondition<? super Throwable> rejected, RetryCondition<? super Void> resolved) {
        return asynchronous(null, task, rejected, resolved);
    }

    public static <R> CompletableFuture<ExecutionResult<R, Throwable>> async(CallableTask<R> task) {
        return asynchronous(null, task, null, null);
    }

    public static <R> CompletableFuture<ExecutionResult<R, Throwable>> async(CallableTask<R> task, RetryCondition<? super Throwable> rejected) {
        return asynchronous(null, task, rejected, null);
    }

    public static <R> CompletableFuture<ExecutionResult<R, Throwable>> async(CallableTask<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return asynchronous(null, task, rejected, resolved);
    }

    private static <R> CompletableFuture<ExecutionResult<R, Throwable>> asynchronous(ExecutorService executor, Task<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        if (executor == null) {
            return CompletableFuture.supplyAsync(() -> synchronous(task, rejected, resolved));
        }
        return CompletableFuture.supplyAsync(() -> synchronous(task, rejected, resolved), executor);
    }

    /** Deferred implementations */
    public static ExecutionResult<Void, Throwable> defer(ExecutorService executor, RunnableTask task) {
        return deferred(executor, task, null, null);
    }

    public static ExecutionResult<Void, Throwable> defer(ExecutorService executor, RunnableTask task, RetryCondition<? super Throwable> rejected) {
        return deferred(executor, task, rejected, null);
    }

    public static ExecutionResult<Void, Throwable> defer(ExecutorService executor, RunnableTask task, RetryCondition<? super Throwable> rejected, RetryCondition<? super Void> resolved) {
        return deferred(executor, task, rejected, resolved);
    }

    public static <R> ExecutionResult<R, Throwable> defer(ExecutorService executor, CallableTask<R> task) {
        return deferred(executor, task, null, null);
    }

    public static <R> ExecutionResult<R, Throwable> defer(ExecutorService executor, CallableTask<R> task, RetryCondition<? super Throwable> rejected) {
        return deferred(executor, task, rejected, null);
    }

    public static <R> ExecutionResult<R, Throwable> defer(ExecutorService executor, CallableTask<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return deferred(executor, task, rejected, resolved);
    }

    public static ExecutionResult<Void, Throwable> defer(RunnableTask task) {
        return deferred(null, task, null, null);
    }

    public static ExecutionResult<Void, Throwable> defer(RunnableTask task, RetryCondition<? super Throwable> rejected) {
        return deferred(null, task, rejected, null);
    }

    public static ExecutionResult<Void, Throwable> defer(RunnableTask task, RetryCondition<? super Throwable> rejected, RetryCondition<? super Void> resolved) {
        return deferred(null, task, rejected, resolved);
    }

    public static <R> ExecutionResult<R, Throwable> defer(CallableTask<R> task) {
        return deferred(null, task, null, null);
    }

    public static <R> ExecutionResult<R, Throwable> defer(CallableTask<R> task, RetryCondition<? super Throwable> rejected) {
        return deferred(null, task, rejected, null);
    }

    public static <R> ExecutionResult<R, Throwable> defer(CallableTask<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return deferred(null, task, rejected, resolved);
    }

    private static <R> ExecutionResult<R, Throwable> deferred(ExecutorService executor, Task<R> task, RetryCondition<? super Throwable> rejected, RetryCondition<? super R> resolved) {
        return ExecutionResult.deferred(asynchronous(executor, task, rejected, resolved));
    }

    private static <T> ExecutionResult<T, Throwable> execute(final Task<T> action, final RetryCondition<? super Throwable> rejected, final RetryCondition<? super T> resolved) {
        Objects.requireNonNull(action, "action");
        for (;;) {
            try {
                final T result = action.execute();
                if (acceptable(resolved, result)) {
                    return ExecutionResult.resolved(result);
                }
            } catch (Throwable cause) {
                if (rejectable(rejected, cause)) {
                    return ExecutionResult.rejected(cause);
                }
            }
        }
    }

    private static <R> boolean acceptable(RetryCondition<? super R> condition, R result) {
        if (condition == null) {
            return true;
        }
        return !condition.retryable(result);
    }

    private static <T extends Throwable> boolean rejectable(RetryCondition<? super T> condition, T cause) {
        Objects.requireNonNull(cause, "cause");
        if (condition == null) {
            return true;
        }
        return !condition.retryable(cause);
    }
}