// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

public interface AdsLoader
{
    void addAdErrorListener(final AdErrorEvent.AdErrorListener p0);
    
    void addAdsLoadedListener(final AdsLoadedListener p0);
    
    void contentComplete();
    
    void requestAds(final AdsRequest p0);
    
    public interface AdsLoadedListener
    {
        void onAdsManagerLoaded(final AdsManagerLoadedEvent p0);
    }
}
