// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import io.fabric.sdk.android.Logger;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.concurrent.ScheduledExecutorService;
import android.content.Context;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.events.FilesSender;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.events.EnabledEventsStrategy;

class EnabledSessionAnalyticsManagerStrategy extends EnabledEventsStrategy<SessionEvent> implements SessionAnalyticsManagerStrategy<SessionEvent>
{
    ApiKey apiKey;
    boolean customEventsEnabled;
    EventFilter eventFilter;
    FilesSender filesSender;
    private final HttpRequestFactory httpRequestFactory;
    private final Kit kit;
    final SessionEventMetadata metadata;
    boolean predefinedEventsEnabled;
    
    public EnabledSessionAnalyticsManagerStrategy(final Kit kit, final Context context, final ScheduledExecutorService scheduledExecutorService, final SessionAnalyticsFilesManager sessionAnalyticsFilesManager, final HttpRequestFactory httpRequestFactory, final SessionEventMetadata metadata) {
        super(context, scheduledExecutorService, sessionAnalyticsFilesManager);
        this.eventFilter = new KeepAllEventFilter();
        this.apiKey = new ApiKey();
        this.customEventsEnabled = true;
        this.predefinedEventsEnabled = true;
        this.kit = kit;
        this.httpRequestFactory = httpRequestFactory;
        this.metadata = metadata;
    }
    
    @Override
    public FilesSender getFilesSender() {
        return this.filesSender;
    }
    
    @Override
    public void processEvent(final SessionEvent.Builder builder) {
        final SessionEvent build = builder.build(this.metadata);
        if (!this.customEventsEnabled && SessionEvent.Type.CUSTOM.equals(build.type)) {
            Fabric.getLogger().d("Answers", "Custom events tracking disabled - skipping event: " + build);
            return;
        }
        if (!this.predefinedEventsEnabled && SessionEvent.Type.PREDEFINED.equals(build.type)) {
            Fabric.getLogger().d("Answers", "Predefined events tracking disabled - skipping event: " + build);
            return;
        }
        if (this.eventFilter.skipEvent(build)) {
            Fabric.getLogger().d("Answers", "Skipping filtered event: " + build);
            return;
        }
        this.recordEvent(build);
    }
    
    @Override
    public void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData, String s) {
        this.filesSender = AnswersRetryFilesSender.build(new SessionAnalyticsFilesSender(this.kit, s, analyticsSettingsData.analyticsURL, this.httpRequestFactory, this.apiKey.getValue(this.context)));
        ((SessionAnalyticsFilesManager)this.filesManager).setAnalyticsSettingsData(analyticsSettingsData);
        this.customEventsEnabled = analyticsSettingsData.trackCustomEvents;
        final Logger logger = Fabric.getLogger();
        final StringBuilder append = new StringBuilder().append("Custom event tracking ");
        if (this.customEventsEnabled) {
            s = "enabled";
        }
        else {
            s = "disabled";
        }
        logger.d("Answers", append.append(s).toString());
        this.predefinedEventsEnabled = analyticsSettingsData.trackPredefinedEvents;
        final Logger logger2 = Fabric.getLogger();
        final StringBuilder append2 = new StringBuilder().append("Predefined event tracking ");
        if (this.predefinedEventsEnabled) {
            s = "enabled";
        }
        else {
            s = "disabled";
        }
        logger2.d("Answers", append2.append(s).toString());
        if (analyticsSettingsData.samplingRate > 1) {
            Fabric.getLogger().d("Answers", "Event sampling enabled");
            this.eventFilter = new SamplingEventFilter(analyticsSettingsData.samplingRate);
        }
        this.configureRollover(analyticsSettingsData.flushIntervalSeconds);
    }
}
