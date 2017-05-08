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

public class LargestLimitedMemoryCache extends LimitedMemoryCache
{
    private final Map<Bitmap, Integer> valueSizes;
    
    public LargestLimitedMemoryCache(final int n) {
        super(n);
        this.valueSizes = Collections.synchronizedMap(new HashMap<Bitmap, Integer>());
    }
    
    @Override
    public void clear() {
        this.valueSizes.clear();
        super.clear();
    }
    
    @Override
    protected Reference<Bitmap> createReference(final Bitmap bitmap) {
        return new WeakReference<Bitmap>(bitmap);
    }
    
    @Override
    protected int getSize(final Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        if (super.put(s, bitmap)) {
            this.valueSizes.put(bitmap, this.getSize(bitmap));
            return true;
        }
        return false;
    }
    
    @Override
    public Bitmap remove(final String s) {
        final Bitmap value = super.get(s);
        if (value != null) {
            this.valueSizes.remove(value);
        }
        return super.remove(s);
    }
    
    @Override
    protected Bitmap removeNext() {
        Integer n = null;
        Object o = null;
        final Set<Map.Entry<Bitmap, Integer>> entrySet = this.valueSizes.entrySet();
        synchronized (this.valueSizes) {
            for (final Map.Entry<Bitmap, Integer> entry : entrySet) {
                if (o == null) {
                    o = entry.getKey();
                    n = entry.getValue();
                }
                else {
                    final Integer n2 = entry.getValue();
                    if (n2 <= n) {
                        continue;
                    }
                    n = n2;
                    o = entry.getKey();
                }
            }
            // monitorexit(this.valueSizes)
            this.valueSizes.remove(o);
            return (Bitmap)o;
        }
    }
}
