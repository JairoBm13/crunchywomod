// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.Message;
import com.google.inject.ProvisionException;
import com.google.inject.spi.Dependency;
import com.google.inject.Provider;

final class ProviderToInternalFactoryAdapter<T> implements Provider<T>
{
    private final InjectorImpl injector;
    private final InternalFactory<? extends T> internalFactory;
    
    public ProviderToInternalFactoryAdapter(final InjectorImpl injector, final InternalFactory<? extends T> internalFactory) {
        this.injector = injector;
        this.internalFactory = internalFactory;
    }
    
    @Override
    public T get() {
        final Errors errors = new Errors();
        try {
            final T callInContext = this.injector.callInContext((ContextualCallable<T>)new ContextualCallable<T>() {
                @Override
                public T call(final InternalContext internalContext) throws ErrorsException {
                    return ProviderToInternalFactoryAdapter.this.internalFactory.get(errors, internalContext, internalContext.getDependency(), true);
                }
            });
            errors.throwIfNewErrors(0);
            return callInContext;
        }
        catch (ErrorsException ex) {
            throw new ProvisionException(errors.merge(ex.getErrors()).getMessages());
        }
    }
    
    @Override
    public String toString() {
        return this.internalFactory.toString();
    }
}
