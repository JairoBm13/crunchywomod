// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Provider;
import com.google.inject.Key;

interface Lookups
{
     <T> Provider<T> getProvider(final Key<T> p0);
}
