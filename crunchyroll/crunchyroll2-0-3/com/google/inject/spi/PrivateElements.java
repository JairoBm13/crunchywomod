// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.util.Set;
import java.util.List;

public interface PrivateElements extends Element
{
    List<Element> getElements();
    
    Set<Key<?>> getExposedKeys();
    
    Object getExposedSource(final Key<?> p0);
    
    Injector getInjector();
}
