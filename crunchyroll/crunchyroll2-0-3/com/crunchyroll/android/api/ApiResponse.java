// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import com.google.common.cache.Cache;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public final class ApiResponse
{
    public final TokenBuffer body;
    private final Cache<Object, TokenBuffer> cache;
    public final Object key;
    
    ApiResponse(final Object key, final TokenBuffer body, final Cache<Object, TokenBuffer> cache) {
        this.key = key;
        this.body = body;
        this.cache = cache;
    }
    
    public void cache() {
        this.cache.put(this.key, this.body);
    }
    
    public void invalidate() {
        this.cache.invalidate(this.key);
    }
}
