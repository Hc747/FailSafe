package com.github.hc747.failsafe.retry.suspend;

import com.github.hc747.failsafe.unsafe.Unsafe;

final class BlockingSuspensor implements Suspensor {

    private static class Singleton {
        private static final Suspensor INSTANCE = new BlockingSuspensor();
    }

    private BlockingSuspensor() {}

    @Override
    public void suspend(long millis) {
        if (millis <= 0L) return;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interrupt) {
            Unsafe.raise(interrupt);
        }
    }

    public static Suspensor getInstance() {
        return Singleton.INSTANCE;
    }
}
