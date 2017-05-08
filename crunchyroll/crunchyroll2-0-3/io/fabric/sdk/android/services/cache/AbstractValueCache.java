// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.cache;

import android.content.Context;

public abstract class AbstractValueCache<T> implements ValueCache<T>
{
    private final ValueCache<T> childCache;
    
    public AbstractValueCache(final ValueCache<T> childCache) {
        this.childCache = childCache;
    }
    
    private void cache(final Context context, final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        this.cacheValue(context, t);
    }
    
    protected abstract void cacheValue(final Context p0, final T p1);
    
    @Override
    public final T get(final Context context, final ValueLoader<T> valueLoader) throws Exception {
        synchronized (this) {
            T cached;
            if ((cached = this.getCached(context)) == null) {
                T t;
                if (this.childCache != null) {
                    t = this.childCache.get(context, valueLoader);
                }
                else {
                    t = valueLoader.load(context);
                }
                this.cache(context, t);
                cached = t;
            }
            return cached;
        }
    }
    
    protected abstract T getCached(final Context p0);
}
