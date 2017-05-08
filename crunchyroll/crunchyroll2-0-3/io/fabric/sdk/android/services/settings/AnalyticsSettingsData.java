// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

public class AnalyticsSettingsData
{
    public final String analyticsURL;
    public final int flushIntervalSeconds;
    public final int maxByteSizePerFile;
    public final int maxFileCountPerSend;
    public final int maxPendingSendFileCount;
    public final int samplingRate;
    public final boolean trackCustomEvents;
    public final boolean trackPredefinedEvents;
    
    public AnalyticsSettingsData(final String analyticsURL, final int flushIntervalSeconds, final int maxByteSizePerFile, final int maxFileCountPerSend, final int maxPendingSendFileCount, final boolean trackCustomEvents, final boolean trackPredefinedEvents, final int samplingRate) {
        this.analyticsURL = analyticsURL;
        this.flushIntervalSeconds = flushIntervalSeconds;
        this.maxByteSizePerFile = maxByteSizePerFile;
        this.maxFileCountPerSend = maxFileCountPerSend;
        this.maxPendingSendFileCount = maxPendingSendFileCount;
        this.trackCustomEvents = trackCustomEvents;
        this.trackPredefinedEvents = trackPredefinedEvents;
        this.samplingRate = samplingRate;
    }
}
