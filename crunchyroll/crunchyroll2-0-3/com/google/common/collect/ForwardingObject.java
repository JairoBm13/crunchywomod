// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

public abstract class ForwardingObject
{
    protected abstract Object delegate();
    
    @Override
    public String toString() {
        return this.delegate().toString();
    }
}
