// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.Locale;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;

public final class ExecutorUtils
{
    private static final void addDelayedShutdownHook(final String s, final ExecutorService executorService) {
        addDelayedShutdownHook(s, executorService, 2L, TimeUnit.SECONDS);
    }
    
    public static final void addDelayedShutdownHook(final String s, final ExecutorService executorService, final long n, final TimeUnit timeUnit) {
        Runtime.getRuntime().addShutdownHook(new Thread(new BackgroundPriorityRunnable() {
            public void onRun() {
                try {
                    Fabric.getLogger().d("Fabric", "Executing shutdown hook for " + s);
                    executorService.shutdown();
                    if (!executorService.awaitTermination(n, timeUnit)) {
                        Fabric.getLogger().d("Fabric", s + " did not shut down in the" + " allocated time. Requesting immediate shutdown.");
                        executorService.shutdownNow();
                    }
                }
                catch (InterruptedException ex) {
                    Fabric.getLogger().d("Fabric", String.format(Locale.US, "Interrupted while waiting for %s to shut down. Requesting immediate shutdown.", s));
                    executorService.shutdownNow();
                }
            }
        }, "Crashlytics Shutdown Hook for " + s));
    }
    
    public static ExecutorService buildSingleThreadExecutorService(final String s) {
        final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(getNamedThreadFactory(s));
        addDelayedShutdownHook(s, singleThreadExecutor);
        return singleThreadExecutor;
    }
    
    public static ScheduledExecutorService buildSingleThreadScheduledExecutorService(final String s) {
        final ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor(getNamedThreadFactory(s));
        addDelayedShutdownHook(s, singleThreadScheduledExecutor);
        return singleThreadScheduledExecutor;
    }
    
    public static final ThreadFactory getNamedThreadFactory(final String s) {
        return new ThreadFactory() {
            final /* synthetic */ AtomicLong val$count = new AtomicLong(1L);
            
            @Override
            public Thread newThread(final Runnable runnable) {
                final Thread thread = Executors.defaultThreadFactory().newThread(new BackgroundPriorityRunnable() {
                    public void onRun() {
                        runnable.run();
                    }
                });
                thread.setName(s + this.val$count.getAndIncrement());
                return thread;
            }
        };
    }
}
