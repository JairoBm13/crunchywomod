// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdOption implements Serializable
{
    public static final String IMA = "googleIMA";
    public static final String PARAM_APP_ID = "app_id";
    public static final String PARAM_URL = "url";
    public static final String TREMOR = "tremor";
    public static final String VAST = "vast";
    private static final long serialVersionUID = -625678544780637898L;
    @JsonProperty("params")
    private Map<String, Object> params;
    @JsonProperty("type")
    private String type;
    
    public AdOption() {
    }
    
    public AdOption(final String type, final Map<String, Object> params) {
        this.type = type;
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final AdOption adOption = (AdOption)o;
            if (this.params == null) {
                if (adOption.params != null) {
                    return false;
                }
            }
            else if (!this.params.equals(adOption.params)) {
                return false;
            }
            if (this.type == null) {
                if (adOption.type != null) {
                    return false;
                }
            }
            else if (!this.type.equals(adOption.type)) {
                return false;
            }
        }
        return true;
    }
    
    public Map<String, Object> getParams() {
        return this.params;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.params == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.params.hashCode();
        }
        if (this.type != null) {
            hashCode = this.type.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void setParams(final Map<String, Object> params) {
        this.params = params;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "AdOption [type=" + this.type + ", params=" + this.params + "]";
    }
}
