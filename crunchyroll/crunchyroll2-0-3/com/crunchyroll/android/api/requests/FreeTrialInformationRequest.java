// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class FreeTrialInformationRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 7732332362439441756L;
    
    @Override
    public String getApiMethod() {
        return "free_trial_info";
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
        return "FreeTrialInformationRequest [getParams()=" + this.getParams() + "]";
    }
}
