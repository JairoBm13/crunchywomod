// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class LogoutRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 5771313645201564050L;
    private final String auth;
    
    public LogoutRequest(final String auth) {
        this.auth = auth;
    }
    
    @Override
    public String getApiMethod() {
        return "logout";
    }
    
    public String getAuth() {
        return this.auth;
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        return ImmutableMap.of("auth", this.auth);
    }
    
    @Override
    public String toString() {
        return "LogoutRequest [getParams()=" + this.getParams() + "]";
    }
}
