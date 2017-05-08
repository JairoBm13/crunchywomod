// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class EndSessionRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -3920284353801143162L;
    private final String sessionId;
    
    public EndSessionRequest(final String sessionId) {
        this.sessionId = sessionId;
    }
    
    @Override
    public String getApiMethod() {
        return "end_session";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public Map<String, String> getParams() {
        return ImmutableMap.of("session_id", this.sessionId);
    }
    
    @Override
    public String toString() {
        return "EndSessionRequest [getParams()=" + this.getParams() + "]";
    }
}
