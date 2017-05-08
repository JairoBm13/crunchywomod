// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import com.facebook.internal.Logger;
import android.os.Bundle;
import java.io.Serializable;

class FacebookTimeSpentData implements Serializable
{
    private static final long APP_ACTIVATE_SUPPRESSION_PERIOD_IN_MILLISECONDS = 300000L;
    private static final long FIRST_TIME_LOAD_RESUME_TIME = -1L;
    private static final long[] INACTIVE_SECONDS_QUANTA;
    private static final long INTERRUPTION_THRESHOLD_MILLISECONDS = 1000L;
    private static final long NUM_MILLISECONDS_IDLE_TO_BE_NEW_SESSION = 60000L;
    private static final String TAG;
    private static final long serialVersionUID = 1L;
    private String firstOpenSourceApplication;
    private int interruptionCount;
    private boolean isAppActive;
    private boolean isWarmLaunch;
    private long lastActivateEventLoggedTime;
    private long lastResumeTime;
    private long lastSuspendTime;
    private long millisecondsSpentInSession;
    
    static {
        TAG = AppEventsLogger.class.getCanonicalName();
        INACTIVE_SECONDS_QUANTA = new long[] { 300000L, 900000L, 1800000L, 3600000L, 21600000L, 43200000L, 86400000L, 172800000L, 259200000L, 604800000L, 1209600000L, 1814400000L, 2419200000L, 5184000000L, 7776000000L, 10368000000L, 12960000000L, 15552000000L, 31536000000L };
    }
    
    FacebookTimeSpentData() {
        this.resetSession();
    }
    
    private FacebookTimeSpentData(final long lastResumeTime, final long lastSuspendTime, final long millisecondsSpentInSession, final int interruptionCount) {
        this.resetSession();
        this.lastResumeTime = lastResumeTime;
        this.lastSuspendTime = lastSuspendTime;
        this.millisecondsSpentInSession = millisecondsSpentInSession;
        this.interruptionCount = interruptionCount;
    }
    
    private FacebookTimeSpentData(final long lastResumeTime, final long lastSuspendTime, final long millisecondsSpentInSession, final int interruptionCount, final String firstOpenSourceApplication) {
        this.resetSession();
        this.lastResumeTime = lastResumeTime;
        this.lastSuspendTime = lastSuspendTime;
        this.millisecondsSpentInSession = millisecondsSpentInSession;
        this.interruptionCount = interruptionCount;
        this.firstOpenSourceApplication = firstOpenSourceApplication;
    }
    
    private static int getQuantaIndex(final long n) {
        int n2;
        for (n2 = 0; n2 < FacebookTimeSpentData.INACTIVE_SECONDS_QUANTA.length && FacebookTimeSpentData.INACTIVE_SECONDS_QUANTA[n2] < n; ++n2) {}
        return n2;
    }
    
    private boolean isColdLaunch() {
        final boolean b = !this.isWarmLaunch;
        this.isWarmLaunch = true;
        return b;
    }
    
    private void logAppDeactivatedEvent(final AppEventsLogger appEventsLogger, final long n) {
        final Bundle bundle = new Bundle();
        bundle.putInt("fb_mobile_app_interruptions", this.interruptionCount);
        bundle.putString("fb_mobile_time_between_sessions", String.format("session_quanta_%d", getQuantaIndex(n)));
        bundle.putString("fb_mobile_launch_source", this.firstOpenSourceApplication);
        appEventsLogger.logEvent("fb_mobile_deactivate_app", this.millisecondsSpentInSession / 1000L, bundle);
        this.resetSession();
    }
    
    private void resetSession() {
        this.isAppActive = false;
        this.lastResumeTime = -1L;
        this.lastSuspendTime = -1L;
        this.interruptionCount = 0;
        this.millisecondsSpentInSession = 0L;
    }
    
    private boolean wasSuspendedEver() {
        return this.lastSuspendTime != -1L;
    }
    
    private Object writeReplace() {
        return new SerializationProxyV2(this.lastResumeTime, this.lastSuspendTime, this.millisecondsSpentInSession, this.interruptionCount, this.firstOpenSourceApplication);
    }
    
