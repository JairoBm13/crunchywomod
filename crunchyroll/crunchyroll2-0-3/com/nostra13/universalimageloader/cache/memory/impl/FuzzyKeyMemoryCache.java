// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.util.Iterator;
import java.util.Collection;
import android.graphics.Bitmap;
import java.util.Comparator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;

public class FuzzyKeyMemoryCache implements MemoryCache
{
    private final MemoryCache cache;
    private final Comparator<String> keyComparator;
    
    public FuzzyKeyMemoryCache(final MemoryCache cache, final Comparator<String> keyComparator) {
        this.cache = cache;
        this.keyComparator = keyComparator;
    }
    
    @Override
    public void clear() {
        this.cache.clear();
    }
    
    @Override
    public Bitmap get(final String s) {
        return this.cache.get(s);
    }
    
    @Override
    public Collection<String> keys() {
        return this.cache.keys();
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        final MemoryCache cache = this.cache;
        // monitorenter(cache)
        final String s2 = null;
        try {
            final Iterator<String> iterator = this.cache.keys().iterator();
            String s3;
            do {
                s3 = s2;
                if (!iterator.hasNext()) {
                    break;
                }
                s3 = iterator.next();
            } while (this.keyComparator.compare(s, s3) != 0);
            if (s3 != null) {
                this.cache.remove(s3);
            }
            // monitorexit(cache)
            return this.cache.put(s, bitmap);
        }
        finally {
        }
        // monitorexit(cache)
    }
    
    @Override
    public Bitmap remove(final String s) {
        return this.cache.remove(s);
    }
}
