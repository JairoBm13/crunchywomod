// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.util.concurrent.TimeUnit;

public final class Stopwatch
{
    private long elapsedNanos;
    private boolean isRunning;
    private long startTick;
    private final Ticker ticker;
    
    public Stopwatch() {
        this(Ticker.systemTicker());
    }
    
    public Stopwatch(final Ticker ticker) {
        this.ticker = Preconditions.checkNotNull(ticker);
    }
    
    private static String abbreviate(final TimeUnit timeUnit) {
        switch (timeUnit) {
            default: {
                throw new AssertionError();
            }
            case NANOSECONDS: {
                return "ns";
            }
            case MICROSECONDS: {
                return "\u03bcs";
            }
            case MILLISECONDS: {
                return "ms";
            }
            case SECONDS: {
                return "s";
            }
        }
    }
    
    private static TimeUnit chooseUnit(final long n) {
        if (TimeUnit.SECONDS.convert(n, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.SECONDS;
        }
        if (TimeUnit.MILLISECONDS.convert(n, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MILLISECONDS;
        }
        if (TimeUnit.MICROSECONDS.convert(n, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MICROSECONDS;
        }
        return TimeUnit.NANOSECONDS;
    }
    
    private long elapsedNanos() {
        if (this.isRunning) {
            return this.ticker.read() - this.startTick + this.elapsedNanos;
        }
        return this.elapsedNanos;
    }
    
    public long elapsedTime(final TimeUnit timeUnit) {
        return timeUnit.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
    }
    
    public Stopwatch start() {
        Preconditions.checkState(!this.isRunning);
        this.isRunning = true;
        this.startTick = this.ticker.read();
        return this;
    }
    
    @Override
    public String toString() {
        return this.toString(4);
    }
    
    @Deprecated
    public String toString(final int n) {
        final long elapsedNanos = this.elapsedNanos();
        final TimeUnit chooseUnit = chooseUnit(elapsedNanos);
        return String.format("%." + n + "g %s", elapsedNanos / TimeUnit.NANOSECONDS.convert(1L, chooseUnit), abbreviate(chooseUnit));
    }
}
