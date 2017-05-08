// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.spi.Dependency;

interface InternalFactory<T>
{
    T get(final Errors p0, final InternalContext p1, final Dependency<?> p2, final boolean p3) throws ErrorsException;
}
