// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

public interface AdsManager
{
    void addAdErrorListener(final AdErrorEvent.AdErrorListener p0);
    
    void addAdEventListener(final AdEvent.AdEventListener p0);
    
    void destroy();
    
    void init();
    
    void start();
}
