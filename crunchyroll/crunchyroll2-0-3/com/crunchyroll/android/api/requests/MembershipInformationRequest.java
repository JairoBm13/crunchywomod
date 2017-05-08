// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class MembershipInformationRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -7170159806320308690L;
    
    @Override
    public String getApiMethod() {
        return "membership_info";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        return ImmutableMap.of();
    }
    
    @Override
    public String toString() {
        return "MembershipInformationRequest [getParams()=" + this.getParams() + "]";
    }
}
