// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import com.google.inject.spi.InjectionPoint;

final class DefaultConstructionProxyFactory<T> implements ConstructionProxyFactory<T>
{
    private final InjectionPoint injectionPoint;
    
    DefaultConstructionProxyFactory(final InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }
    
    @Override
    public ConstructionProxy<T> create() {
        final Constructor constructor = (Constructor)this.injectionPoint.getMember();
        if (Modifier.isPublic(constructor.getModifiers())) {
            if (!Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
                constructor.setAccessible(true);
            }
        }
        else {
            constructor.setAccessible(true);
        }
        return new ConstructionProxy<T>() {
            @Override
            public InjectionPoint getInjectionPoint() {
                return DefaultConstructionProxyFactory.this.injectionPoint;
            }
            
            @Override
            public T newInstance(final Object... array) throws InvocationTargetException {
                try {
                    return constructor.newInstance(array);
                }
                catch (InstantiationException ex) {
                    throw new AssertionError((Object)ex);
                }
                catch (IllegalAccessException ex2) {
                    throw new AssertionError((Object)ex2);
                }
            }
        };
    }
}
