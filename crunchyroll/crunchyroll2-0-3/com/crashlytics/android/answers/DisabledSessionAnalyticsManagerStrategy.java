// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import io.fabric.sdk.android.services.events.DisabledEventsStrategy;

class DisabledSessionAnalyticsManagerStrategy extends DisabledEventsStrategy<SessionEvent> implements SessionAnalyticsManagerStrategy<SessionEvent>
{
    @Override
    public void processEvent(final SessionEvent.Builder builder) {
    }
    
    @Override
    public void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData, final String s) {
    }
}
