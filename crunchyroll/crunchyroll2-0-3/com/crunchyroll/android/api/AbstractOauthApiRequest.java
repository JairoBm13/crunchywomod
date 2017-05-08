// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

public abstract class AbstractOauthApiRequest extends AbstractApiRequest
{
    @Override
    public String getUrl() {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append("api.crunchyroll.com").append("/v");
        sb.append(this.getVersion()).append("/");
        sb.append(this.getApiMethod());
        return sb.toString();
    }
    
    @Override
    public boolean requiresOauth() {
        return true;
    }
}
