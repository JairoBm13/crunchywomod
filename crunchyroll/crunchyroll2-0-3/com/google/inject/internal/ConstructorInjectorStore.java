// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.Set;
import com.google.inject.spi.InjectionPoint;

final class ConstructorInjectorStore
{
    private final FailableCache<InjectionPoint, ConstructorInjector<?>> cache;
    private final InjectorImpl injector;
    
    ConstructorInjectorStore(final InjectorImpl injector) {
        this.cache = new FailableCache<InjectionPoint, ConstructorInjector<?>>() {
            @Override
            protected ConstructorInjector<?> create(final InjectionPoint injectionPoint, final Errors errors) throws ErrorsException {
                return ConstructorInjectorStore.this.createConstructor(injectionPoint, errors);
            }
        };
        this.injector = injector;
    }
    
    private <T> ConstructorInjector<T> createConstructor(final InjectionPoint injectionPoint, final Errors errors) throws ErrorsException {
        final int size = errors.size();
        final SingleParameterInjector<?>[] parametersInjectors = this.injector.getParametersInjectors(injectionPoint.getDependencies(), errors);
        final MembersInjectorImpl<?> value = this.injector.membersInjectorStore.get(injectionPoint.getDeclaringType(), errors);
        final DefaultConstructionProxyFactory<T> defaultConstructionProxyFactory = new DefaultConstructionProxyFactory<T>(injectionPoint);
        errors.throwIfNewErrors(size);
        return new ConstructorInjector<T>(value.getInjectionPoints(), defaultConstructionProxyFactory.create(), parametersInjectors, (MembersInjectorImpl<Object>)value);
    }
    
    public ConstructorInjector<?> get(final InjectionPoint injectionPoint, final Errors errors) throws ErrorsException {
        return this.cache.get(injectionPoint, errors);
    }
    
    boolean remove(final InjectionPoint injectionPoint) {
        return this.cache.remove(injectionPoint);
    }
}
