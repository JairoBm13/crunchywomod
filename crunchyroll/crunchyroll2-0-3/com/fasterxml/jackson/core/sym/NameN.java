// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

public final class NameN extends Name
{
    final int mQuadLen;
    final int[] mQuads;
    
    NameN(final String s, final int n, final int[] mQuads, final int mQuadLen) {
        super(s, n);
        if (mQuadLen < 3) {
            throw new IllegalArgumentException("Qlen must >= 3");
        }
        this.mQuads = mQuads;
        this.mQuadLen = mQuadLen;
    }
    
    @Override
    public boolean equals(final int n) {
        return false;
    }
    
    @Override
    public boolean equals(final int n, final int n2) {
        return false;
    }
    
    @Override
    public boolean equals(final int[] array, final int n) {
        if (n == this.mQuadLen) {
            for (int i = 0; i < n; ++i) {
                if (array[i] != this.mQuads[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
