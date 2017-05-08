// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Binding;

public interface ConstructorBinding<T> extends Binding<T>, HasDependencies
{
    InjectionPoint getConstructor();
}
