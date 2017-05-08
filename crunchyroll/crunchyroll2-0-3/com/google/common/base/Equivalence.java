// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.io.Serializable;

public abstract class Equivalence<T>
{
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }
    
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }
    
    protected abstract boolean doEquivalent(final T p0, final T p1);
    
    protected abstract int doHash(final T p0);
    
    public final boolean equivalent(final T t, final T t2) {
        return t == t2 || (t != null && t2 != null && this.doEquivalent(t, t2));
    }
    
    public final int hash(final T t) {
        if (t == null) {
            return 0;
        }
        return this.doHash(t);
    }
    
    static final class Equals extends Equivalence<Object> implements Serializable
    {
        static final Equals INSTANCE;
        
        static {
            INSTANCE = new Equals();
        }
        
        @Override
        protected boolean doEquivalent(final Object o, final Object o2) {
            return o.equals(o2);
        }
        
        public int doHash(final Object o) {
            return o.hashCode();
        }
    }
    
    static final class Identity extends Equivalence<Object> implements Serializable
    {
        static final Identity INSTANCE;
        
        static {
            INSTANCE = new Identity();
        }
        
        @Override
        protected boolean doEquivalent(final Object o, final Object o2) {
            return false;
        }
        
        @Override
        protected int doHash(final Object o) {
            return System.identityHashCode(o);
        }
    }
}
