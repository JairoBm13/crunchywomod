// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.ConfigurationException;
import java.util.Set;
import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.TypeLiteral;

public final class InjectionRequest<T> implements Element
{
    private final T instance;
    private final Object source;
    private final TypeLiteral<T> type;
    
    public InjectionRequest(final Object o, final TypeLiteral<T> typeLiteral, final T t) {
        this.source = $Preconditions.checkNotNull(o, "source");
        this.type = $Preconditions.checkNotNull(typeLiteral, "type");
        this.instance = $Preconditions.checkNotNull(t, "instance");
    }
    
    @Override
    public <R> R acceptVisitor(final ElementVisitor<R> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).requestInjection(this.type, this.instance);
    }
    
    public Set<InjectionPoint> getInjectionPoints() throws ConfigurationException {
        return InjectionPoint.forInstanceMethodsAndFields(this.instance.getClass());
    }
    
    public T getInstance() {
        return this.instance;
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
}
