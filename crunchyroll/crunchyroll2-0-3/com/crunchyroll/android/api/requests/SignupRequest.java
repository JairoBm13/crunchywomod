// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public class SignupRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = -5231636496104617595L;
    private final Optional<Integer> duration;
    private final String email;
    private final Optional<String> firstName;
    private final Optional<String> lastName;
    private final String password;
    private final Optional<String> username;
    
    public SignupRequest(final String s, final String s2) {
        this(s, s2, null, null, null, null);
    }
    
    public SignupRequest(final String email, final String password, final String s, final String s2, final String s3, final Integer n) {
        this.email = email;
        this.password = password;
        this.username = Optional.fromNullable(s);
        this.firstName = Optional.fromNullable(s2);
        this.lastName = Optional.fromNullable(s3);
        this.duration = Optional.fromNullable(n);
    }
    
    @Override
    public String getApiMethod() {
        return "signup";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public ImmutableMap<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("email", this.email);
        builder.put("password", this.password);
        if (this.username.isPresent()) {
            builder.put("username", this.username.get().toString());
        }
        if (this.firstName.isPresent()) {
            builder.put("first_name", this.firstName.get().toString());
        }
        if (this.lastName.isPresent()) {
            builder.put("last_name", this.lastName.get().toString());
        }
        if (this.duration.isPresent()) {
            builder.put("duration", this.duration.get().toString());
        }
        return builder.build();
    }
    
    @Override
    public String toString() {
        return "SignupRequest [getParams()=" + this.getParams() + "]";
    }
}
