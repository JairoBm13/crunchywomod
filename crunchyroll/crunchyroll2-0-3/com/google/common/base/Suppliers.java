// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.io.Serializable;

public final class Suppliers
{
    public static <T> Supplier<T> ofInstance(final T t) {
        return new SupplierOfInstance<T>(t);
    }
    
    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable
    {
        final T instance;
        
        SupplierOfInstance(final T instance) {
            this.instance = instance;
        }
        
        @Override
        public T get() {
            return this.instance;
        }
        
        @Override
        public String toString() {
            return "Suppliers.ofInstance(" + this.instance + ")";
        }
    }
}
