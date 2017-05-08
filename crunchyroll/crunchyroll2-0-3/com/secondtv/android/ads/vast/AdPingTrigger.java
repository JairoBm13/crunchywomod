// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import java.io.Serializable;
import java.util.Iterator;
import java.util.HashMap;
import android.os.Bundle;
import java.util.HashSet;
import java.util.Map;

public class AdPingTrigger implements Runnable
{
    public static final String BUNDLE_NAME = "AdPingTrigger";
    public static final String KEY_PINGS_EXECUTED = "PingsExecuted";
    private final Map<Double, TrackingType> mPingMap;
    private HashSet<Double> mPingsExecuted;
    private final VastFragment mVastFragment;
    
    public AdPingTrigger(final VastFragment mVastFragment, final Bundle bundle) {
        this.mVastFragment = mVastFragment;
        (this.mPingMap = new HashMap<Double, TrackingType>(4)).put(0.0, TrackingType.START);
        this.mPingMap.put(0.25, TrackingType.FIRST_QUARTILE);
        this.mPingMap.put(0.5, TrackingType.MIDPOINT);
        this.mPingMap.put(0.75, TrackingType.THIRD_QUARTILE);
        this.mPingsExecuted = new HashSet<Double>();
        if (bundle != null) {
            final Bundle bundle2 = bundle.getBundle("AdPingTrigger");
            if (bundle2 != null && bundle2.containsKey("PingsExecuted")) {
                this.mPingsExecuted = (HashSet<Double>)bundle2.getSerializable("PingsExecuted");
            }
        }
    }
    
    @Override
    public void run() {
        if (this.mVastFragment != null && this.mVastFragment.isPlaying()) {
            final double n = this.mVastFragment.getPlayhead();
            final double n2 = this.mVastFragment.getDuration();
            double n3;
            if (n > 0.0 && n2 > 0.0) {
                n3 = n / n2;
            }
            else {
                n3 = 0.0;
            }
            for (final double doubleValue : this.mPingMap.keySet()) {
                if (n3 > doubleValue && !this.mPingsExecuted.contains(doubleValue)) {
                    if (doubleValue == 0.0) {
                        this.mVastFragment.dispatchImpressionPing();
                    }
                    else {
                        this.mVastFragment.dispatchTrackingPing(this.mPingMap.get(doubleValue));
                    }
                    this.mPingsExecuted.add(doubleValue);
                }
            }
        }
    }
    
    public void saveInstanceState(final Bundle bundle) {
        final Bundle bundle2 = new Bundle();
        bundle2.putSerializable("PingsExecuted", (Serializable)this.mPingsExecuted);
        bundle.putBundle("AdPingTrigger", bundle2);
    }
}
