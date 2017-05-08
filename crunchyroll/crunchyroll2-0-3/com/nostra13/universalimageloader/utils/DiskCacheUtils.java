// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.utils;

import java.io.File;
import com.nostra13.universalimageloader.cache.disc.DiskCache;

public final class DiskCacheUtils
{
    public static File findInCache(final String s, final DiskCache diskCache) {
        final File value = diskCache.get(s);
        if (value != null && value.exists()) {
            return value;
        }
        return null;
    }
    
    public static boolean removeFromCache(final String s, final DiskCache diskCache) {
        final File value = diskCache.get(s);
        return value != null && value.exists() && value.delete();
    }
}
