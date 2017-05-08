// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Key;
import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedElementBuilder;

public class ExposureBuilder<T> implements AnnotatedElementBuilder
{
    private final Binder binder;
    private Key<T> key;
    private final Object source;
    
    public ExposureBuilder(final Binder binder, final Object source, final Key<T> key) {
        this.binder = binder;
        this.source = source;
        this.key = key;
    }
    
    public Key<?> getKey() {
        return this.key;
    }
    
    public Object getSource() {
        return this.source;
    }
    
    @Override
    public String toString() {
        return "AnnotatedElementBuilder";
    }
}
