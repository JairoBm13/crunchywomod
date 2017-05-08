// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

public interface Hasher
{
    HashCode hash();
    
    Hasher putChar(final char p0);
    
    Hasher putInt(final int p0);
    
    Hasher putString(final CharSequence p0);
}
