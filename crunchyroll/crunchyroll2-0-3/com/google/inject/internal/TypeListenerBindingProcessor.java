// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.TypeListenerBinding;

final class TypeListenerBindingProcessor extends AbstractProcessor
{
    TypeListenerBindingProcessor(final Errors errors) {
        super(errors);
    }
    
    @Override
    public Boolean visit(final TypeListenerBinding typeListenerBinding) {
        this.injector.state.addTypeListener(typeListenerBinding);
        return true;
    }
}
