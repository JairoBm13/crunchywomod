// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.rest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IRESTClient
{
    void get(final String p0, final Map<String, String> p1, final IRESTResponseListener p2) throws UnsupportedEncodingException;
    
    void post(final String p0, final String p1, final IRESTResponseListener p2);
}
