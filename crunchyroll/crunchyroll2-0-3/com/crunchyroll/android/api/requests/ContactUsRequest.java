// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.AbstractApiRequest;

public class ContactUsRequest extends AbstractApiRequest
{
    private String mDescription;
    private String mEmail;
    private Optional<Boolean> mReceiveResponse;
    private String mSubject;
    private Optional<String> mType;
    
    public ContactUsRequest(final String mEmail, final String mSubject, final String s, final String mDescription, final Boolean b) {
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mDescription = mDescription;
        this.mType = Optional.fromNullable(s);
        this.mReceiveResponse = Optional.fromNullable(b);
    }
    
    @Override
    public String getApiMethod() {
        return "contact";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public Map<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("email", this.mEmail);
        builder.put("subject", this.mSubject);
        builder.put("message", this.mDescription);
        if (this.mType.isPresent()) {
            builder.put("type", this.mType.get());
        }
        if (this.mReceiveResponse.isPresent()) {
            builder.put("receive_response", this.mReceiveResponse.get().toString());
        }
        return builder.build();
    }
}
