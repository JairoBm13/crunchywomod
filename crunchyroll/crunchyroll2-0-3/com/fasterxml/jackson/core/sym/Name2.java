// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

public final class Name2 extends Name
{
    final int mQuad1;
    final int mQuad2;
    
    Name2(final String s, final int n, final int mQuad1, final int mQuad2) {
        super(s, n);
        this.mQuad1 = mQuad1;
        this.mQuad2 = mQuad2;
    }
    
    @Override
    public boolean equals(final int n) {
        return false;
    }
    
    @Override
    public boolean equals(final int n, final int n2) {
        return n == this.mQuad1 && n2 == this.mQuad2;
    }
    
    @Override
    public boolean equals(final int[] array, final int n) {
        return n == 2 && array[0] == this.mQuad1 && array[1] == this.mQuad2;
    }
}
