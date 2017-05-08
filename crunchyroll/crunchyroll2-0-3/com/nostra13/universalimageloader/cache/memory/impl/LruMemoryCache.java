// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.util.HashSet;
import java.util.Collection;
import java.util.Map;
import android.graphics.Bitmap;
import java.util.LinkedHashMap;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;

public class LruMemoryCache implements MemoryCache
{
    private final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int size;
    
    public LruMemoryCache(final int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
    }
    
    private int sizeOf(final String s, final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
    private void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    throw new IllegalStateException(this.getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }
            }
            if (this.size <= n || this.map.isEmpty()) {
                // monitorexit(this)
                return;
            }
            final Map.Entry<String, Bitmap> entry = this.map.entrySet().iterator().next();
            if (entry == null) {
                // monitorexit(this)
                return;
            }
            final String s = entry.getKey();
            final Bitmap bitmap = entry.getValue();
            this.map.remove(s);
            this.size -= this.sizeOf(s, bitmap);
        }
        // monitorexit(this)
    }
    
    @Override
    public void clear() {
        this.trimToSize(-1);
    }
    
    @Override
    public final Bitmap get(final String s) {
        if (s == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            return this.map.get(s);
        }
    }
    
    @Override
    public Collection<String> keys() {
        synchronized (this) {
            return new HashSet<String>(this.map.keySet());
        }
    }
    
    @Override
    public final boolean put(final String s, Bitmap bitmap) {
        if (s == null || bitmap == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.size += this.sizeOf(s, bitmap);
            bitmap = this.map.put(s, bitmap);
            if (bitmap != null) {
                this.size -= this.sizeOf(s, bitmap);
            }
            // monitorexit(this)
            this.trimToSize(this.maxSize);
            return true;
        }
    }
    
    @Override
    public final Bitmap remove(final String s) {
        if (s == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            final Bitmap bitmap = this.map.remove(s);
            if (bitmap != null) {
                this.size -= this.sizeOf(s, bitmap);
            }
            return bitmap;
        }
    }
    
    @Override
    public final String toString() {
        synchronized (this) {
            return String.format("LruCache[maxSize=%d]", this.maxSize);
        }
    }
}
