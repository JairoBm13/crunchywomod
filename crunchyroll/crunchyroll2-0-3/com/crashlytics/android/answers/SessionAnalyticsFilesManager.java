// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.util.UUID;
import java.io.IOException;
import io.fabric.sdk.android.services.events.EventTransform;
import io.fabric.sdk.android.services.events.EventsStorage;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import android.content.Context;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import io.fabric.sdk.android.services.events.EventsFilesManager;

class SessionAnalyticsFilesManager extends EventsFilesManager<SessionEvent>
{
    private AnalyticsSettingsData analyticsSettingsData;
    
    SessionAnalyticsFilesManager(final Context context, final SessionEventTransform sessionEventTransform, final CurrentTimeProvider currentTimeProvider, final EventsStorage eventsStorage) throws IOException {
        super(context, sessionEventTransform, currentTimeProvider, eventsStorage, 100);
    }
    
    @Override
    protected String generateUniqueRollOverFileName() {
        return "sa" + "_" + UUID.randomUUID().toString() + "_" + this.currentTimeProvider.getCurrentTimeMillis() + ".tap";
    }
    
    @Override
    protected int getMaxByteSizePerFile() {
        if (this.analyticsSettingsData == null) {
            return super.getMaxByteSizePerFile();
        }
        return this.analyticsSettingsData.maxByteSizePerFile;
    }
    
    @Override
    protected int getMaxFilesToKeep() {
        if (this.analyticsSettingsData == null) {
            return super.getMaxFilesToKeep();
        }
        return this.analyticsSettingsData.maxPendingSendFileCount;
    }
    
    void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData) {
        this.analyticsSettingsData = analyticsSettingsData;
    }
}
