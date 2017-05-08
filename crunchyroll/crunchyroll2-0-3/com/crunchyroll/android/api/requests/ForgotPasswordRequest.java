// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class ForgotPasswordRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 94506398961665461L;
    private final String email;
    
    public ForgotPasswordRequest(final String email) {
        this.email = email;
    }
    
    @Override
    public String getApiMethod() {
        return "forgot_password";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        return ImmutableMap.of("email", this.email);
    }
    
    @Override
    public String toString() {
        return "ForgotPasswordRequest [getParams()=" + this.getParams() + "]";
    }
}
