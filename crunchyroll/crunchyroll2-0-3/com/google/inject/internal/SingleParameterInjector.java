// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.Dependency;

final class SingleParameterInjector<T>
{
    private static final Object[] NO_ARGUMENTS;
    private final Dependency<T> dependency;
    private final InternalFactory<? extends T> factory;
    
    static {
        NO_ARGUMENTS = new Object[0];
    }
    
    SingleParameterInjector(final Dependency<T> dependency, final InternalFactory<? extends T> factory) {
        this.dependency = dependency;
        this.factory = factory;
    }
    
    static Object[] getAll(final Errors errors, final InternalContext internalContext, final SingleParameterInjector<?>[] array) throws ErrorsException {
        if (array == null) {
            return SingleParameterInjector.NO_ARGUMENTS;
        }
        final int size = errors.size();
        final int length = array.length;
        final Object[] array2 = new Object[length];
        int i = 0;
    Label_0049_Outer:
        while (i < length) {
            final SingleParameterInjector<?> singleParameterInjector = array[i];
            while (true) {
                try {
                    array2[i] = singleParameterInjector.inject(errors, internalContext);
                    ++i;
                    continue Label_0049_Outer;
                }
                catch (ErrorsException ex) {
                    errors.merge(ex.getErrors());
                    continue;
                }
                break;
            }
            break;
        }
        errors.throwIfNewErrors(size);
        return array2;
    }
    
    private T inject(final Errors errors, final InternalContext internalContext) throws ErrorsException {
        final Dependency setDependency = internalContext.setDependency(this.dependency);
        try {
            return (T)this.factory.get(errors.withSource(this.dependency), internalContext, this.dependency, false);
        }
        finally {
            internalContext.setDependency(setDependency);
        }
    }
}
