// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

public class UnmetDependencyException extends RuntimeException
{
    public UnmetDependencyException() {
    }
    
    public UnmetDependencyException(final String s) {
        super(s);
    }
    
    public UnmetDependencyException(final Throwable t) {
        super(t);
    }
}
