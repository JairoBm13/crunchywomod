// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

public interface BindingTargetVisitor<T, V>
{
    V visit(final ConstructorBinding<? extends T> p0);
    
    V visit(final ConvertedConstantBinding<? extends T> p0);
    
    V visit(final ExposedBinding<? extends T> p0);
    
    V visit(final InstanceBinding<? extends T> p0);
    
    V visit(final LinkedKeyBinding<? extends T> p0);
    
    V visit(final ProviderBinding<? extends T> p0);
    
    V visit(final ProviderInstanceBinding<? extends T> p0);
    
    V visit(final ProviderKeyBinding<? extends T> p0);
    
    V visit(final UntargettedBinding<? extends T> p0);
}
