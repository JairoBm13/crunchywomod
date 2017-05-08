// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.utils;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import java.util.Comparator;

public final class MemoryCacheUtils
{
    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";
    
    public static Comparator<String> createFuzzyKeyComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(final String s, final String s2) {
                return s.substring(0, s.lastIndexOf("_")).compareTo(s2.substring(0, s2.lastIndexOf("_")));
            }
        };
    }
    
    public static List<String> findCacheKeysForImageUri(final String s, final MemoryCache memoryCache) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String s2 : memoryCache.keys()) {
            if (s2.startsWith(s)) {
                list.add(s2);
            }
        }
        return list;
    }
    
    public static List<Bitmap> findCachedBitmapsForImageUri(final String s, final MemoryCache memoryCache) {
        final ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        for (final String s2 : memoryCache.keys()) {
            if (s2.startsWith(s)) {
                list.add(memoryCache.get(s2));
            }
        }
        return list;
    }
    
    public static String generateKey(final String s, final ImageSize imageSize) {
        return s + "_" + imageSize.getWidth() + "x" + imageSize.getHeight();
    }
    
    public static void removeFromCache(final String s, final MemoryCache memoryCache) {
        final ArrayList<String> list = new ArrayList<String>();
        for (final String s2 : memoryCache.keys()) {
            if (s2.startsWith(s)) {
                list.add(s2);
            }
        }
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            memoryCache.remove(iterator2.next());
        }
    }
}
