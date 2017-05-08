// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.Dependency;
import com.google.inject.Key;
import javax.inject.Provider;

final class BoundProviderFactory<T> implements CreationListener, InternalFactory<T>
{
    private final InjectorImpl injector;
    private InternalFactory<? extends Provider<? extends T>> providerFactory;
    final Key<? extends Provider<? extends T>> providerKey;
    final Object source;
    
    BoundProviderFactory(final InjectorImpl injector, final Key<? extends Provider<? extends T>> providerKey, final Object source) {
        this.injector = injector;
        this.providerKey = providerKey;
        this.source = source;
    }
    
    @Override
    public T get(Errors withSource, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) throws ErrorsException {
        withSource = withSource.withSource(this.providerKey);
        final Provider provider = (Provider)this.providerFactory.get(withSource, internalContext, dependency, true);
        try {
            return withSource.checkForNull((T)provider.get(), this.source, dependency);
        }
        catch (RuntimeException ex) {
            throw withSource.errorInProvider(ex).toException();
        }
    }
    
    @Override
    public void notify(final Errors errors) {
        try {
            this.providerFactory = this.injector.getInternalFactory(this.providerKey, errors.withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
        }
        catch (ErrorsException ex) {
            errors.merge(ex.getErrors());
        }
    }
    
    @Override
    public String toString() {
        return this.providerKey.toString();
    }
}
