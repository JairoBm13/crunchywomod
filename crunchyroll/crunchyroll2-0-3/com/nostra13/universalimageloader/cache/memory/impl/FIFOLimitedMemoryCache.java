// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import java.util.Collections;
import java.util.LinkedList;
import android.graphics.Bitmap;
import java.util.List;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;

public class FIFOLimitedMemoryCache extends LimitedMemoryCache
{
    private final List<Bitmap> queue;
    
    public FIFOLimitedMemoryCache(final int n) {
        super(n);
        this.queue = Collections.synchronizedList(new LinkedList<Bitmap>());
    }
    
    @Override
    public void clear() {
        this.queue.clear();
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
            this.queue.add(bitmap);
            return true;
        }
        return false;
    }
    
    @Override
    public Bitmap remove(final String s) {
        final Bitmap value = super.get(s);
        if (value != null) {
            this.queue.remove(value);
        }
        return super.remove(s);
    }
    
    @Override
    protected Bitmap removeNext() {
        return this.queue.remove(0);
    }
}
