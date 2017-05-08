// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import java.lang.annotation.Annotation;
import com.google.inject.Scope;

public class DefaultBindingScopingVisitor<V> implements BindingScopingVisitor<V>
{
    @Override
    public V visitEagerSingleton() {
        return this.visitOther();
    }
    
    @Override
    public V visitNoScoping() {
        return this.visitOther();
    }
    
    protected V visitOther() {
        return null;
    }
    
    @Override
    public V visitScope(final Scope scope) {
        return this.visitOther();
    }
    
    @Override
    public V visitScopeAnnotation(final Class<? extends Annotation> clazz) {
        return this.visitOther();
    }
}
