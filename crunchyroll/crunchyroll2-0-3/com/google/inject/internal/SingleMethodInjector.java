// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.google.inject.spi.InjectionPoint;

final class SingleMethodInjector implements SingleMemberInjector
{
    private final InjectionPoint injectionPoint;
    private final InjectorImpl.MethodInvoker methodInvoker;
    private final SingleParameterInjector<?>[] parameterInjectors;
    
    SingleMethodInjector(final InjectorImpl injectorImpl, final InjectionPoint injectionPoint, final Errors errors) throws ErrorsException {
        this.injectionPoint = injectionPoint;
        this.methodInvoker = this.createMethodInvoker((Method)injectionPoint.getMember());
        this.parameterInjectors = injectorImpl.getParametersInjectors(injectionPoint.getDependencies(), errors);
    }
    
    private InjectorImpl.MethodInvoker createMethodInvoker(final Method method) {
        final int modifiers = method.getModifiers();
        if (Modifier.isPrivate(modifiers) || !Modifier.isProtected(modifiers)) {}
        if (!Modifier.isPublic(modifiers) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
        return new InjectorImpl.MethodInvoker() {
            @Override
            public Object invoke(final Object o, final Object... array) throws IllegalAccessException, InvocationTargetException {
                return method.invoke(o, array);
            }
        };
    }
    
    @Override
    public InjectionPoint getInjectionPoint() {
        return this.injectionPoint;
    }
    
    @Override
    public void inject(final Errors errors, final InternalContext internalContext, final Object o) {
        Object[] all;
        try {
            all = SingleParameterInjector.getAll(errors, internalContext, this.parameterInjectors);
            final SingleMethodInjector singleMethodInjector = this;
            final InjectorImpl.MethodInvoker methodInvoker = singleMethodInjector.methodInvoker;
            final Object o2 = o;
            final Object[] array = all;
            methodInvoker.invoke(o2, array);
            return;
        }
        catch (ErrorsException ex) {
            errors.merge(ex.getErrors());
            return;
        }
        try {
            final SingleMethodInjector singleMethodInjector = this;
            final InjectorImpl.MethodInvoker methodInvoker = singleMethodInjector.methodInvoker;
            final Object o2 = o;
            final Object[] array = all;
            methodInvoker.invoke(o2, array);
        }
        catch (IllegalAccessException ex2) {
            throw new AssertionError((Object)ex2);
        }
        catch (InvocationTargetException cause) {
            if (((InvocationTargetException)cause).getCause() != null) {
                cause = ((InvocationTargetException)cause).getCause();
            }
            errors.withSource(this.injectionPoint).errorInjectingMethod(cause);
        }
    }
}
