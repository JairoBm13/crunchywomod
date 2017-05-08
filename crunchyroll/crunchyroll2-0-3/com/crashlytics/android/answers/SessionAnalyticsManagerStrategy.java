// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import io.fabric.sdk.android.services.events.EventsStrategy;

interface SessionAnalyticsManagerStrategy<T> extends EventsStrategy<T>
{
    void processEvent(final SessionEvent.Builder p0);
    
    void setAnalyticsSettingsData(final AnalyticsSettingsData p0, final String p1);
}
