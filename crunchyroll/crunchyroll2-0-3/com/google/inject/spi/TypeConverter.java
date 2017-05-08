// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.TypeLiteral;

public interface TypeConverter
{
    Object convert(final String p0, final TypeLiteral<?> p1);
}
