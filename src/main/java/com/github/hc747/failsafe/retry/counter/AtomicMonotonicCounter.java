package com.github.hc747.failsafe.retry.counter;

import java.util.concurrent.atomic.AtomicInteger;

public final class AtomicMonotonicCounter implements MonotonicCounter {

    private final AtomicInteger counter;

    AtomicMonotonicCounter(int initial) {
        this.counter = new AtomicInteger(initial);
    }

    @Override
    public int next() {
        return counter.incrementAndGet();
    }
}
