package com.github.hc747.failsafe.retry.execution;

public class ResolvedExecution<R> implements ExecutionResult<R, Throwable> {

    private final R result;

    ResolvedExecution(R result) {
        this.result = result;
    }

    @Override
    public R result() {
        return result;
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public boolean succeeded() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "[async: " + async() + "]" + "[resolved: " + result +"]";
    }
}
