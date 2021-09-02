package com.github.hc747.failsafe.retry.backoff;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@FunctionalInterface
public interface EasingFunction {

    long ease(int attempts);

    default EasingFunction plus(EasingFunction function) {
        Objects.requireNonNull(function, "function");
        return (attempts) -> ease(attempts) + function.ease(attempts);
    }

    default EasingFunction minus(EasingFunction function) {
        Objects.requireNonNull(function, "function");
        return (attempts) -> ease(attempts) - function.ease(attempts);
    }

    default EasingFunction limit(long duration, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return limit(unit.toMillis(duration));
    }

    default EasingFunction limit(long limit) {
        return (attempts) -> Math.min(limit, ease(attempts));
    }

    static EasingFunction fixed(long duration, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return fixed(unit.toMillis(duration));
    }

    static EasingFunction fixed(long delay) {
        return (attempts) -> delay;
    }

    static EasingFunction linear(long duration, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return linear(unit.toMillis(duration));
    }

    static EasingFunction linear(long multiplier) {
        return (attempts) -> attempts * multiplier;
    }

    static EasingFunction exponential() {
        return (attempts) -> Math.round(Math.pow(attempts, 2));
    }

    static EasingFunction random(long min, long max, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return random(unit.toMillis(min), unit.toMillis(max));
    }

    static EasingFunction random(long min, long max) {
        return (attempts) -> (long) (ThreadLocalRandom.current().nextDouble() * (max - min)) + min;
    }

    static EasingFunction random(Random random, long min, long max, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return random(random, unit.toMillis(min), unit.toMillis(max));
    }

    static EasingFunction random(Random random, long min, long max) {
        Objects.requireNonNull(random, "random");
        return (attempts) -> (long) (random.nextDouble() * (max - min)) + min;
    }
}
