// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import com.google.inject.Provider;

public interface ProviderWithExtensionVisitor<T> extends Provider<T>
{
     <B, V> V acceptExtensionVisitor(final BindingTargetVisitor<B, V> p0, final ProviderInstanceBinding<? extends B> p1);
}
