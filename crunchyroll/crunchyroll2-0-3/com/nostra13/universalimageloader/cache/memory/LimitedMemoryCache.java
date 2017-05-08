// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory;

import com.nostra13.universalimageloader.utils.L;
import java.util.Collections;
import java.util.LinkedList;
import android.graphics.Bitmap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedMemoryCache extends BaseMemoryCache
{
    private static final int MAX_NORMAL_CACHE_SIZE = 16777216;
    private static final int MAX_NORMAL_CACHE_SIZE_IN_MB = 16;
    private final AtomicInteger cacheSize;
    private final List<Bitmap> hardCache;
    private final int sizeLimit;
    
    public LimitedMemoryCache(final int sizeLimit) {
        this.hardCache = Collections.synchronizedList(new LinkedList<Bitmap>());
        this.sizeLimit = sizeLimit;
        this.cacheSize = new AtomicInteger();
        if (sizeLimit > 16777216) {
            L.w("You set too large memory cache size (more than %1$d Mb)", 16);
        }
    }
    
    @Override
    public void clear() {
        this.hardCache.clear();
        this.cacheSize.set(0);
        super.clear();
    }
    
    protected abstract int getSize(final Bitmap p0);
    
    protected int getSizeLimit() {
        return this.sizeLimit;
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        boolean b = false;
        final int size = this.getSize(bitmap);
        final int sizeLimit = this.getSizeLimit();
        int n = this.cacheSize.get();
        if (size < sizeLimit) {
            while (n + size > sizeLimit) {
                final Bitmap removeNext = this.removeNext();
                if (this.hardCache.remove(removeNext)) {
                    n = this.cacheSize.addAndGet(-this.getSize(removeNext));
                }
            }
            this.hardCache.add(bitmap);
            this.cacheSize.addAndGet(size);
            b = true;
        }
        super.put(s, bitmap);
        return b;
    }
    
    @Override
    public Bitmap remove(final String s) {
        final Bitmap value = super.get(s);
        if (value != null && this.hardCache.remove(value)) {
            this.cacheSize.addAndGet(-this.getSize(value));
        }
        return super.remove(s);
    }
    
    protected abstract Bitmap removeNext();
}
