// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

public interface AdEvent
{
    AdEventType getType();
    
    public interface AdEventListener
    {
        void onAdEvent(final AdEvent p0);
    }
    
    public enum AdEventType
    {
        ALL_ADS_COMPLETED, 
        CLICKED, 
        COMPLETED, 
        CONTENT_PAUSE_REQUESTED, 
        CONTENT_RESUME_REQUESTED, 
        FIRST_QUARTILE, 
        LOADED, 
        LOG, 
        MIDPOINT, 
        PAUSED, 
        RESUMED, 
        SKIPPED, 
        STARTED, 
        THIRD_QUARTILE;
    }
}
