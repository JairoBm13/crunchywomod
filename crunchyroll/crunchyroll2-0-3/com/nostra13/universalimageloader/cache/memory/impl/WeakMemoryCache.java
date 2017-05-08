// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.memory.impl;

import java.lang.ref.WeakReference;
import java.lang.ref.Reference;
import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.BaseMemoryCache;

public class WeakMemoryCache extends BaseMemoryCache
{
    @Override
    protected Reference<Bitmap> createReference(final Bitmap bitmap) {
        return new WeakReference<Bitmap>(bitmap);
    }
}
