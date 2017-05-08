// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.util.Iterator;
import java.util.Set;
import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.util.Collections;
import java.util.HashMap;
import android.graphics.Bitmap;
import java.util.Map;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;

public class UsingFreqLimitedMemoryCache extends LimitedMemoryCache
{
    private final Map<Bitmap, Integer> usingCounts;
    
    public UsingFreqLimitedMemoryCache(final int n) {
        super(n);
        this.usingCounts = Collections.synchronizedMap(new HashMap<Bitmap, Integer>());
    }
    
    @Override
    public void clear() {
        this.usingCounts.clear();
        super.clear();
    }
    
    @Override
    protected Reference<Bitmap> createReference(final Bitmap bitmap) {
        return new WeakReference<Bitmap>(bitmap);
    }
    
    @Override
    public Bitmap get(final String s) {
        final Bitmap value = super.get(s);
        if (value != null) {
            final Integer n = this.usingCounts.get(value);
            if (n != null) {
                this.usingCounts.put(value, n + 1);
            }
        }
        return value;
    }
    
    @Override
    protected int getSize(final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        boolean b = false;
        if (super.put(s, bitmap)) {
            this.usingCounts.put(bitmap, 0);
            b = true;
        }
        return b;
    }
    
    @Override
    public Bitmap remove(final String s) {
        final Bitmap value = super.get(s);
        if (value != null) {
            this.usingCounts.remove(value);
        }
        return super.remove(s);
    }
    
    @Override
    protected Bitmap removeNext() {
        Integer n = null;
        Object o = null;
        final Set<Map.Entry<Bitmap, Integer>> entrySet = this.usingCounts.entrySet();
        synchronized (this.usingCounts) {
            for (final Map.Entry<Bitmap, Integer> entry : entrySet) {
                if (o == null) {
                    o = entry.getKey();
                    n = entry.getValue();
                }
                else {
                    final Integer n2 = entry.getValue();
                    if (n2 >= n) {
                        continue;
                    }
                    n = n2;
                    o = entry.getKey();
                }
            }
            // monitorexit(this.usingCounts)
            this.usingCounts.remove(o);
            return (Bitmap)o;
        }
    }
}
