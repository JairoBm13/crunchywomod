// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.binder;

import javax.inject.Provider;
import com.google.inject.TypeLiteral;
import java.lang.reflect.Constructor;
import com.google.inject.Key;

public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder
{
    ScopedBindingBuilder to(final Key<? extends T> p0);
    
     <S extends T> ScopedBindingBuilder toConstructor(final Constructor<S> p0, final TypeLiteral<? extends S> p1);
    
    void toInstance(final T p0);
    
    ScopedBindingBuilder toProvider(final Key<? extends Provider<? extends T>> p0);
    
    ScopedBindingBuilder toProvider(final com.google.inject.Provider<? extends T> p0);
    
    ScopedBindingBuilder toProvider(final Class<? extends Provider<? extends T>> p0);
}
