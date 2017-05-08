// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

abstract class AbstractCompositeHashFunction extends AbstractStreamingHashFunction
{
    final HashFunction[] functions;
    
    AbstractCompositeHashFunction(final HashFunction... functions) {
        this.functions = functions;
    }
    
    abstract HashCode makeHash(final Hasher[] p0);
    
    @Override
    public Hasher newHasher() {
        final Hasher[] array = new Hasher[this.functions.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.functions[i].newHasher();
        }
        return new Hasher() {
            @Override
            public HashCode hash() {
                return AbstractCompositeHashFunction.this.makeHash(array);
            }
            
            @Override
            public Hasher putChar(final char c) {
                final Hasher[] val$hashers = array;
                for (int length = val$hashers.length, i = 0; i < length; ++i) {
                    val$hashers[i].putChar(c);
                }
                return this;
            }
            
            @Override
            public Hasher putInt(final int n) {
                final Hasher[] val$hashers = array;
                for (int length = val$hashers.length, i = 0; i < length; ++i) {
                    val$hashers[i].putInt(n);
                }
                return this;
            }
            
            @Override
            public Hasher putString(final CharSequence charSequence) {
                final Hasher[] val$hashers = array;
                for (int length = val$hashers.length, i = 0; i < length; ++i) {
                    val$hashers[i].putString(charSequence);
                }
                return this;
            }
        };
    }
}
