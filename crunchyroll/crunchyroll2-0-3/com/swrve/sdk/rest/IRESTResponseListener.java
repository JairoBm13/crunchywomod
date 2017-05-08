// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.rest;

public interface IRESTResponseListener
{
    void onException(final Exception p0);
    
    void onResponse(final RESTResponse p0);
}
