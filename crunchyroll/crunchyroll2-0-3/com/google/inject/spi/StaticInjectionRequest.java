// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.ConfigurationException;
import java.util.Set;
import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;

public final class StaticInjectionRequest implements Element
{
    private final Object source;
    private final Class<?> type;
    
    StaticInjectionRequest(final Object o, final Class<?> clazz) {
        this.source = $Preconditions.checkNotNull(o, "source");
        this.type = $Preconditions.checkNotNull(clazz, "type");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).requestStaticInjection(this.type);
    }
    
    public Set<InjectionPoint> getInjectionPoints() throws ConfigurationException {
        return InjectionPoint.forStaticMethodsAndFields(this.type);
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public Class<?> getType() {
        return this.type;
    }
}
