// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson;

public interface ExclusionStrategy
{
    boolean shouldSkipClass(final Class<?> p0);
    
    boolean shouldSkipField(final FieldAttributes p0);
}
