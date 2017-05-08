// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import java.util.HashSet;
import java.util.Set;

class SamplingEventFilter implements EventFilter
{
    static final Set<SessionEvent.Type> EVENTS_TYPE_TO_SAMPLE;
    final int samplingRate;
    
    static {
        EVENTS_TYPE_TO_SAMPLE = new HashSet<SessionEvent.Type>() {
            {
                this.add(SessionEvent.Type.CREATE);
                this.add(SessionEvent.Type.START);
                this.add(SessionEvent.Type.RESUME);
                this.add(SessionEvent.Type.SAVE_INSTANCE_STATE);
                this.add(SessionEvent.Type.PAUSE);
                this.add(SessionEvent.Type.STOP);
                this.add(SessionEvent.Type.DESTROY);
                this.add(SessionEvent.Type.ERROR);
            }
        };
    }
    
    public SamplingEventFilter(final int samplingRate) {
        this.samplingRate = samplingRate;
    }
    
    @Override
    public boolean skipEvent(final SessionEvent sessionEvent) {
        boolean b;
        if (SamplingEventFilter.EVENTS_TYPE_TO_SAMPLE.contains(sessionEvent.type) && sessionEvent.sessionEventMetadata.betaDeviceToken == null) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if (Math.abs(sessionEvent.sessionEventMetadata.installationId.hashCode() % this.samplingRate) != 0) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        return b && b2;
    }
}
