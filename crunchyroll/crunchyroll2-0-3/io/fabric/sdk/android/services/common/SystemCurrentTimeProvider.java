// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

public class SystemCurrentTimeProvider implements CurrentTimeProvider
{
    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