    void onResume(final AppEventsLogger appEventsLogger, final long n, final String firstOpenSourceApplication) {
        if (this.isColdLaunch() || n - this.lastActivateEventLoggedTime > 300000L) {
            final Bundle bundle = new Bundle();
            bundle.putString("fb_mobile_launch_source", firstOpenSourceApplication);
            appEventsLogger.logEvent("fb_mobile_activate_app", bundle);
            this.lastActivateEventLoggedTime = n;
        }
        if (this.isAppActive) {
            Logger.log(LoggingBehavior.APP_EVENTS, FacebookTimeSpentData.TAG, "Resume for active app");
            return;
        }
        long n2;
        if (this.wasSuspendedEver()) {
            n2 = n - this.lastSuspendTime;
        }
        else {
            n2 = 0L;
        }
        long n3 = n2;
        if (n2 < 0L) {
            Logger.log(LoggingBehavior.APP_EVENTS, FacebookTimeSpentData.TAG, "Clock skew detected");
            n3 = 0L;
        }
        if (n3 > 60000L) {
            this.logAppDeactivatedEvent(appEventsLogger, n3);
        }
        else if (n3 > 1000L) {
            ++this.interruptionCount;
        }
        if (this.interruptionCount == 0) {
            this.firstOpenSourceApplication = firstOpenSourceApplication;
        }
        this.lastResumeTime = n;
        this.isAppActive = true;
    }
    
    void onSuspend(final AppEventsLogger appEventsLogger, final long lastSuspendTime) {
        if (!this.isAppActive) {
            Logger.log(LoggingBehavior.APP_EVENTS, FacebookTimeSpentData.TAG, "Suspend for inactive app");
            return;
        }
        long n;
        if ((n = lastSuspendTime - this.lastResumeTime) < 0L) {
            Logger.log(LoggingBehavior.APP_EVENTS, FacebookTimeSpentData.TAG, "Clock skew detected");
            n = 0L;
        }
        this.millisecondsSpentInSession += n;
        this.lastSuspendTime = lastSuspendTime;
        this.isAppActive = false;
    }
    
    private static class SerializationProxyV1 implements Serializable
    {
        private static final long serialVersionUID = 6L;
        private final int interruptionCount;
        private final long lastResumeTime;
        private final long lastSuspendTime;
        private final long millisecondsSpentInSession;
        
        SerializationProxyV1(final long lastResumeTime, final long lastSuspendTime, final long millisecondsSpentInSession, final int interruptionCount) {
            this.lastResumeTime = lastResumeTime;
            this.lastSuspendTime = lastSuspendTime;
            this.millisecondsSpentInSession = millisecondsSpentInSession;
            this.interruptionCount = interruptionCount;
        }
        
        private Object readResolve() {
            return new FacebookTimeSpentData(this.lastResumeTime, this.lastSuspendTime, this.millisecondsSpentInSession, this.interruptionCount, (FacebookTimeSpentData$1)null);
        }
    }
    
    private static class SerializationProxyV2 implements Serializable
    {
        private static final long serialVersionUID = 6L;
        private final String firstOpenSourceApplication;
        private final int interruptionCount;
        private final long lastResumeTime;
        private final long lastSuspendTime;
        private final long millisecondsSpentInSession;
        
        SerializationProxyV2(final long lastResumeTime, final long lastSuspendTime, final long millisecondsSpentInSession, final int interruptionCount, final String firstOpenSourceApplication) {
            this.lastResumeTime = lastResumeTime;
            this.lastSuspendTime = lastSuspendTime;
            this.millisecondsSpentInSession = millisecondsSpentInSession;
            this.interruptionCount = interruptionCount;
            this.firstOpenSourceApplication = firstOpenSourceApplication;
        }
        
        private Object readResolve() {
            return new FacebookTimeSpentData(this.lastResumeTime, this.lastSuspendTime, this.millisecondsSpentInSession, this.interruptionCount, this.firstOpenSourceApplication, null);
        }
    }
}
