// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

final class Initializables
{
    static <T> Initializable<T> of(final T t) {
        return new Initializable<T>() {
            @Override
            public T get(final Errors errors) throws ErrorsException {
                return t;
            }
            
            @Override
            public String toString() {
                return String.valueOf(t);
            }
        };
    }
}
