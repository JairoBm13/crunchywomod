// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.cache;

import android.content.Context;

public interface ValueLoader<T>
{
    T load(final Context p0) throws Exception;
}
