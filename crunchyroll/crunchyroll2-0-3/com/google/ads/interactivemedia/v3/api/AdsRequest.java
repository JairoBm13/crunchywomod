// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

import java.util.Map;

public interface AdsRequest
{
    AdDisplayContainer getAdDisplayContainer();
    
    String getAdTagUrl();
    
    String getAdsResponse();
    
    Map<String, String> getExtraParameters();
    
    Object getUserRequestContext();
    
    void setAdDisplayContainer(final AdDisplayContainer p0);
    
    void setAdTagUrl(final String p0);
}
