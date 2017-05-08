// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.ProviderLookup;
import com.google.inject.MembersInjector;
import com.google.inject.spi.MembersInjectorLookup;

final class LookupProcessor extends AbstractProcessor
{
    LookupProcessor(final Errors errors) {
        super(errors);
    }
    
    @Override
    public <T> Boolean visit(final MembersInjectorLookup<T> membersInjectorLookup) {
        try {
            membersInjectorLookup.initializeDelegate(this.injector.membersInjectorStore.get(membersInjectorLookup.getType(), this.errors));
            return true;
        }
        catch (ErrorsException ex) {
            this.errors.merge(ex.getErrors());
            return true;
        }
    }
    
    @Override
    public <T> Boolean visit(final ProviderLookup<T> providerLookup) {
        try {
            providerLookup.initializeDelegate(this.injector.getProviderOrThrow(providerLookup.getKey(), this.errors));
            return true;
        }
        catch (ErrorsException ex) {
            this.errors.merge(ex.getErrors());
            return true;
        }
    }
}
