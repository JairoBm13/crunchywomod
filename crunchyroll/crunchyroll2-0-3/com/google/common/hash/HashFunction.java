// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.hash;

public interface HashFunction
{
    int bits();
    
    Hasher newHasher();
}
