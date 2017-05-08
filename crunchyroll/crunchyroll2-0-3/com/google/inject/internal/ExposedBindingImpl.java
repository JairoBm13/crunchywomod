// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.Injector;
import com.google.inject.spi.Dependency;
import java.util.Set;
import com.google.inject.Binder;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.Key;
import com.google.inject.spi.PrivateElements;
import com.google.inject.spi.ExposedBinding;

public final class ExposedBindingImpl<T> extends BindingImpl<T> implements ExposedBinding<T>
{
    private final PrivateElements privateElements;
    
    public ExposedBindingImpl(final InjectorImpl injectorImpl, final Object o, final Key<T> key, final InternalFactory<T> internalFactory, final PrivateElements privateElements) {
        super(injectorImpl, key, o, (InternalFactory<? extends T>)internalFactory, Scoping.UNSCOPED);
        this.privateElements = privateElements;
    }
    
    @Override
    public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit((ExposedBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        throw new UnsupportedOperationException("This element represents a synthetic binding.");
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        return (Set<Dependency<?>>)$ImmutableSet.of(Dependency.get((Key<Object>)Key.get((Class<T>)Injector.class)));
    }
    
    public PrivateElements getPrivateElements() {
        return this.privateElements;
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(ExposedBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("privateElements", this.privateElements).toString();
    }
}
