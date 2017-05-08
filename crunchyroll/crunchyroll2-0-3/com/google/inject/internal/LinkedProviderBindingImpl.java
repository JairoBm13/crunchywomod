// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.spi.Dependency;
import java.util.Set;
import com.google.inject.internal.util.$Objects;
import com.google.inject.Binder;
import com.google.inject.spi.BindingTargetVisitor;
import javax.inject.Provider;
import com.google.inject.Key;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.HasDependencies;

final class LinkedProviderBindingImpl<T> extends BindingImpl<T> implements HasDependencies, ProviderKeyBinding<T>
{
    final Key<? extends Provider<? extends T>> providerKey;
    
    public LinkedProviderBindingImpl(final InjectorImpl injectorImpl, final Key<T> key, final Object o, final InternalFactory<? extends T> internalFactory, final Scoping scoping, final Key<? extends Provider<? extends T>> providerKey) {
        super(injectorImpl, key, o, internalFactory, scoping);
        this.providerKey = providerKey;
    }
    
    LinkedProviderBindingImpl(final Object o, final Key<T> key, final Scoping scoping, final Key<? extends Provider<? extends T>> providerKey) {
        super(o, key, scoping);
        this.providerKey = providerKey;
    }
    
    @Override
    public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit((ProviderKeyBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toProvider(this.getProviderKey()));
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof LinkedProviderBindingImpl) {
            final LinkedProviderBindingImpl linkedProviderBindingImpl = (LinkedProviderBindingImpl)o;
            b2 = b;
            if (this.getKey().equals(linkedProviderBindingImpl.getKey())) {
                b2 = b;
                if (this.getScoping().equals(linkedProviderBindingImpl.getScoping())) {
                    b2 = b;
                    if ($Objects.equal(this.providerKey, linkedProviderBindingImpl.providerKey)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        return (Set<Dependency<?>>)$ImmutableSet.of(Dependency.get(this.providerKey));
    }
    
    @Override
    public Key<? extends Provider<? extends T>> getProviderKey() {
        return this.providerKey;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getKey(), this.getScoping(), this.providerKey);
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(ProviderKeyBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("provider", this.providerKey).toString();
    }
    
    public BindingImpl<T> withKey(final Key<T> key) {
        return new LinkedProviderBindingImpl(this.getSource(), (Key<Object>)key, this.getScoping(), this.providerKey);
    }
    
    public BindingImpl<T> withScoping(final Scoping scoping) {
        return new LinkedProviderBindingImpl(this.getSource(), this.getKey(), scoping, this.providerKey);
    }
}
