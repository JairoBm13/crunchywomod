// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

public interface Scope
{
     <T> Provider<T> scope(final Key<T> p0, final Provider<T> p1);
}
