// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.requests;

import com.google.common.collect.ImmutableMap;
import android.text.TextUtils;
import java.util.Map;
import com.crunchyroll.android.api.ApiRequest;
import com.crunchyroll.android.api.AbstractApiRequest;

public class LogInstallReferrerRequest extends AbstractApiRequest
{
    private static final long serialVersionUID = 6119372730992642405L;
    private final String installReferrer;
    
    public LogInstallReferrerRequest(final String installReferrer) {
        this.installReferrer = installReferrer;
    }
    
    @Override
    public String getApiMethod() {
        return "log_install_referrer";
    }
    
    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }
    
    @Override
    public Map<String, String> getParams() {
        if (this.installReferrer == null || TextUtils.isEmpty((CharSequence)this.installReferrer)) {
            return (Map<String, String>)ImmutableMap.of();
        }
        return ImmutableMap.of("referrer", this.installReferrer);
    }
    
    @Override
    public String toString() {
        return "LogFirstLaunchRequest [getParams()=" + this.getParams() + "]";
    }
}
