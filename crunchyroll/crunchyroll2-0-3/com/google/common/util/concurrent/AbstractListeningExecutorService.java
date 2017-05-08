// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.Collection;

abstract class AbstractListeningExecutorService implements ListeningExecutorService
{
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> iterator) throws InterruptedException {
        if (iterator == null) {
            throw new NullPointerException();
        }
        Object iterator2 = new ArrayList(((Collection)iterator).size());
        try {
            final Iterator<Callable<V>> iterator3 = ((Collection<Callable<V>>)iterator).iterator();
            while (iterator3.hasNext()) {
                final ListenableFutureTask<Object> create = ListenableFutureTask.create((Callable<Object>)iterator3.next());
                ((List<ListenableFutureTask<Object>>)iterator2).add(create);
                this.execute(create);
            }
        }
        finally {
            if (!false) {
                iterator2 = ((List<Future>)iterator2).iterator();
                while (((Iterator)iterator2).hasNext()) {
                    ((Iterator<Future>)iterator2).next().cancel(true);
                }
                goto Label_0208;
            }
            goto Label_0208;
        }
        iterator = ((List<Future>)iterator2).iterator();
        while (iterator.hasNext()) {
            final Future future = iterator.next();
            if (!future.isDone()) {
                try {
                    future.get();
                }
                catch (CancellationException ex) {}
                catch (ExecutionException ex2) {}
            }
        }
        goto Label_0169;
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> iterator, long nanoTime, TimeUnit iterator2) throws InterruptedException {
        if (iterator == null || iterator2 == null) {
            throw new NullPointerException();
        }
        long nanos = iterator2.toNanos(nanoTime);
        iterator2 = (TimeUnit)new ArrayList(((Collection)iterator).size());
        try {
            final Iterator<Future> iterator3 = ((Collection<Future>)iterator).iterator();
            while (iterator3.hasNext()) {
                ((List<ListenableFutureTask<Object>>)iterator2).add(ListenableFutureTask.create((Callable<Object>)(Callable<V>)iterator3.next()));
            }
        }
        finally {
            if (!false) {
                iterator2 = (TimeUnit)((List<Object>)iterator2).iterator();
                while (((Iterator)iterator2).hasNext()) {
                    ((Iterator<Future>)iterator2).next().cancel(true);
                }
                goto Label_0431;
            }
            goto Label_0431;
        }
        nanoTime = System.nanoTime();
        final Iterator<Runnable> iterator4 = ((List<Runnable>)iterator2).iterator();
        while (iterator4.hasNext()) {
            this.execute(iterator4.next());
            final long nanoTime2 = System.nanoTime();
            final long n = nanos - (nanoTime2 - nanoTime);
            nanoTime = nanoTime2;
            nanos = n;
            if (n <= 0L) {
                if (!false) {
                    final Iterator<Future> iterator5 = ((List<Future>)iterator2).iterator();
                    while (iterator5.hasNext()) {
                        iterator5.next().cancel(true);
                    }
                    goto Label_0433;
                }
                goto Label_0433;
            }
        }
        iterator = ((List<Object>)iterator2).iterator();
        Block_9: {
            while (iterator.hasNext()) {
                final Future future = iterator.next();
                if (!future.isDone()) {
                    if (nanos <= 0L) {
                        break Block_9;
                    }
                    while (true) {
                        try {
                            future.get(nanos, TimeUnit.NANOSECONDS);
                            final long nanoTime3 = System.nanoTime();
                            nanos -= nanoTime3 - nanoTime;
                            nanoTime = nanoTime3;
                        }
                        catch (TimeoutException ex) {
                            if (!false) {
                                final Iterator<Future> iterator6 = ((List<Future>)iterator2).iterator();
                                while (iterator6.hasNext()) {
                                    iterator6.next().cancel(true);
                                }
                                goto Label_0433;
                            }
                            goto Label_0433;
                        }
                        catch (ExecutionException ex2) {
                            continue;
                        }
                        catch (CancellationException ex3) {
                            continue;
                        }
                        break;
                    }
                }
            }
            goto Label_0391;
        }
        if (!false) {
            final Iterator<Future> iterator7 = ((List<Future>)iterator2).iterator();
            while (iterator7.hasNext()) {
                iterator7.next().cancel(true);
            }
            goto Label_0433;
        }
        goto Label_0433;
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        try {
            return MoreExecutors.invokeAnyImpl((ListeningExecutorService)this, collection, false, 0L);
        }
        catch (TimeoutException ex) {
            throw new AssertionError();
        }
    }
    
    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> collection, final long n, final TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return MoreExecutors.invokeAnyImpl((ListeningExecutorService)this, collection, true, timeUnit.toNanos(n));
    }
    
    @Override
    public ListenableFuture<?> submit(final Runnable runnable) {
        final ListenableFutureTask<Object> create = ListenableFutureTask.create(runnable, (Object)null);
        this.execute(create);
        return create;
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Runnable runnable, final T t) {
        final ListenableFutureTask<T> create = ListenableFutureTask.create(runnable, t);
        this.execute(create);
        return create;
    }
    
    @Override
    public <T> ListenableFuture<T> submit(final Callable<T> callable) {
        final ListenableFutureTask<T> create = ListenableFutureTask.create(callable);
        this.execute(create);
        return create;
    }
}
