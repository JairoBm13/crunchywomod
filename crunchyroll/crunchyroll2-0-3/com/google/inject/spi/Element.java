// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;

public interface Element
{
     <T> T acceptVisitor(final ElementVisitor<T> p0);
    
    void applyTo(final Binder p0);
    
    Object getSource();
}
