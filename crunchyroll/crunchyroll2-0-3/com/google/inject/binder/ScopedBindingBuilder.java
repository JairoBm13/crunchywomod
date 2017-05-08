// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.binder;

import java.lang.annotation.Annotation;
import com.google.inject.Scope;

public interface ScopedBindingBuilder
{
    void asEagerSingleton();
    
    void in(final Scope p0);
    
    void in(final Class<? extends Annotation> p0);
}
