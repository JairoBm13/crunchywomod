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
import com.google.inject.Key;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.HasDependencies;

public final class LinkedBindingImpl<T> extends BindingImpl<T> implements HasDependencies, LinkedKeyBinding<T>
{
    final Key<? extends T> targetKey;
    
    public LinkedBindingImpl(final InjectorImpl injectorImpl, final Key<T> key, final Object o, final InternalFactory<? extends T> internalFactory, final Scoping scoping, final Key<? extends T> targetKey) {
        super(injectorImpl, key, o, internalFactory, scoping);
        this.targetKey = targetKey;
    }
    
    public LinkedBindingImpl(final Object o, final Key<T> key, final Scoping scoping, final Key<? extends T> targetKey) {
        super(o, key, scoping);
        this.targetKey = targetKey;
    }
    
    @Override
    public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit((LinkedKeyBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).to(this.getLinkedKey()));
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof LinkedBindingImpl) {
            final LinkedBindingImpl linkedBindingImpl = (LinkedBindingImpl)o;
            b2 = b;
            if (this.getKey().equals(linkedBindingImpl.getKey())) {
                b2 = b;
                if (this.getScoping().equals(linkedBindingImpl.getScoping())) {
                    b2 = b;
                    if ($Objects.equal(this.targetKey, linkedBindingImpl.targetKey)) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        return (Set<Dependency<?>>)$ImmutableSet.of(Dependency.get(this.targetKey));
    }
    
    @Override
    public Key<? extends T> getLinkedKey() {
        return this.targetKey;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getKey(), this.getScoping(), this.targetKey);
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(LinkedKeyBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("target", this.targetKey).toString();
    }
    
    public BindingImpl<T> withKey(final Key<T> key) {
        return new LinkedBindingImpl(this.getSource(), (Key<Object>)key, this.getScoping(), this.targetKey);
    }
    
    public BindingImpl<T> withScoping(final Scoping scoping) {
        return new LinkedBindingImpl(this.getSource(), this.getKey(), scoping, this.targetKey);
    }
}
