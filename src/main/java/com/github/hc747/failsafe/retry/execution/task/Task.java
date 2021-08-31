package com.github.hc747.failsafe.retry.execution.task;

@FunctionalInterface
public
interface Task<R> {

    R execute() throws Throwable;
}
