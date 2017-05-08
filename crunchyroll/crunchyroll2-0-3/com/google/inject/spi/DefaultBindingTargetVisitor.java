// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binding;

public abstract class DefaultBindingTargetVisitor<T, V> implements BindingTargetVisitor<T, V>
{
    @Override
    public V visit(final ConstructorBinding<? extends T> constructorBinding) {
        return this.visitOther(constructorBinding);
    }
    
    @Override
    public V visit(final ConvertedConstantBinding<? extends T> convertedConstantBinding) {
        return this.visitOther(convertedConstantBinding);
    }
    
    @Override
    public V visit(final ExposedBinding<? extends T> exposedBinding) {
        return this.visitOther(exposedBinding);
    }
    
    @Override
    public V visit(final InstanceBinding<? extends T> instanceBinding) {
        return this.visitOther(instanceBinding);
    }
    
    @Override
    public V visit(final LinkedKeyBinding<? extends T> linkedKeyBinding) {
        return this.visitOther(linkedKeyBinding);
    }
    
    @Override
    public V visit(final ProviderBinding<? extends T> providerBinding) {
        return this.visitOther(providerBinding);
    }
    
    @Override
    public V visit(final ProviderInstanceBinding<? extends T> providerInstanceBinding) {
        return this.visitOther(providerInstanceBinding);
    }
    
    @Override
    public V visit(final ProviderKeyBinding<? extends T> providerKeyBinding) {
        return this.visitOther(providerKeyBinding);
    }
    
    @Override
    public V visit(final UntargettedBinding<? extends T> untargettedBinding) {
        return this.visitOther(untargettedBinding);
    }
    
    protected V visitOther(final Binding<? extends T> binding) {
        return null;
    }
}
