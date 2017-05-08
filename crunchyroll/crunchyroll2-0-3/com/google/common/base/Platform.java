// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

final class Platform
{
    private static final ThreadLocal<char[]> DEST_TL;
    
    static {
        DEST_TL = new ThreadLocal<char[]>() {
            @Override
            protected char[] initialValue() {
                return new char[1024];
            }
        };
    }
    
    static CharMatcher precomputeCharMatcher(final CharMatcher charMatcher) {
        return charMatcher.precomputedInternal();
    }
    
    static long systemNanoTime() {
        return System.nanoTime();
    }
}
