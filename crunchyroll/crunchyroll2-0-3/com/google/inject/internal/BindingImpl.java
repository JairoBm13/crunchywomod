// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.Provider;
import com.google.inject.Key;
import com.google.inject.Binding;

public abstract class BindingImpl<T> implements Binding<T>
{
    private final InjectorImpl injector;
    private final InternalFactory<? extends T> internalFactory;
    private final Key<T> key;
    private volatile Provider<T> provider;
    private final Scoping scoping;
    private final Object source;
    
    public BindingImpl(final InjectorImpl injector, final Key<T> key, final Object source, final InternalFactory<? extends T> internalFactory, final Scoping scoping) {
        this.injector = injector;
        this.key = key;
        this.source = source;
        this.internalFactory = internalFactory;
        this.scoping = scoping;
    }
    
    protected BindingImpl(final Object source, final Key<T> key, final Scoping scoping) {
        this.internalFactory = null;
        this.injector = null;
        this.source = source;
        this.key = key;
        this.scoping = scoping;
    }
    
    @Override
    public <V> V acceptScopingVisitor(final BindingScopingVisitor<V> bindingScopingVisitor) {
        return this.scoping.acceptVisitor(bindingScopingVisitor);
    }
    
    @Override
    public <V> V acceptVisitor(final ElementVisitor<V> elementVisitor) {
        return elementVisitor.visit((Binding<Object>)this);
    }
    
    public InjectorImpl getInjector() {
        return this.injector;
    }
    
    public InternalFactory<? extends T> getInternalFactory() {
        return this.internalFactory;
    }
    
    @Override
    public Key<T> getKey() {
        return this.key;
    }
    
    @Override
    public Provider<T> getProvider() {
        if (this.provider == null) {
            if (this.injector == null) {
                throw new UnsupportedOperationException("getProvider() not supported for module bindings");
            }
            this.provider = this.injector.getProvider(this.key);
        }
        return this.provider;
    }
    
    public Scoping getScoping() {
        return this.scoping;
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public boolean isConstant() {
        return this instanceof InstanceBinding;
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(Binding.class).add("key", this.key).add("scope", this.scoping).add("source", this.source).toString();
    }
    
    protected BindingImpl<T> withKey(final Key<T> key) {
        throw new AssertionError();
    }
    
    protected BindingImpl<T> withScoping(final Scoping scoping) {
        throw new AssertionError();
    }
}
