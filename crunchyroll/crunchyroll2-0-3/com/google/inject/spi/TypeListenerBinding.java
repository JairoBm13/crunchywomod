// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

public final class TypeListenerBinding implements Element
{
    private final TypeListener listener;
    private final Object source;
    private final Matcher<? super TypeLiteral<?>> typeMatcher;
    
    TypeListenerBinding(final Object source, final TypeListener listener, final Matcher<? super TypeLiteral<?>> typeMatcher) {
        this.source = source;
        this.listener = listener;
        this.typeMatcher = typeMatcher;
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).bindListener(this.typeMatcher, this.listener);
    }
    
    public TypeListener getListener() {
        return this.listener;
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
    
    public Matcher<? super TypeLiteral<?>> getTypeMatcher() {
        return this.typeMatcher;
    }
}
