// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.Key;
import com.google.inject.Provider;

public final class ProviderLookup<T> implements Element
{
    private Provider<T> delegate;
    private final Key<T> key;
    private final Object source;
    
    public ProviderLookup(final Object o, final Key<T> key) {
        this.source = $Preconditions.checkNotNull(o, "source");
        this.key = $Preconditions.checkNotNull(key, "key");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit((ProviderLookup<Object>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.initializeDelegate(binder.withSource(this.getSource()).getProvider(this.key));
    }
    
    public Key<T> getKey() {
        return this.key;
    }
    
    public Provider<T> getProvider() {
        return new Provider<T>() {
            @Override
            public T get() {
                $Preconditions.checkState(ProviderLookup.this.delegate != null, (Object)"This Provider cannot be used until the Injector has been created.");
                return ProviderLookup.this.delegate.get();
            }
            
            @Override
            public String toString() {
                return "Provider<" + ProviderLookup.this.key.getTypeLiteral() + ">";
            }
        };
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public void initializeDelegate(final Provider<T> provider) {
        $Preconditions.checkState(this.delegate == null, (Object)"delegate already initialized");
        this.delegate = $Preconditions.checkNotNull(provider, "delegate");
    }
}
