// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.concurrent.TimeUnit;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

class CrashlyticsExecutorServiceWrapper
{
    private final ExecutorService executorService;
    
    public CrashlyticsExecutorServiceWrapper(final ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    Future<?> executeAsync(final Runnable runnable) {
        try {
            return this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    }
                    catch (Exception ex) {
                        Fabric.getLogger().e("CrashlyticsCore", "Failed to execute task.", ex);
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {
            Fabric.getLogger().d("CrashlyticsCore", "Executor is shut down because we're handling a fatal crash.");
            return null;
        }
    }
    
     <T> Future<T> executeAsync(final Callable<T> callable) {
        try {
            return this.executorService.submit((Callable<T>)new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        return callable.call();
                    }
                    catch (Exception ex) {
                        Fabric.getLogger().e("CrashlyticsCore", "Failed to execute task.", ex);
                        return null;
                    }
                }
            });
        }
        catch (RejectedExecutionException ex) {
            Fabric.getLogger().d("CrashlyticsCore", "Executor is shut down because we're handling a fatal crash.");
            return null;
        }
    }
    
     <T> T executeSyncLoggingException(final Callable<T> callable) {
        try {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                return this.executorService.submit(callable).get(4L, TimeUnit.SECONDS);
            }
            return this.executorService.submit(callable).get();
        }
        catch (RejectedExecutionException ex2) {
            Fabric.getLogger().d("CrashlyticsCore", "Executor is shut down because we're handling a fatal crash.");
            return null;
        }
        catch (Exception ex) {
            Fabric.getLogger().e("CrashlyticsCore", "Failed to execute task.", ex);
            return null;
        }
    }
}
