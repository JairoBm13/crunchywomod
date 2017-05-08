// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.util.Collections;
import java.util.LinkedHashMap;
import android.graphics.Bitmap;
import java.util.Map;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;

public class LRULimitedMemoryCache extends LimitedMemoryCache
{
    private static final int INITIAL_CAPACITY = 10;
    private static final float LOAD_FACTOR = 1.1f;
    private final Map<String, Bitmap> lruCache;
    
    public LRULimitedMemoryCache(final int n) {
        super(n);
        this.lruCache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.1f, true));
    }
    
    @Override
    public void clear() {
        this.lruCache.clear();
        super.clear();
    }
    
    @Override
    protected Reference<Bitmap> createReference(final Bitmap bitmap) {
        return new WeakReference<Bitmap>(bitmap);
    }
    
    @Override
    public Bitmap get(final String s) {
        this.lruCache.get(s);
        return super.get(s);
    }
    
    @Override
    protected int getSize(final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        if (super.put(s, bitmap)) {
            this.lruCache.put(s, bitmap);
            return true;
        }
        return false;
    }
    
    @Override
    public Bitmap remove(final String s) {
        this.lruCache.remove(s);
        return super.remove(s);
    }
    
    @Override
    protected Bitmap removeNext() {
        Bitmap bitmap = null;
        synchronized (this.lruCache) {
            final Iterator<Map.Entry<String, Bitmap>> iterator = this.lruCache.entrySet().iterator();
            if (iterator.hasNext()) {
                bitmap = iterator.next().getValue();
                iterator.remove();
            }
            return bitmap;
        }
    }
}
