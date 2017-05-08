// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.Dependency;
import com.google.inject.internal.util.$Objects;
import com.google.inject.Binder;
import com.google.inject.spi.ProviderWithExtensionVisitor;
import com.google.inject.spi.BindingTargetVisitor;
import java.util.Set;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.spi.ProviderInstanceBinding;

final class ProviderInstanceBindingImpl<T> extends BindingImpl<T> implements ProviderInstanceBinding<T>
{
    final $ImmutableSet<InjectionPoint> injectionPoints;
    final Provider<? extends T> providerInstance;
    
    public ProviderInstanceBindingImpl(final InjectorImpl injectorImpl, final Key<T> key, final Object o, final InternalFactory<? extends T> internalFactory, final Scoping scoping, final Provider<? extends T> providerInstance, final Set<InjectionPoint> set) {
        super(injectorImpl, key, o, internalFactory, scoping);
        this.providerInstance = providerInstance;
        this.injectionPoints = $ImmutableSet.copyOf((Iterable<? extends InjectionPoint>)set);
    }
    
    public ProviderInstanceBindingImpl(final Object o, final Key<T> key, final Scoping scoping, final Set<InjectionPoint> set, final Provider<? extends T> providerInstance) {
        super(o, key, scoping);
        this.injectionPoints = $ImmutableSet.copyOf((Iterable<? extends InjectionPoint>)set);
        this.providerInstance = providerInstance;
    }
    
    @Override
    public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        if (this.providerInstance instanceof ProviderWithExtensionVisitor) {
            return ((ProviderWithExtensionVisitor)this.providerInstance).acceptExtensionVisitor(bindingTargetVisitor, this);
        }
        return bindingTargetVisitor.visit((ProviderInstanceBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toProvider(this.getProviderInstance()));
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof ProviderInstanceBindingImpl) {
            final ProviderInstanceBindingImpl providerInstanceBindingImpl = (ProviderInstanceBindingImpl)o;
            b2 = b;
            if (this.getKey().equals(providerInstanceBindingImpl.getKey())) {
                b2 = b;
                if (this.getScoping().equals(providerInstanceBindingImpl.getScoping())) {
                    b2 = b;
                    if ($Objects.equal(this.providerInstance, providerInstanceBindingImpl.providerInstance)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        if (this.providerInstance instanceof HasDependencies) {
            return (Set<Dependency<?>>)$ImmutableSet.copyOf((Iterable<?>)((HasDependencies)this.providerInstance).getDependencies());
        }
        return Dependency.forInjectionPoints(this.injectionPoints);
    }
    
    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return this.injectionPoints;
    }
    
    @Override
    public Provider<? extends T> getProviderInstance() {
        return this.providerInstance;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getKey(), this.getScoping());
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(ProviderInstanceBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("provider", this.providerInstance).toString();
    }
    
    public BindingImpl<T> withKey(final Key<T> key) {
        return new ProviderInstanceBindingImpl(this.getSource(), (Key<Object>)key, this.getScoping(), this.injectionPoints, this.providerInstance);
    }
    
    public BindingImpl<T> withScoping(final Scoping scoping) {
        return new ProviderInstanceBindingImpl(this.getSource(), this.getKey(), scoping, this.injectionPoints, this.providerInstance);
    }
}
