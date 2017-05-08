// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.cache;

import android.content.Context;

public class MemoryValueCache<T> extends AbstractValueCache<T>
{
    private T value;
    
    public MemoryValueCache() {
        this(null);
    }
    
    public MemoryValueCache(final ValueCache<T> valueCache) {
        super(valueCache);
    }
    
    @Override
    protected void cacheValue(final Context context, final T value) {
        this.value = value;
    }
    
    @Override
    protected T getCached(final Context context) {
        return this.value;
    }
}
