// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

public interface InitializationCallback<T>
{
    public static final InitializationCallback EMPTY = new Empty();
    
    void failure(final Exception p0);
    
    void success(final T p0);
    
    public static class Empty implements InitializationCallback<Object>
    {
        @Override
        public void failure(final Exception ex) {
        }
        
        @Override
        public void success(final Object o) {
        }
    }
}
