// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binding;

public interface ElementVisitor<V>
{
     <T> V visit(final Binding<T> p0);
    
    V visit(final DisableCircularProxiesOption p0);
    
    V visit(final InjectionRequest<?> p0);
    
     <T> V visit(final MembersInjectorLookup<T> p0);
    
    V visit(final Message p0);
    
    V visit(final PrivateElements p0);
    
     <T> V visit(final ProviderLookup<T> p0);
    
    V visit(final RequireExplicitBindingsOption p0);
    
    V visit(final ScopeBinding p0);
    
    V visit(final StaticInjectionRequest p0);
    
    V visit(final TypeConverterBinding p0);
    
    V visit(final TypeListenerBinding p0);
}
