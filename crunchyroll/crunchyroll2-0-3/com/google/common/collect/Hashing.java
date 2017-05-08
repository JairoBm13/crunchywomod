// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

final class Hashing
{
    static int smear(int n) {
        n ^= (n >>> 20 ^ n >>> 12);
        return n >>> 7 ^ n ^ n >>> 4;
    }
}
