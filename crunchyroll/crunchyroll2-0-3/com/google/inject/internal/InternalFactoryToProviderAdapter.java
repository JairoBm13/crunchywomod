// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.Dependency;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.Provider;

final class InternalFactoryToProviderAdapter<T> implements InternalFactory<T>
{
    private final Initializable<Provider<? extends T>> initializable;
    private final Object source;
    
    public InternalFactoryToProviderAdapter(final Initializable<Provider<? extends T>> initializable, final Object o) {
        this.initializable = $Preconditions.checkNotNull(initializable, "provider");
        this.source = $Preconditions.checkNotNull(o, "source");
    }
    
    @Override
    public T get(final Errors errors, final InternalContext internalContext, final Dependency<?> dependency, final boolean b) throws ErrorsException {
        try {
            return errors.checkForNull((T)this.initializable.get(errors).get(), this.source, dependency);
        }
        catch (RuntimeException ex) {
            throw errors.withSource(this.source).errorInProvider(ex).toException();
        }
    }
    
    @Override
    public String toString() {
        return this.initializable.toString();
    }
}
