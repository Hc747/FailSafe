package com.github.hc747.failsafe.retry.suspend;

import java.util.function.Consumer;

@FunctionalInterface
public
interface Suspensor {

    void suspend(long millis);

    default Suspensor intercept(Consumer<Long> consumer) {
        return (millis) -> {
            consumer.accept(millis);
            suspend(millis);
        };
    }

    static Suspensor blocking() {
        return BlockingSuspensor.getInstance();
    }
}
