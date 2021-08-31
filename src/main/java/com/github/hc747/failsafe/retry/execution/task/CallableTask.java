package com.github.hc747.failsafe.retry.execution.task;

import java.util.Objects;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface CallableTask<V> extends Task<V> {

    V call() throws Throwable;

    @Override
    default V execute() throws Throwable {
        return call();
    }

    static <T> Task<T> wrap(Callable<T> callable) {
        Objects.requireNonNull(callable, "callable");
        return callable::call;
    }
}
