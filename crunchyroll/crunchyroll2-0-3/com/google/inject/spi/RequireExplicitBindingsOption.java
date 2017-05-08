// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binder;
import com.google.inject.internal.util.$Preconditions;

public final class RequireExplicitBindingsOption implements Element
{
    private final Object source;
    
    RequireExplicitBindingsOption(final Object o) {
        this.source = $Preconditions.checkNotNull(o, "source");
    }
    
    @Override
    public <T> T acceptVisitor(final ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }
    
    @Override
    public void applyTo(final Binder binder) {
        binder.withSource(this.getSource()).requireExplicitBindings();
    }
    
    @Override
    public Object getSource() {
        return this.source;
    }
}
