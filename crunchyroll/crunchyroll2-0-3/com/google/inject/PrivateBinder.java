// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject;

public interface PrivateBinder extends Binder
{
    void expose(final Key<?> p0);
    
    PrivateBinder withSource(final Object p0);
}
