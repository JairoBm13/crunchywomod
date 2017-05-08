// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

public final class Name1 extends Name
{
    static final Name1 sEmptyName;
    final int mQuad;
    
    static {
        sEmptyName = new Name1("", 0, 0);
    }
    
    Name1(final String s, final int n, final int mQuad) {
        super(s, n);
        this.mQuad = mQuad;
    }
    
    static Name1 getEmptyName() {
        return Name1.sEmptyName;
    }
    
    @Override
    public boolean equals(final int n) {
        return n == this.mQuad;
    }
    
    @Override
    public boolean equals(final int n, final int n2) {
        return n == this.mQuad && n2 == 0;
    }
    
    @Override
    public boolean equals(final int[] array, final int n) {
        return n == 1 && array[0] == this.mQuad;
    }
}
