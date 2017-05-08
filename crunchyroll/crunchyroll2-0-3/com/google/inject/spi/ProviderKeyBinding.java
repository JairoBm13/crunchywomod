// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import javax.inject.Provider;
import com.google.inject.Key;
import com.google.inject.Binding;

public interface ProviderKeyBinding<T> extends Binding<T>
{
    Key<? extends Provider<? extends T>> getProviderKey();
}
