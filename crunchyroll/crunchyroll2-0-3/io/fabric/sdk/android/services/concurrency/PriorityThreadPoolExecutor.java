// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor
{
    private static final int CORE_POOL_SIZE;
    private static final int CPU_COUNT;
    private static final int MAXIMUM_POOL_SIZE;
    
    static {
        CPU_COUNT = Runtime.getRuntime().availableProcessors();
        CORE_POOL_SIZE = PriorityThreadPoolExecutor.CPU_COUNT + 1;
        MAXIMUM_POOL_SIZE = PriorityThreadPoolExecutor.CPU_COUNT * 2 + 1;
    }
    
    PriorityThreadPoolExecutor(final int n, final int n2, final long n3, final TimeUnit timeUnit, final DependencyPriorityBlockingQueue<T> dependencyPriorityBlockingQueue, final ThreadFactory threadFactory) {
        super(n, n2, n3, timeUnit, (BlockingQueue<Runnable>)dependencyPriorityBlockingQueue, threadFactory);
        this.prestartAllCoreThreads();
    }
    
    public static PriorityThreadPoolExecutor create() {
        return create(PriorityThreadPoolExecutor.CORE_POOL_SIZE, PriorityThreadPoolExecutor.MAXIMUM_POOL_SIZE);
    }
    
    public static <T extends java.lang.Runnable> PriorityThreadPoolExecutor create(final int n, final int n2) {
        return new PriorityThreadPoolExecutor(n, n2, 1L, TimeUnit.SECONDS, (DependencyPriorityBlockingQueue<T>)new DependencyPriorityBlockingQueue<java.lang.Runnable>(), new PriorityThreadFactory(10));
    }
    
    @Override
    protected void afterExecute(final Runnable runnable, final Throwable error) {
        final Task task = (Task)runnable;
        task.setFinished(true);
        task.setError(error);
        this.getQueue().recycleBlockedQueue();
        super.afterExecute(runnable, error);
    }
    
    @Override
    public void execute(final Runnable runnable) {
        if (PriorityTask.isProperDelegate(runnable)) {
            super.execute(runnable);
            return;
        }
        super.execute(this.newTaskFor(runnable, (Object)null));
    }
    
    @Override
    public DependencyPriorityBlockingQueue getQueue() {
        return (DependencyPriorityBlockingQueue)super.getQueue();
    }
    
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T t) {
        return new PriorityFutureTask<T>(runnable, t);
    }
    
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return new PriorityFutureTask<T>(callable);
    }
    
    protected static final class PriorityThreadFactory implements ThreadFactory
    {
        private final int threadPriority;
        
        public PriorityThreadFactory(final int threadPriority) {
            this.threadPriority = threadPriority;
        }
        
        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = new Thread(runnable);
            thread.setPriority(this.threadPriority);
            thread.setName("Queue");
            return thread;
        }
    }
}
