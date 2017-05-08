// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory;

import java.util.Collection;
import android.graphics.Bitmap;

public interface MemoryCache
{
    void clear();
    
    Bitmap get(final String p0);
    
    Collection<String> keys();
    
    boolean put(final String p0, final Bitmap p1);
    
    Bitmap remove(final String p0);
}
