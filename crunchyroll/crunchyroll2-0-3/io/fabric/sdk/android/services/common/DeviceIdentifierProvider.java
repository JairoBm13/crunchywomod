// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import java.util.Map;

public interface DeviceIdentifierProvider
{
    Map<IdManager.DeviceIdentifierType, String> getDeviceIdentifiers();
}
