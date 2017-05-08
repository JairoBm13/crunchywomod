// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

public final class Name3 extends Name
{
    final int mQuad1;
    final int mQuad2;
    final int mQuad3;
    
    Name3(final String s, final int n, final int mQuad1, final int mQuad2, final int mQuad3) {
        super(s, n);
        this.mQuad1 = mQuad1;
        this.mQuad2 = mQuad2;
        this.mQuad3 = mQuad3;
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
        return n == 3 && array[0] == this.mQuad1 && array[1] == this.mQuad2 && array[2] == this.mQuad3;
    }
}
