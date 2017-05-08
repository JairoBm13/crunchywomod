// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import android.os.SystemClock;
import android.util.Log;

public class TimingMetric
{
    private final boolean disabled;
    private long duration;
    private final String eventName;
    private long start;
    private final String tag;
    
    public TimingMetric(final String eventName, final String tag) {
        this.eventName = eventName;
        this.tag = tag;
        this.disabled = !Log.isLoggable(tag, 2);
    }
    
    private void reportToLog() {
        Log.v(this.tag, this.eventName + ": " + this.duration + "ms");
    }
    
    public void startMeasuring() {
        synchronized (this) {
            if (!this.disabled) {
                this.start = SystemClock.elapsedRealtime();
                this.duration = 0L;
            }
        }
    }
    
    public void stopMeasuring() {
        synchronized (this) {
            if (!this.disabled && this.duration == 0L) {
                this.duration = SystemClock.elapsedRealtime() - this.start;
                this.reportToLog();
            }
        }
    }
}
