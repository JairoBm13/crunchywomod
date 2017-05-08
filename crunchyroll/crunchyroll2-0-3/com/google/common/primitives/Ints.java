// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.primitives;

public final class Ints
{
    public static int compare(final int n, final int n2) {
        if (n < n2) {
            return -1;
        }
        if (n > n2) {
            return 1;
        }
        return 0;
    }
    
    public static int saturatedCast(final long n) {
        if (n > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (n < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int)n;
    }
}
