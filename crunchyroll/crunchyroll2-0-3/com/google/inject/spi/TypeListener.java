// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.TypeLiteral;

public interface TypeListener
{
     <I> void hear(final TypeLiteral<I> p0, final TypeEncounter<I> p1);
}
