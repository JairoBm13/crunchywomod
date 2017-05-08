// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

public interface Task
{
    boolean isFinished();
    
    void setError(final Throwable p0);
    
    void setFinished(final boolean p0);
}
