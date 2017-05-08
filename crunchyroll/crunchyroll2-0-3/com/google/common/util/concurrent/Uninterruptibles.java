// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class Uninterruptibles
{
    public static <V> V getUninterruptibly(final Future<V> future) throws ExecutionException {
        boolean b = false;
        try {
            return future.get();
        }
        catch (InterruptedException ex) {
            b = true;
            return future.get();
        }
        finally {
            if (b) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
