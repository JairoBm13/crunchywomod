// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public abstract class AbstractFuture<V> implements ListenableFuture<V>
{
    private final ExecutionList executionList;
    private final Sync<V> sync;
    
    protected AbstractFuture() {
        this.sync = new Sync<V>();
        this.executionList = new ExecutionList();
    }
    
    @Override
    public void addListener(final Runnable runnable, final Executor executor) {
        this.executionList.add(runnable, executor);
    }
    
    @Override
    public boolean cancel(final boolean b) {
        if (!this.sync.cancel()) {
            return false;
        }
        this.executionList.execute();
        if (b) {
            this.interruptTask();
        }
        return true;
    }
    
    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.sync.get();
    }
    
    @Override
    public V get(final long n, final TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
        return this.sync.get(timeUnit.toNanos(n));
    }
    
    protected void interruptTask() {
    }
    
    @Override
    public boolean isCancelled() {
        return this.sync.isCancelled();
    }
    
    @Override
    public boolean isDone() {
        return this.sync.isDone();
    }
    
    protected boolean set(final V v) {
        final boolean set = this.sync.set(v);
        if (set) {
            this.executionList.execute();
        }
        return set;
    }
    
    protected boolean setException(final Throwable t) {
        final boolean setException = this.sync.setException(Preconditions.checkNotNull(t));
        if (setException) {
            this.executionList.execute();
        }
        if (t instanceof Error) {
            throw (Error)t;
        }
        return setException;
    }
    
    static final class Sync<V> extends AbstractQueuedSynchronizer
    {
        private Throwable exception;
        private V value;
        
        private boolean complete(final V value, final Throwable exception, final int n) {
            final boolean compareAndSetState = this.compareAndSetState(0, 1);
            if (compareAndSetState) {
                this.value = value;
                this.exception = exception;
                this.releaseShared(n);
            }
            else if (this.getState() == 1) {
                this.acquireShared(-1);
                return compareAndSetState;
            }
            return compareAndSetState;
        }
        
        private V getValue() throws CancellationException, ExecutionException {
            final int state = this.getState();
            switch (state) {
                default: {
                    throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
                }
                case 2: {
                    if (this.exception != null) {
                        throw new ExecutionException(this.exception);
                    }
                    return this.value;
                }
                case 4: {
                    throw new CancellationException("Task was cancelled.");
                }
            }
        }
        
        boolean cancel() {
            return this.complete(null, null, 4);
        }
        
        V get() throws CancellationException, ExecutionException, InterruptedException {
            this.acquireSharedInterruptibly(-1);
            return this.getValue();
        }
        
        V get(final long n) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
            if (!this.tryAcquireSharedNanos(-1, n)) {
                throw new TimeoutException("Timeout waiting for task.");
            }
            return this.getValue();
        }
        
        boolean isCancelled() {
            return this.getState() == 4;
        }
        
        boolean isDone() {
            return (this.getState() & 0x6) != 0x0;
        }
        
        boolean set(final V v) {
            return this.complete(v, null, 2);
        }
        
        boolean setException(final Throwable t) {
            return this.complete(null, t, 2);
        }
        
        @Override
        protected int tryAcquireShared(final int n) {
            if (this.isDone()) {
                return 1;
            }
            return -1;
        }
        
        @Override
        protected boolean tryReleaseShared(final int state) {
            this.setState(state);
            return true;
        }
    }
}
