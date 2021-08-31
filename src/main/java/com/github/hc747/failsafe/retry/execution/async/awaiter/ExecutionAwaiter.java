package com.github.hc747.failsafe.retry.execution.async.awaiter;

import com.github.hc747.failsafe.retry.execution.ExecutionResult;

public interface ExecutionAwaiter<T> {

    ExecutionResult<T, Throwable> await();

    boolean cancel(boolean interrupt);

}
