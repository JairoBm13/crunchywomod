// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import java.io.IOException;

public interface EventTransform<T>
{
    byte[] toBytes(final T p0) throws IOException;
}
