// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.a;

import java.util.Iterator;
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot;
import java.util.HashMap;
import com.google.ads.interactivemedia.v3.b.a;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import java.util.Map;

public class d
{
    private String adTagUrl;
    private String adsResponse;
    private Map<String, String> companionSlots;
    private String env;
    private Map<String, String> extraParameters;
    private String network;
    private ImaSdkSettings settings;
    
    public d(final AdsRequest adsRequest, final String env, String network, final ImaSdkSettings settings) {
        this.adTagUrl = adsRequest.getAdTagUrl();
        this.adsResponse = adsRequest.getAdsResponse();
        this.env = env;
        this.network = network;
        this.extraParameters = adsRequest.getExtraParameters();
        this.settings = settings;
        final Map<String, CompanionAdSlot> a = ((a)adsRequest.getAdDisplayContainer()).a();
        if (a != null && !a.isEmpty()) {
            this.companionSlots = new HashMap<String, String>();
            final Iterator<String> iterator = a.keySet().iterator();
            while (iterator.hasNext()) {
                network = iterator.next();
                final CompanionAdSlot companionAdSlot = a.get(network);
                this.companionSlots.put(network, companionAdSlot.getWidth() + "x" + companionAdSlot.getHeight());
            }
        }
    }
    
    @Override
    public String toString() {
        String s = "adTagUrl=" + this.adTagUrl + ", env=" + this.env + ", network=" + this.network + ", companionSlots=" + this.companionSlots + ", extraParameters=" + this.extraParameters + ", settings=" + this.settings;
        if (this.adsResponse != null) {
            s = s + ", adsResponse=" + this.adsResponse;
        }
        return "GsonAdsRequest [" + s + "]";
    }
}
