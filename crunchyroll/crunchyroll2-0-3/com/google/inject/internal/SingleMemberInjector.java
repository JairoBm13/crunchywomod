// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.InjectionPoint;

interface SingleMemberInjector
{
    InjectionPoint getInjectionPoint();
    
    void inject(final Errors p0, final InternalContext p1, final Object p2);
}
