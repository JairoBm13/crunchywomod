// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

public interface PriorityProvider<T> extends Comparable<T>
{
    Priority getPriority();
}
