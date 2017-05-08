// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory;

import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import android.graphics.Bitmap;
import java.lang.ref.Reference;
import java.util.Map;

public abstract class BaseMemoryCache implements MemoryCache
{
    private final Map<String, Reference<Bitmap>> softMap;
    
    public BaseMemoryCache() {
        this.softMap = Collections.synchronizedMap(new HashMap<String, Reference<Bitmap>>());
    }
    
    @Override
    public void clear() {
        this.softMap.clear();
    }
    
    protected abstract Reference<Bitmap> createReference(final Bitmap p0);
    
    @Override
    public Bitmap get(final String s) {
        final Bitmap bitmap = null;
        final Reference<Bitmap> reference = this.softMap.get(s);
        Bitmap bitmap2 = bitmap;
        if (reference != null) {
            bitmap2 = reference.get();
        }
        return bitmap2;
    }
    
    @Override
    public Collection<String> keys() {
        synchronized (this.softMap) {
            return new HashSet<String>(this.softMap.keySet());
        }
    }
    
    @Override
    public boolean put(final String s, final Bitmap bitmap) {
        this.softMap.put(s, this.createReference(bitmap));
        return true;
    }
    
    @Override
    public Bitmap remove(final String s) {
        final Reference<Bitmap> reference = this.softMap.remove(s);
        if (reference == null) {
            return null;
        }
        return reference.get();
    }
}
