// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class LocaleData implements Serializable
{
    private static final long serialVersionUID = 943789583548458745L;
    @JsonProperty("label")
    private String label;
    @JsonProperty("locale_id")
    private String localeId;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final LocaleData localeData = (LocaleData)o;
            if (this.localeId == null) {
                if (localeData.localeId != null) {
                    return false;
                }
            }
            else if (!this.localeId.equals(localeData.localeId)) {
                return false;
            }
            if (this.label == null) {
                if (localeData.label != null) {
                    return false;
                }
            }
            else if (!this.label.equals(localeData.label)) {
                return false;
            }
        }
        return true;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getLocaleId() {
        return this.localeId;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.localeId == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.localeId.hashCode();
        }
        if (this.label != null) {
            hashCode = this.label.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public void setLocaleId(final String localeId) {
        this.localeId = localeId;
    }
    
    @Override
    public String toString() {
        return "LocaleData [localeId=" + this.localeId + ", label=" + this.label + "]";
    }
}
