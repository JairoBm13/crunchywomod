// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.TypeLiteral;
import com.google.inject.MembersInjector;

public final class MembersInjectorLookup<T> implements Element
{
    private MembersInjector<T> delegate;
    private final Object source;
    private final TypeLiteral<T> type;
    
    public MembersInjectorLookup(final Object o, final TypeLiteral<T> typeLiteral) {
        this.source = $Preconditions.checkNotNull(o, "source");
        this.type = $Preconditions.checkNotNull(typeLiteral, "type");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit((MembersInjectorLookup<Object>)this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        this.initializeDelegate(binder.withSource(this.getSource()).getMembersInjector(this.type));
    }
    
    public MembersInjector<T> getMembersInjector() {
        return new MembersInjector<T>() {
            @Override
            public void injectMembers(final T t) {
                $Preconditions.checkState(MembersInjectorLookup.this.delegate != null, (Object)"This MembersInjector cannot be used until the Injector has been created.");
                MembersInjectorLookup.this.delegate.injectMembers(t);
            }
            
            @Override
            public String toString() {
                return "MembersInjector<" + MembersInjectorLookup.this.type + ">";
            }
        };
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public TypeLiteral<T> getType() {
        return this.type;
    }
    
    public void initializeDelegate(final MembersInjector<T> membersInjector) {
        $Preconditions.checkState(this.delegate == null, (Object)"delegate already initialized");
        this.delegate = $Preconditions.checkNotNull(membersInjector, "delegate");
    }
}
