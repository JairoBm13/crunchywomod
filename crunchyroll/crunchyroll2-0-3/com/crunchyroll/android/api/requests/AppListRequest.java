// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.HashMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractOauthApiRequest;

public class AppListRequest extends AbstractOauthApiRequest
{
    @Override
    public String getApiMethod() {
        return "apps";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public Map<String, String> getParams() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("related_to", "com.crunchyroll.crunchyroid");
        return hashMap;
    }
}
