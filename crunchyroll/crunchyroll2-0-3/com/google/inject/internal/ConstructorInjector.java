// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.internal.util.$ImmutableSet;

final class ConstructorInjector<T>
{
    private final ConstructionProxy<T> constructionProxy;
    private final $ImmutableSet<InjectionPoint> injectableMembers;
    private final MembersInjectorImpl<T> membersInjector;
    private final SingleParameterInjector<?>[] parameterInjectors;
    
    ConstructorInjector(final Set<InjectionPoint> set, final ConstructionProxy<T> constructionProxy, final SingleParameterInjector<?>[] parameterInjectors, final MembersInjectorImpl<T> membersInjector) {
        this.injectableMembers = $ImmutableSet.copyOf((Iterable<? extends InjectionPoint>)set);
        this.constructionProxy = constructionProxy;
        this.parameterInjectors = parameterInjectors;
        this.membersInjector = membersInjector;
    }
    
    Object construct(final Errors errors, InternalContext cause, final Class<?> clazz, final boolean b) throws ErrorsException {
        final ConstructionContext<T> constructionContext = (ConstructionContext<T>)((InternalContext)cause).getConstructionContext(this);
        Object o;
        if (constructionContext.isConstructing()) {
            if (!b) {
                throw errors.circularProxiesDisabled(clazz).toException();
            }
            o = constructionContext.createProxy(errors, clazz);
        }
        else if ((o = constructionContext.getCurrentReference()) == null) {
            while (true) {
                while (true) {
                    Label_0174: {
                        try {
                            constructionContext.startConstruction();
                            try {
                                final T instance = this.constructionProxy.newInstance(SingleParameterInjector.getAll(errors, (InternalContext)cause, this.parameterInjectors));
                                constructionContext.setProxyDelegates(instance);
                                constructionContext.finishConstruction();
                                constructionContext.setCurrentReference(instance);
                                this.membersInjector.injectMembers(instance, errors, (InternalContext)cause, false);
                                this.membersInjector.notifyListeners(instance, errors);
                                return instance;
                            }
                            finally {
                                constructionContext.finishConstruction();
                            }
                        }
                        catch (InvocationTargetException cause) {
                            try {
                                if (cause.getCause() != null) {
                                    cause = (InvocationTargetException)cause.getCause();
                                    throw errors.withSource(this.constructionProxy.getInjectionPoint()).errorInjectingConstructor(cause).toException();
                                }
                                break Label_0174;
                            }
                            finally {
                                constructionContext.removeCurrentReference();
                            }
                        }
                    }
                    continue;
                }
            }
        }
        return o;
    }
    
    ConstructionProxy<T> getConstructionProxy() {
        return this.constructionProxy;
    }
    
    public $ImmutableSet<InjectionPoint> getInjectableMembers() {
        return this.injectableMembers;
    }
}
