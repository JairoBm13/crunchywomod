// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import com.google.common.hash.Hashing;
import com.google.common.base.Optional;
import com.google.common.hash.HashFunction;

public abstract class AbstractApiRequest implements ApiRequest
{
    private static final HashFunction hashFunction;
    private static final long serialVersionUID = -4362805173765535708L;
    protected Optional<String> sessionId;
    
    static {
        hashFunction = Hashing.goodFastHash(32);
    }
    
    public AbstractApiRequest() {
        this.sessionId = Optional.absent();
    }
    
    @Override
    public Object getKey() {
        return AbstractApiRequest.hashFunction.newHasher().putString(this.getUrl()).putInt(this.getParams().hashCode()).hash();
    }
    
    public Optional<String> getSessionId() {
        return this.sessionId;
    }
    
    @Override
    public String getUrl() {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append("api.crunchyroll.com").append("/");
        sb.append(this.getApiMethod()).append(".");
        sb.append(this.getVersion()).append(".");
        sb.append("json");
        return sb.toString();
    }
    
    @Override
    public int getVersion() {
        return 0;
    }
    
    @Override
    public boolean requiresOauth() {
        return false;
    }
    
    public void setSessionId(final String s) {
        this.sessionId = Optional.of(s);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [getParams()=" + this.getParams() + "]";
    }
}
