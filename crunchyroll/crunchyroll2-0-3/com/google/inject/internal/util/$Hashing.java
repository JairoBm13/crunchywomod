// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

final class $Hashing
{
    static int chooseTableSize(final int n) {
        if (n < 536870912) {
            return Integer.highestOneBit(n) << 2;
        }
        $Preconditions.checkArgument(n < 1073741824, (Object)"collection too large");
        return 1073741824;
    }
    
    static int smear(int n) {
        n ^= (n >>> 20 ^ n >>> 12);
        return n >>> 7 ^ n ^ n >>> 4;
    }
}
