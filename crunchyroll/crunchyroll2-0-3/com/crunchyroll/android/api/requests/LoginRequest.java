// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public final class LoginRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 3046300772129169412L;
    private final String account;
    private final Optional<Integer> duration;
    private final String password;
    
    public LoginRequest(final String s, final String s2) {
        this(s, s2, null);
    }
    
    public LoginRequest(final String account, final String password, final Integer n) {
        this.account = account;
        this.password = password;
        this.duration = Optional.fromNullable(n);
    }
    
    @Override
    public String getApiMethod() {
        return "login";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("account", this.account);
        builder.put("password", this.password);
        if (this.duration.isPresent()) {
            builder.put("duration", this.duration.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "LoginRequest [getParams()=" + this.getParams() + "]";
    }
}
