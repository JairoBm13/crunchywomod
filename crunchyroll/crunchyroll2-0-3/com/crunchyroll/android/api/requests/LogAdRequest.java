// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LogAdRequest extends AbstractApiRequest
{
    public static final String AD_END = "ad_end";
    public static final String AD_PARSE_COMPLETE = "ad_parse_complete";
    public static final String AD_PARSE_START = "ad_parse_start";
    public static final String AD_REQUESTED = "ad_requested";
    public static final String AD_SLOT_START = "ad_slot_start";
    public static final String AD_START = "ad_start";
    public static final String AD_UNFULFILLED = "ad_unfulfilled";
    public static final String POST_AD_RESUME = "post_ad_resume";
    private final String adNetwork;
    private final String event;
    
    public LogAdRequest(final String event, final String adNetwork) {
        this.adNetwork = adNetwork;
        this.event = event;
    }
    
    @Override
    public String getApiMethod() {
        return "log_ad_event";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("event", this.event);
        if (this.adNetwork != null) {
            builder.put("ad_network", this.adNetwork);
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "LogAdRequest [getParams()=" + this.getParams() + "]";
    }
}
