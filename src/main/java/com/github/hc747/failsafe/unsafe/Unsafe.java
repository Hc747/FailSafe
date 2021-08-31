package com.github.hc747.failsafe.unsafe;

public final class Unsafe {

    private static final String ERROR_FORMAT = "Unable to instantiate: %s";

    private Unsafe() {
        throw new IllegalStateException(String.format(ERROR_FORMAT, Unsafe.class.getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void raise(Throwable exception) throws T {
        throw (T) exception;
    }
}
