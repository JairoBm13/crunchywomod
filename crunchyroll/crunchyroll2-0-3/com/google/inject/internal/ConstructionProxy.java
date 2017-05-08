// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.InvocationTargetException;
import com.google.inject.spi.InjectionPoint;

interface ConstructionProxy<T>
{
    InjectionPoint getInjectionPoint();
    
    T newInstance(final Object... p0) throws InvocationTargetException;
}
