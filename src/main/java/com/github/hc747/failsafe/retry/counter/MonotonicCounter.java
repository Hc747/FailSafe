package com.github.hc747.failsafe.retry.counter;

import java.util.function.Function;

@FunctionalInterface
public interface MonotonicCounter {

    //TODO: document that this isn't shareable

    int next();

    static MonotonicCounter counter() {
        return counter(true);
    }

    static MonotonicCounter counter(boolean atomic) {
        return counter(0, atomic);
    }

    static MonotonicCounter counter(int initial, boolean atomic) {
        final Function<Integer, MonotonicCounter> ctor = atomic ? AtomicMonotonicCounter::new : UnsafeMonotonicCounter::new;
        return ctor.apply(initial);
    }
}
