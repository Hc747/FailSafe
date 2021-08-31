package com.github.hc747.failsafe.retry.condition;

import com.github.hc747.failsafe.retry.backoff.EasingFunction;
import com.github.hc747.failsafe.retry.counter.MonotonicCounter;
import com.github.hc747.failsafe.retry.suspend.Suspensor;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public
interface RetryCondition<T> {

    boolean retryable(T element);

    default RetryCondition<T> and(RetryCondition<? super T> other) {
        Objects.requireNonNull(other, "other");
        return (e) -> retryable(e) && other.retryable(e);
    }

    default RetryCondition<T> or(RetryCondition<? super T> other) {
        Objects.requireNonNull(other, "other");
        return (e) -> retryable(e) || other.retryable(e);
    }

    default RetryCondition<T> xor(RetryCondition<? super T> other) {
        Objects.requireNonNull(other, "other");
        return (e) -> retryable(e) != other.retryable(e);
    }

    default RetryCondition<T> negate() {
        return (e) -> !retryable(e);
    }
    
    static <V> RetryCondition<V> all(RetryCondition<V>... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        return (e) -> {
            for (RetryCondition<V> condition : conditions) {
                if (!condition.retryable(e)) {
                    return false;
                }
            }
            return true;
        };
    }

    static <V> RetryCondition<V> any(RetryCondition<V>... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        return (e) -> {
            for (RetryCondition<V> condition : conditions) {
                if (condition.retryable(e)) {
                    return true;
                }
            }
            return false;
        };
    }

    static <V> RetryCondition<V> none(RetryCondition<V>... conditions) {
        Objects.requireNonNull(conditions, "conditions");
        return (e) -> {
            for (RetryCondition<V> condition : conditions) {
                if (condition.retryable(e)) {
                    return false;
                }
            }
            return true;
        };
    }

    static <V> RetryCondition<V> never() {
        return (e) -> false;
    }

    static <V> RetryCondition<V> always() {
        return (e) -> true;
    }

    static <V> RetryCondition<V> intercept(Consumer<V> middleware) {
        Objects.requireNonNull(middleware, "middleware");
        return (e) -> {
            middleware.accept(e);
            return true;
        };
    }

    static <V> RetryCondition<V> attempts(int limit) {
        return attempts(limit, MonotonicCounter.counter());
    }

    static <V> RetryCondition<V> attempts(int limit, MonotonicCounter attempts) {
        Objects.requireNonNull(attempts, "attempts");
        return (e) -> attempts.next() <= limit;
    }

    static <V> RetryCondition<V> backoff(EasingFunction delay, Suspensor suspensor) {
        return backoff(delay, suspensor, MonotonicCounter.counter());
    }

    static <V> RetryCondition<V> backoff(EasingFunction delay, Suspensor suspensor, MonotonicCounter attempts) {
        Objects.requireNonNull(delay, "delay");
        Objects.requireNonNull(suspensor, "suspensor");
        Objects.requireNonNull(attempts, "attempts");
        return (e) -> {
            suspensor.suspend(delay.ease(attempts.next()));
            return true;
        };
    }
}
