// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.Member;
import com.google.inject.internal.util.$StackTraceElements;
import com.google.inject.internal.util.$Objects;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import com.google.inject.PrivateBinder;
import com.google.inject.Binder;
import com.google.inject.Exposed;
import java.lang.annotation.Annotation;
import com.google.inject.Provider;
import java.util.List;
import java.lang.reflect.Method;
import com.google.inject.Key;
import com.google.inject.spi.Dependency;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.spi.ProviderWithDependencies;

public class ProviderMethod<T> implements ProviderWithDependencies<T>
{
    private final $ImmutableSet<Dependency<?>> dependencies;
    private final boolean exposed;
    private final Object instance;
    private final Key<T> key;
    private final Method method;
    private final List<Provider<?>> parameterProviders;
    private final Class<? extends Annotation> scopeAnnotation;
    
    ProviderMethod(final Key<T> key, final Method method, final Object instance, final $ImmutableSet<Dependency<?>> dependencies, final List<Provider<?>> parameterProviders, final Class<? extends Annotation> scopeAnnotation) {
        this.key = key;
        this.scopeAnnotation = scopeAnnotation;
        this.instance = instance;
        this.dependencies = dependencies;
        this.method = method;
        this.parameterProviders = parameterProviders;
        this.exposed = method.isAnnotationPresent(Exposed.class);
        method.setAccessible(true);
    }
    
    public void configure(Binder withSource) {
        withSource = withSource.withSource(this.method);
        if (this.scopeAnnotation != null) {
            withSource.bind(this.key).toProvider((Provider<? extends T>)this).in(this.scopeAnnotation);
        }
        else {
            withSource.bind(this.key).toProvider((Provider<? extends T>)this);
        }
        if (this.exposed) {
            ((PrivateBinder)withSource).expose(this.key);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof ProviderMethod) {
            final ProviderMethod providerMethod = (ProviderMethod)o;
            b2 = b;
            if (this.method.equals(providerMethod.method)) {
                b2 = b;
                if (this.instance.equals(providerMethod.instance)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public T get() {
        final Object[] array = new Object[this.parameterProviders.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.parameterProviders.get(i).get();
        }
        try {
            return (T)this.method.invoke(this.instance, array);
        }
        catch (IllegalAccessException ex) {
            throw new AssertionError((Object)ex);
        }
        catch (InvocationTargetException ex2) {
            throw Exceptions.throwCleanly(ex2);
        }
    }
    
    @Override
    public Set<Dependency<?>> getDependencies() {
        return this.dependencies;
    }
    
    @Override
    public int hashCode() {
        return $Objects.hashCode(this.method);
    }
    
    @Override
    public String toString() {
        return "@Provides " + $StackTraceElements.forMember(this.method).toString();
    }
}
