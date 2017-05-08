// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.util.Iterator;
import java.util.ArrayList;
import com.secondtv.android.ads.AdSlot;
import java.util.List;
import java.io.Serializable;

public class EpisodeInfo implements Serializable
{
    private static final long serialVersionUID = 8783140808019007825L;
    private List<AdSlot> adSlots;
    private Integer maxAdStartSeconds;
    private Media media;
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
            final EpisodeInfo episodeInfo = (EpisodeInfo)o;
            if (this.series == null) {
                if (episodeInfo.series != null) {
                    return false;
                }
            }
            else if (!this.series.equals(episodeInfo.series)) {
                return false;
            }
            if (this.adSlots == null) {
                if (episodeInfo.adSlots != null) {
                    return false;
                }
            }
            else if (!this.adSlots.equals(episodeInfo.adSlots)) {
                return false;
            }
            if (this.media == null) {
                if (episodeInfo.media != null) {
                    return false;
                }
            }
            else if (!this.media.equals(episodeInfo.media)) {
                return false;
            }
            if (this.maxAdStartSeconds == null) {
                if (episodeInfo.maxAdStartSeconds != null) {
                    return false;
                }
            }
            else if (!this.maxAdStartSeconds.equals(episodeInfo.maxAdStartSeconds)) {
                return false;
            }
        }
        return true;
    }
    
    public List<AdSlot> getAdSlots() {
        return this.adSlots;
    }
    
    public List<Double> getAdsPlayHeads() {
        final ArrayList<Double> list = new ArrayList<Double>();
        final Iterator<AdSlot> iterator = this.adSlots.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getPlayhead());
        }
        return list;
    }
    
    public int getMaxAdStartSeconds() {
        return this.maxAdStartSeconds;
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
        if (this.series == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.series.hashCode();
        }
        int hashCode3;
        if (this.maxAdStartSeconds == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.maxAdStartSeconds.hashCode();
        }
        int hashCode4;
        if (this.adSlots == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.adSlots.hashCode();
        }
        if (this.media != null) {
            hashCode = this.media.hashCode();
        }
        return (((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
    }
    
    public void setAdSlots(final List<AdSlot> adSlots) {
        this.adSlots = adSlots;
    }
    
    public void setMaxAdStartSeconds(final Integer maxAdStartSeconds) {
        this.maxAdStartSeconds = maxAdStartSeconds;
    }
    
    public void setMedia(final Media media) {
        this.media = media;
    }
    
    public void setSeries(final Series series) {
        this.series = series;
    }
    
    @Override
    public String toString() {
        return "EpisodeInfo [media=" + this.media + ", series=" + this.series + ", maxAdStartSeconds=" + this.maxAdStartSeconds + ", adSlots=" + this.adSlots + "]";
    }
}
