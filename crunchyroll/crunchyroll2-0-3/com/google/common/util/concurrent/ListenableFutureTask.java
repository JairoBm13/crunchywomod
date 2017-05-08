// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public final class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V>
{
    private final ExecutionList executionList;
    
    private ListenableFutureTask(final Runnable runnable, final V v) {
        super(runnable, v);
        this.executionList = new ExecutionList();
    }
    
    private ListenableFutureTask(final Callable<V> callable) {
        super(callable);
        this.executionList = new ExecutionList();
    }
    
    public static <V> ListenableFutureTask<V> create(final Runnable runnable, final V v) {
        return new ListenableFutureTask<V>(runnable, v);
    }
    
    public static <V> ListenableFutureTask<V> create(final Callable<V> callable) {
        return new ListenableFutureTask<V>(callable);
    }
    
    @Override
    public void addListener(final Runnable runnable, final Executor executor) {
        this.executionList.add(runnable, executor);
    }
    
    @Override
    protected void done() {
        this.executionList.execute();
    }
}
