// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import android.app.Activity;
import io.fabric.sdk.android.Fabric;
import android.os.Looper;

class SessionAnalyticsManager
{
    final AnswersEventsHandler eventsHandler;
    
    public SessionAnalyticsManager(final AnswersEventsHandler eventsHandler) {
        this.eventsHandler = eventsHandler;
    }
    
    public void disable() {
        this.eventsHandler.disable();
    }
    
    public void onCrash(final String s) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("onCrash called from main thread!!!");
        }
        Fabric.getLogger().d("Answers", "Logged crash");
        this.eventsHandler.processEventSync(SessionEvent.crashEventBuilder(s));
    }
    
    public void onInstall() {
        Fabric.getLogger().d("Answers", "Logged install");
        this.eventsHandler.processEventAsyncAndFlush(SessionEvent.installEventBuilder());
    }
    
    public void onLifecycle(final Activity activity, final SessionEvent.Type type) {
        Fabric.getLogger().d("Answers", "Logged lifecycle event: " + type.name());
        this.eventsHandler.processEventAsync(SessionEvent.lifecycleEventBuilder(type, activity));
    }
    
    public void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData, final String s) {
        this.eventsHandler.setAnalyticsSettingsData(analyticsSettingsData, s);
    }
}
