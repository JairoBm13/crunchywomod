// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.util.Collection;
import android.graphics.Bitmap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;

public class LimitedAgeMemoryCache implements MemoryCache
{
    private final MemoryCache cache;
    private final Map<String, Long> loadingDates;
    private final long maxAge;
    
    public LimitedAgeMemoryCache(final MemoryCache cache, final long n) {
        this.loadingDates = Collections.synchronizedMap(new HashMap<String, Long>());
        this.cache = cache;
        this.maxAge = 1000L * n;
    }
    
    @Override
    public void clear() {
        this.cache.clear();
        this.loadingDates.clear();
    }
    
    @Override
    public Bitmap get(final String s) {
        final Long n = this.loadingDates.get(s);
        if (n != null && System.currentTimeMillis() - n > this.maxAge) {
            this.cache.remove(s);
            this.loadingDates.remove(s);
        }
        return this.cache.get(s);
    }
    
    @Override
    public Collection<String> keys() {
        return this.cache.keys();
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        final boolean put = this.cache.put(s, bitmap);
        if (put) {
            this.loadingDates.put(s, System.currentTimeMillis());
        }
        return put;
    }
    
    @Override
    public Bitmap remove(final String s) {
        this.loadingDates.remove(s);
        return this.cache.remove(s);
    }
}
