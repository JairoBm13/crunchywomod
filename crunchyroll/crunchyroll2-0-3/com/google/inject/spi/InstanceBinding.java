// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import java.util.Set;
import com.google.inject.Binding;

public interface InstanceBinding<T> extends Binding<T>, HasDependencies
{
    Set<InjectionPoint> getInjectionPoints();
    
    T getInstance();
}
