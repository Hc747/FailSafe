# failsafe
Dynamic and concurrent retry constructs for the JVM.

```Java
public static void main(String[] args) {
  final Suspensor suspensor = Suspensor.blocking().intercept(millis -> System.out.println("Sleeping for: " + Duration.ofMillis(millis)));
  final EasingFunction delay = fixed(100).plus(linear(150)).plus(random(50, 250)).limit(5000);

  final AtomicInteger count = new AtomicInteger(0);
  final Consumer < Throwable > log = (e) -> System.out.println("[" + Instant.now() + " - " + count.incrementAndGet() + "] Received: " + e);
  final RetryCondition < Throwable > logger = RetryCondition.intercept(log);
  final RetryCondition < Throwable > backoff = RetryCondition.backoff(delay, suspensor);

  final RetryCondition < Throwable > rejection = logger.and(backoff);

  final CallableTask < ? > task = () -> {
    Thread.sleep(500); // simulate expensive task
    if (ThreadLocalRandom.current().nextBoolean()) {
      throw new IllegalStateException("Not OK"); // ... that may fail
    }
    return "OK";
  };

  final ExecutionResult < ? , ? > execution;
  if (ThreadLocalRandom.current().nextBoolean()) {
    execution = Retry.defer(task, rejection); // asynchronous (non-blocking)
  } else {
    execution = Retry.sync(task, rejection); // synchronous (blocking)
  }

  if (ThreadLocalRandom.current().nextBoolean()) { // attempt to cancel the execution of the task
    execution.cancel(true);
  }

  System.out.println("Handle ==> " + execution);
  System.out.println("Result ==> " + execution.unwrap());
}
```
