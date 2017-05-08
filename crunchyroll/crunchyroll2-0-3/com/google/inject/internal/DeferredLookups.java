// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.ProviderLookup;
import com.google.inject.Provider;
import com.google.inject.Key;
import com.google.inject.internal.util.$Lists;
import com.google.inject.spi.Element;
import java.util.List;

final class DeferredLookups implements Lookups
{
    private final InjectorImpl injector;
    private final List<Element> lookups;
    
    DeferredLookups(final InjectorImpl injector) {
        this.lookups = (List<Element>)$Lists.newArrayList();
        this.injector = injector;
    }
    
    @Override
    public <T> Provider<T> getProvider(final Key<T> key) {
        final ProviderLookup<T> providerLookup = new ProviderLookup<T>(key, key);
        this.lookups.add(providerLookup);
        return providerLookup.getProvider();
    }
    
    void initialize(final Errors errors) {
        this.injector.lookups = this.injector;
        new LookupProcessor(errors).process(this.injector, this.lookups);
    }
}
