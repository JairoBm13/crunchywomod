// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.Map;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdsRequest;

public class j implements AdsRequest
{
    private String a;
    private AdDisplayContainer b;
    private Map<String, String> c;
    private String d;
    private transient Object e;
    
    @Override
    public AdDisplayContainer getAdDisplayContainer() {
        return this.b;
    }
    
    @Override
    public String getAdTagUrl() {
        return this.a;
    }
    
    @Override
    public String getAdsResponse() {
        return this.d;
    }
    
    @Override
    public Map<String, String> getExtraParameters() {
        return this.c;
    }
    
    @Override
    public Object getUserRequestContext() {
        return this.e;
    }
    
    @Override
    public void setAdDisplayContainer(final AdDisplayContainer b) {
        this.b = b;
    }
    
    @Override
    public void setAdTagUrl(final String a) {
        this.a = a;
    }
}
