// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import java.util.Map;
import java.io.Serializable;

public interface ApiRequest extends Serializable
{
    String getApiMethod();
    
    Object getKey();
    
    RequestMethod getMethod();
    
    Map<String, String> getParams();
    
    String getUrl();
    
    int getVersion();
    
    boolean requiresOauth();
    
    public enum AdIntegration
    {
        TREMOR, 
        VAST;
        
        @Override
        public String toString() {
            switch (this) {
                default: {
                    return "unknown";
                }
                case TREMOR: {
                    return "tremor";
                }
                case VAST: {
                    return "vast";
                }
            }
        }
    }
    
    public enum RequestMethod
    {
        GET, 
        POST;
    }
}
