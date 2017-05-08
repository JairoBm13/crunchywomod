// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;

public final class ScopeBinding implements Element
{
    private final Class<? extends Annotation> annotationType;
    private final Scope scope;
    private final Object source;
    
    ScopeBinding(final Object o, final Class<? extends Annotation> clazz, final Scope scope) {
        this.source = $Preconditions.checkNotNull(o, "source");
        this.annotationType = $Preconditions.checkNotNull(clazz, "annotationType");
        this.scope = $Preconditions.checkNotNull(scope, "scope");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).bindScope(this.annotationType, this.scope);
    }
    
    public Class<? extends Annotation> getAnnotationType() {
        return this.annotationType;
    }
    
    public Scope getScope() {
        return this.scope;
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
}
