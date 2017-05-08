// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.MembersInjector;
import com.google.inject.Provider;

public interface TypeEncounter<I>
{
     <T> Provider<T> getProvider(final Class<T> p0);
    
    void register(final MembersInjector<? super I> p0);
    
    void register(final InjectionListener<? super I> p0);
}
