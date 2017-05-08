// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdSlot implements Serializable
{
    public static final String MIDROLL = "midroll";
    public static final String POSTROLL = "postroll";
    public static final String PREROLL = "preroll";
    private static final long serialVersionUID = 4130635902594403196L;
    @JsonProperty("options")
    private List<AdOption> options;
    @JsonProperty("playhead")
    private Double playhead;
    @JsonProperty("type")
    private String type;
    
    public AdSlot() {
    }
    
    public AdSlot(final String type, final Double playhead, final List<AdOption> options) {
        this.type = type;
        this.playhead = playhead;
        this.options = options;
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
            final AdSlot adSlot = (AdSlot)o;
            if (this.options == null) {
                if (adSlot.options != null) {
                    return false;
                }
            }
            else if (!this.options.equals(adSlot.options)) {
                return false;
            }
            if (this.playhead == null) {
                if (adSlot.playhead != null) {
                    return false;
                }
            }
            else if (!this.playhead.equals(adSlot.playhead)) {
                return false;
            }
            if (this.type == null) {
                if (adSlot.type != null) {
                    return false;
                }
            }
            else if (!this.type.equals(adSlot.type)) {
                return false;
            }
        }
        return true;
    }
    
    public List<AdOption> getOptions() {
        return this.options;
    }
    
    public Double getPlayhead() {
        return this.playhead;
    }
    
    public String getType() {
        return this.type;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.options == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.options.hashCode();
        }
        int hashCode3;
        if (this.playhead == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.playhead.hashCode();
        }
        if (this.type != null) {
            hashCode = this.type.hashCode();
        }
        return ((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode;
    }
    
    public void setOptions(final List<AdOption> options) {
        this.options = options;
    }
    
    public void setPlayhead(final Double playhead) {
        this.playhead = playhead;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "AdSlot [type=" + this.type + ", playhead=" + this.playhead + ", options=" + this.options + "]";
    }
}
