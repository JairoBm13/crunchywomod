// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.util.Map;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.UUID;
import io.fabric.sdk.android.services.common.IdManager;
import android.content.Context;

class SessionMetadataCollector
{
    private final Context context;
    private final IdManager idManager;
    private final String versionCode;
    private final String versionName;
    
    public SessionMetadataCollector(final Context context, final IdManager idManager, final String versionCode, final String versionName) {
        this.context = context;
        this.idManager = idManager;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }
    
    public SessionEventMetadata getMetadata() {
        final Map<IdManager.DeviceIdentifierType, String> deviceIdentifiers = this.idManager.getDeviceIdentifiers();
        return new SessionEventMetadata(this.context.getPackageName(), UUID.randomUUID().toString(), this.idManager.getAppInstallIdentifier(), deviceIdentifiers.get(IdManager.DeviceIdentifierType.ANDROID_ID), deviceIdentifiers.get(IdManager.DeviceIdentifierType.ANDROID_ADVERTISING_ID), this.idManager.isLimitAdTrackingEnabled(), deviceIdentifiers.get(IdManager.DeviceIdentifierType.FONT_TOKEN), CommonUtils.resolveBuildId(this.context), this.idManager.getOsVersionString(), this.idManager.getModelName(), this.versionCode, this.versionName);
    }
}
