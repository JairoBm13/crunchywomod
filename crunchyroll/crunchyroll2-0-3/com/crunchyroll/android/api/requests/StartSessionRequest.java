// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class StartSessionRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 6699456696026589768L;
    private final Optional<String> auth;
    private final Optional<Integer> duration;
    
    public StartSessionRequest(final String s, final Integer n) {
        this.auth = Optional.fromNullable(s);
        this.duration = Optional.fromNullable(n);
    }
    
    @Override
    public String getApiMethod() {
        return "start_session";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (this.auth.isPresent()) {
            builder.put("auth", this.auth.get());
        }
        if (this.duration.isPresent()) {
            builder.put("duration", this.duration.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "StartSessionRequest [getParams()=" + this.getParams() + "]";
    }
}
