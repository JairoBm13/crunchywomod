// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;

public class g implements AdsManagerLoadedEvent
{
    private final AdsManager a;
    private final Object b;
    
    g(final AdsManager a, final Object b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public AdsManager getAdsManager() {
        return this.a;
    }
}
