// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class ListAdsRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 4560228128533127461L;
    private final String mAdvertisingId;
    private final Long mMediaId;
    
    public ListAdsRequest(final Long mMediaId, final String mAdvertisingId) {
        this.mMediaId = mMediaId;
        this.mAdvertisingId = mAdvertisingId;
    }
    
    @Override
    public String getApiMethod() {
        return "list_ads";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        if (this.mAdvertisingId != null) {
            return ImmutableMap.of("media_id", this.mMediaId.toString(), "device_idfa", this.mAdvertisingId);
        }
        return ImmutableMap.of("media_id", this.mMediaId.toString());
    }
    
    @Override
    public int getVersion() {
        return 2;
    }
    
    @Override
    public String toString() {
        return "ListAdsRequest [getParams()=" + this.getParams() + "]";
    }
}
