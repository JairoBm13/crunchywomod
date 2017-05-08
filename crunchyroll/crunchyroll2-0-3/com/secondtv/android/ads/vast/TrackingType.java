// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

public enum TrackingType
{
    COMPLETE("complete"), 
    FIRST_QUARTILE("firstQuartile"), 
    MIDPOINT("midpoint"), 
    MUTE("mute"), 
    PAUSE("pause"), 
    START("start"), 
    THIRD_QUARTILE("thirdQuartile");
    
    public final String vastValue;
    
    private TrackingType(final String vastValue) {
        this.vastValue = vastValue;
    }
    
    public static TrackingType fromVastValue(final String s) {
        final TrackingType[] values = values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final TrackingType trackingType = values[i];
            if (trackingType.vastValue.equals(s)) {
                return trackingType;
            }
        }
        throw new IllegalArgumentException();
    }
}
