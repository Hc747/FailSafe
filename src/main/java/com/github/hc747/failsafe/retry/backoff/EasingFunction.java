package com.github.hc747.failsafe.retry.backoff;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@FunctionalInterface
public interface EasingFunction {

    long ease(int attempts);

    default EasingFunction plus(EasingFunction function) {
        return (attempts) -> ease(attempts) + function.ease(attempts);
    }

    default EasingFunction minus(EasingFunction function) {
        return (attempts) -> ease(attempts) - function.ease(attempts);
    }

    default EasingFunction limit(long limit) {
        return (attempts) -> Math.min(limit, ease(attempts));
    }

    static EasingFunction fixed(long delay) {
        return (attempts) -> delay;
    }

    static EasingFunction linear(long multiplier) {
        return (attempts) -> attempts * multiplier;
    }

    static EasingFunction exponential() {
        return (attempts) -> Math.round(Math.pow(attempts, 2));
    }

    static EasingFunction random(long min, long max) {
        return (attempts) -> (long) (ThreadLocalRandom.current().nextDouble() * (max - min)) + min;
    }

    static EasingFunction random(Random random, long min, long max) {
        Objects.requireNonNull(random, "random");
        return (attempts) -> (long) (random.nextDouble() * (max - min)) + min;
    }
}
