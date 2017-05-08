// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.cache;

import android.content.Context;

public interface ValueCache<T>
{
    T get(final Context p0, final ValueLoader<T> p1) throws Exception;
}
