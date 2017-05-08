// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import java.io.File;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import java.util.concurrent.ExecutorService;
import java.util.WeakHashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;

class ImageLoaderEngine
{
    private final Map<Integer, String> cacheKeysForImageAwares;
    final ImageLoaderConfiguration configuration;
    private final AtomicBoolean networkDenied;
    private final Object pauseLock;
    private final AtomicBoolean paused;
    private final AtomicBoolean slowNetwork;
    private Executor taskDistributor;
    private Executor taskExecutor;
    private Executor taskExecutorForCachedImages;
    private final Map<String, ReentrantLock> uriLocks;
    
    ImageLoaderEngine(final ImageLoaderConfiguration configuration) {
        this.cacheKeysForImageAwares = Collections.synchronizedMap(new HashMap<Integer, String>());
        this.uriLocks = new WeakHashMap<String, ReentrantLock>();
        this.paused = new AtomicBoolean(false);
        this.networkDenied = new AtomicBoolean(false);
        this.slowNetwork = new AtomicBoolean(false);
        this.pauseLock = new Object();
        this.configuration = configuration;
        this.taskExecutor = configuration.taskExecutor;
        this.taskExecutorForCachedImages = configuration.taskExecutorForCachedImages;
        this.taskDistributor = DefaultConfigurationFactory.createTaskDistributor();
    }
    
    private Executor createTaskExecutor() {
        return DefaultConfigurationFactory.createExecutor(this.configuration.threadPoolSize, this.configuration.threadPriority, this.configuration.tasksProcessingType);
    }
    
    private void initExecutorsIfNeed() {
        if (!this.configuration.customExecutor && ((ExecutorService)this.taskExecutor).isShutdown()) {
            this.taskExecutor = this.createTaskExecutor();
        }
        if (!this.configuration.customExecutorForCachedImages && ((ExecutorService)this.taskExecutorForCachedImages).isShutdown()) {
            this.taskExecutorForCachedImages = this.createTaskExecutor();
        }
    }
    
    void cancelDisplayTaskFor(final ImageAware imageAware) {
        this.cacheKeysForImageAwares.remove(imageAware.getId());
    }
    
    void denyNetworkDownloads(final boolean b) {
        this.networkDenied.set(b);
    }
    
    void fireCallback(final Runnable runnable) {
        this.taskDistributor.execute(runnable);
    }
    
    String getLoadingUriForView(final ImageAware imageAware) {
        return this.cacheKeysForImageAwares.get(imageAware.getId());
    }
    
    ReentrantLock getLockForUri(final String s) {
        ReentrantLock reentrantLock;
        if ((reentrantLock = this.uriLocks.get(s)) == null) {
            reentrantLock = new ReentrantLock();
            this.uriLocks.put(s, reentrantLock);
        }
        return reentrantLock;
    }
    
    AtomicBoolean getPause() {
        return this.paused;
    }
    
    Object getPauseLock() {
        return this.pauseLock;
    }
    
    void handleSlowNetwork(final boolean b) {
        this.slowNetwork.set(b);
    }
    
    boolean isNetworkDenied() {
        return this.networkDenied.get();
    }
    
    boolean isSlowNetwork() {
        return this.slowNetwork.get();
    }
    
    void pause() {
        this.paused.set(true);
    }
    
    void prepareDisplayTaskFor(final ImageAware imageAware, final String s) {
        this.cacheKeysForImageAwares.put(imageAware.getId(), s);
    }
    
    void resume() {
        this.paused.set(false);
        synchronized (this.pauseLock) {
            this.pauseLock.notifyAll();
        }
    }
    
    void stop() {
        if (!this.configuration.customExecutor) {
            ((ExecutorService)this.taskExecutor).shutdownNow();
        }
        if (!this.configuration.customExecutorForCachedImages) {
            ((ExecutorService)this.taskExecutorForCachedImages).shutdownNow();
        }
        this.cacheKeysForImageAwares.clear();
        this.uriLocks.clear();
    }
    
    void submit(final LoadAndDisplayImageTask loadAndDisplayImageTask) {
        this.taskDistributor.execute(new Runnable() {
            @Override
            public void run() {
                final File value = ImageLoaderEngine.this.configuration.diskCache.get(loadAndDisplayImageTask.getLoadingUri());
                int n;
                if (value != null && value.exists()) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                ImageLoaderEngine.this.initExecutorsIfNeed();
                if (n != 0) {
                    ImageLoaderEngine.this.taskExecutorForCachedImages.execute(loadAndDisplayImageTask);
                    return;
                }
                ImageLoaderEngine.this.taskExecutor.execute(loadAndDisplayImageTask);
            }
        });
    }
    
    void submit(final ProcessAndDisplayImageTask processAndDisplayImageTask) {
        this.initExecutorsIfNeed();
        this.taskExecutorForCachedImages.execute(processAndDisplayImageTask);
    }
}
