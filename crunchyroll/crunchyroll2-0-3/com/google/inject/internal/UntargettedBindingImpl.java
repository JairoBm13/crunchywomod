// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.internal.util.$ToStringBuilder;
import com.google.inject.internal.util.$Objects;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.Binder;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.Key;
import com.google.inject.spi.UntargettedBinding;

final class UntargettedBindingImpl<T> extends BindingImpl<T> implements UntargettedBinding<T>
{
    UntargettedBindingImpl(final InjectorImpl injectorImpl, final Key<T> key, final Object o) {
        super(injectorImpl, key, o, (InternalFactory<? extends T>)new InternalFactory<T>() {
            @Override
            public T get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) {
                throw new AssertionError();
            }
        }, Scoping.UNSCOPED);
    }
    
    public UntargettedBindingImpl(final Object o, final Key<T> key, final Scoping scoping) {
        super(o, key, scoping);
    }
    
    @Override
    public <V> V acceptTargetVisitor(final BindingTargetVisitor<? super T, V> bindingTargetVisitor) {
        return bindingTargetVisitor.visit((UntargettedBinding<? extends T>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()));
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof UntargettedBindingImpl) {
            final UntargettedBindingImpl untargettedBindingImpl = (UntargettedBindingImpl)o;
            b2 = b;
            if (this.getKey().equals(untargettedBindingImpl.getKey())) {
                b2 = b;
                if (this.getScoping().equals(untargettedBindingImpl.getScoping())) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.getKey(), this.getScoping());
    }
    
    @Override
    public String toString() {
        return new $ToStringBuilder(UntargettedBinding.class).add("key", this.getKey()).add("source", this.getSource()).toString();
    }
    
    public BindingImpl<T> withKey(final Key<T> key) {
        return new UntargettedBindingImpl(this.getSource(), (Key<Object>)key, this.getScoping());
    }
    
    public BindingImpl<T> withScoping(final Scoping scoping) {
        return new UntargettedBindingImpl(this.getSource(), this.getKey(), scoping);
    }
}
