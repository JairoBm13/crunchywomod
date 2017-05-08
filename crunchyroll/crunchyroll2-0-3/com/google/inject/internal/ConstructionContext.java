// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import java.util.Iterator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

final class ConstructionContext<T>
{
    boolean constructing;
    T currentReference;
    List<DelegatingInvocationHandler<T>> invocationHandlers;
    
    public Object createProxy(final Errors errors, final Class<?> clazz) throws ErrorsException {
        if (!clazz.isInterface()) {
            throw errors.cannotSatisfyCircularDependency(clazz).toException();
        }
        if (this.invocationHandlers == null) {
            this.invocationHandlers = new ArrayList<DelegatingInvocationHandler<T>>();
        }
        final DelegatingInvocationHandler<T> delegatingInvocationHandler = new DelegatingInvocationHandler<T>();
        this.invocationHandlers.add(delegatingInvocationHandler);
        return clazz.cast(Proxy.newProxyInstance(BytecodeGen.getClassLoader(clazz), new Class[] { clazz, CircularDependencyProxy.class }, delegatingInvocationHandler));
    }
    
    public void finishConstruction() {
        this.constructing = false;
        this.invocationHandlers = null;
    }
    
    public T getCurrentReference() {
        return this.currentReference;
    }
    
    public boolean isConstructing() {
        return this.constructing;
    }
    
    public void removeCurrentReference() {
        this.currentReference = null;
    }
    
    public void setCurrentReference(final T currentReference) {
        this.currentReference = currentReference;
    }
    
    public void setProxyDelegates(final T delegate) {
        if (this.invocationHandlers != null) {
            final Iterator<DelegatingInvocationHandler<T>> iterator = this.invocationHandlers.iterator();
            while (iterator.hasNext()) {
                iterator.next().setDelegate(delegate);
            }
        }
    }
    
    public void startConstruction() {
        this.constructing = true;
    }
}
