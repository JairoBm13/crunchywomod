// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

class DelegatingInvocationHandler<T> implements InvocationHandler
{
    private T delegate;
    
    @Override
    public Object invoke(Object invoke, final Method method, final Object[] array) throws Throwable {
        if (this.delegate == null) {
            throw new IllegalStateException("This is a proxy used to support circular references involving constructors. The object we're proxying is not constructed yet. Please wait until after injection has completed to use this object.");
        }
        try {
            invoke = method.invoke(this.delegate, array);
            return invoke;
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        catch (IllegalArgumentException ex2) {
            throw new RuntimeException(ex2);
        }
        catch (InvocationTargetException ex3) {
            throw ex3.getTargetException();
        }
    }
    
    void setDelegate(final T delegate) {
        this.delegate = delegate;
    }
}
