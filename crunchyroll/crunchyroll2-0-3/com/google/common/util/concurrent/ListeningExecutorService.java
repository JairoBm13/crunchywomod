// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public interface ListeningExecutorService extends ExecutorService
{
     <T> ListenableFuture<T> submit(final Callable<T> p0);
}
