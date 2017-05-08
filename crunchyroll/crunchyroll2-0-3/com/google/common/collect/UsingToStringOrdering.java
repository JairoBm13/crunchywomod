// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.io.Serializable;

final class UsingToStringOrdering extends Ordering<Object> implements Serializable
{
    static final UsingToStringOrdering INSTANCE;
    
    static {
        INSTANCE = new UsingToStringOrdering();
    }
    
    @Override
    public int compare(final Object o, final Object o2) {
        return o.toString().compareTo(o2.toString());
    }
    
    @Override
    public String toString() {
        return "Ordering.usingToString()";
    }
}
