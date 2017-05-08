// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.base.Joiner;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import java.util.ArrayList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LocalizedStringsRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 3572999448144218040L;
    
    @Override
    public String getApiMethod() {
        return "translations";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }
    
    @Override
    public Map<String, String> getParams() {
        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        final ArrayList<String> list = new ArrayList<String>();
        final LocalizedStrings[] values = LocalizedStrings.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            list.add(values[i].getToken());
        }
        return builder.put("keys", Joiner.on(",").join(list)).build();
    }
    
    @Override
    public String toString() {
        return "LocalizedStringsRequest [getParams()=" + this.getParams() + "]";
    }
}
