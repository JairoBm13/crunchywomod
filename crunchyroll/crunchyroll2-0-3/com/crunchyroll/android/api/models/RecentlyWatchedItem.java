// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentlyWatchedItem implements Serializable
{
    private static final long serialVersionUID = 6320412722674884978L;
    @JsonProperty("media")
    private Media media;
    @JsonProperty("series")
    private Series series;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final RecentlyWatchedItem recentlyWatchedItem = (RecentlyWatchedItem)o;
            if (this.media == null) {
                if (recentlyWatchedItem.media != null) {
                    return false;
                }
            }
            else if (!this.media.equals(recentlyWatchedItem.media)) {
                return false;
            }
            if (this.series == null) {
                if (recentlyWatchedItem.series != null) {
                    return false;
                }
            }
            else if (!this.series.equals(recentlyWatchedItem.series)) {
                return false;
            }
        }
        return true;
    }
    
    public Media getMedia() {
        return this.media;
    }
    
    public Series getSeries() {
        return this.series;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.media == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.media.hashCode();
        }
        if (this.series != null) {
            hashCode = this.series.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void setMedia(final Media media) {
        this.media = media;
    }
    
    public void setSeries(final Series series) {
        this.series = series;
    }
    
    @Override
    public String toString() {
        return "RecentlyWatchedItem [media=" + this.media + ", series=" + this.series + "]";
    }
}
