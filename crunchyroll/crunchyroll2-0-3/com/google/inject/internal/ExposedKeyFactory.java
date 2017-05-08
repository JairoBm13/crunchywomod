// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.Dependency;
import com.google.inject.spi.PrivateElements;
import com.google.inject.Key;

final class ExposedKeyFactory<T> implements CreationListener, InternalFactory<T>
{
    private BindingImpl<T> delegate;
    private final Key<T> key;
    private final PrivateElements privateElements;
    
    ExposedKeyFactory(final Key<T> key, final PrivateElements privateElements) {
        this.key = key;
        this.privateElements = privateElements;
    }
    
    @Override
    public T get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) throws ErrorsException {
        return (T)this.delegate.getInternalFactory().get(errors, internalContext, dependency, b);
    }
    
    @Override
    public void notify(final Errors errors) {
        final BindingImpl<T> explicitBinding = ((InjectorImpl)this.privateElements.getInjector()).state.getExplicitBinding(this.key);
        if (explicitBinding.getInternalFactory() == this) {
            errors.withSource(explicitBinding.getSource()).exposedButNotBound(this.key);
            return;
        }
        this.delegate = explicitBinding;
    }
}
