// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binding;

public abstract class DefaultElementVisitor<V> implements ElementVisitor<V>
{
    @Override
    public <T> V visit(final Binding<T> binding) {
        return this.visitOther(binding);
    }
    
    @Override
    public V visit(final DisableCircularProxiesOption disableCircularProxiesOption) {
        return this.visitOther(disableCircularProxiesOption);
    }
    
    @Override
    public V visit(final InjectionRequest<?> injectionRequest) {
        return this.visitOther(injectionRequest);
    }
    
    @Override
    public <T> V visit(final MembersInjectorLookup<T> membersInjectorLookup) {
        return this.visitOther(membersInjectorLookup);
    }
    
    @Override
    public V visit(final Message message) {
        return this.visitOther(message);
    }
    
    @Override
    public V visit(final PrivateElements privateElements) {
        return this.visitOther(privateElements);
    }
    
    @Override
    public <T> V visit(final ProviderLookup<T> providerLookup) {
        return this.visitOther(providerLookup);
    }
    
    @Override
    public V visit(final RequireExplicitBindingsOption requireExplicitBindingsOption) {
        return this.visitOther(requireExplicitBindingsOption);
    }
    
    @Override
    public V visit(final ScopeBinding scopeBinding) {
        return this.visitOther(scopeBinding);
    }
    
    @Override
    public V visit(final StaticInjectionRequest staticInjectionRequest) {
        return this.visitOther(staticInjectionRequest);
    }
    
    @Override
    public V visit(final TypeConverterBinding typeConverterBinding) {
        return this.visitOther(typeConverterBinding);
    }
    
    @Override
    public V visit(final TypeListenerBinding typeListenerBinding) {
        return this.visitOther(typeListenerBinding);
    }
    
    protected V visitOther(final Element element) {
        return null;
    }
}
