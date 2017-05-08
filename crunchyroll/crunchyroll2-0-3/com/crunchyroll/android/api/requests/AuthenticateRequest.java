// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public class AuthenticateRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 2812485524198278292L;
    private final String auth;
    private final Optional<Integer> duration;
    
    public AuthenticateRequest(final String auth, final Integer n) {
        this.auth = auth;
        this.duration = Optional.fromNullable(n);
    }
    
    @Override
    public String getApiMethod() {
        return "authenticate";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("auth", this.auth);
        if (this.duration.isPresent()) {
            builder.put("duration", this.duration.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "AuthenticateRequest [getParams()=" + this.getParams() + "]";
    }
}
