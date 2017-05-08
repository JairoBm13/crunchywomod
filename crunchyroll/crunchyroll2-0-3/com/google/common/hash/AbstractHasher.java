// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

abstract class AbstractHasher implements Hasher
{
    @Override
    public Hasher putString(final CharSequence charSequence) {
        for (int i = 0; i < charSequence.length(); ++i) {
            this.putChar(charSequence.charAt(i));
        }
        return this;
    }
}
