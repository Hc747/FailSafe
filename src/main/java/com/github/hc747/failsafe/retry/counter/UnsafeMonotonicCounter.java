package com.github.hc747.failsafe.retry.counter;

public class UnsafeMonotonicCounter implements MonotonicCounter {

    private int counter;

    UnsafeMonotonicCounter(int initial) {
        this.counter = initial;
    }

    @Override
    public int next() {
        return ++counter;
    }
}
