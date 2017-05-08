// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.util.List;
import java.io.Serializable;

public class SeriesInfoWithMedia implements Serializable
{
    private static final long serialVersionUID = 4622468894768136469L;
    private List<Media> medias;
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
            final SeriesInfoWithMedia seriesInfoWithMedia = (SeriesInfoWithMedia)o;
            if (this.medias == null) {
                if (seriesInfoWithMedia.medias != null) {
                    return false;
                }
            }
            else if (!this.medias.equals(seriesInfoWithMedia.medias)) {
                return false;
            }
            if (this.series == null) {
                if (seriesInfoWithMedia.series != null) {
                    return false;
                }
            }
            else if (!this.series.equals(seriesInfoWithMedia.series)) {
                return false;
            }
        }
        return true;
    }
    
    public List<Media> getMedias() {
        return this.medias;
    }
    
    public Series getSeries() {
        return this.series;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.medias == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.medias.hashCode();
        }
        if (this.series != null) {
            hashCode = this.series.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void setMedias(final List<Media> medias) {
        this.medias = medias;
    }
    
    public void setSeries(final Series series) {
        this.series = series;
    }
    
    @Override
    public String toString() {
        return "SeriesInfoWithMedia [series=" + this.series + ", medias=" + this.medias + "]";
    }
}
