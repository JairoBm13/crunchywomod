// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Provider;
import java.util.Set;
import com.google.inject.Binding;

public interface ProviderInstanceBinding<T> extends Binding<T>, HasDependencies
{
    Set<InjectionPoint> getInjectionPoints();
    
    Provider<? extends T> getProviderInstance();
}
