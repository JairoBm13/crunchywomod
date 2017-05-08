// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.util.List;
import java.io.Serializable;

public class InitialBrowseData implements Serializable
{
    private static final long serialVersionUID = 1704214069840137353L;
    private List<Series> alphaSeries;
    private Categories categories;
    private List<Series> popularSeries;
    private List<QueueEntry> queueList;
    private List<Series> simulcastSeries;
    private List<Series> updatedSeries;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final InitialBrowseData initialBrowseData = (InitialBrowseData)o;
            if (this.alphaSeries == null) {
                if (initialBrowseData.alphaSeries != null) {
                    return false;
                }
            }
            else if (!this.alphaSeries.equals(initialBrowseData.alphaSeries)) {
                return false;
            }
            if (this.categories == null) {
                if (initialBrowseData.categories != null) {
                    return false;
                }
            }
            else if (!this.categories.equals(initialBrowseData.categories)) {
                return false;
            }
            if (this.popularSeries == null) {
                if (initialBrowseData.popularSeries != null) {
                    return false;
                }
            }
            else if (!this.popularSeries.equals(initialBrowseData.popularSeries)) {
                return false;
            }
            if (this.simulcastSeries == null) {
                if (initialBrowseData.simulcastSeries != null) {
                    return false;
                }
            }
            else if (!this.simulcastSeries.equals(initialBrowseData.simulcastSeries)) {
                return false;
            }
            if (this.updatedSeries == null) {
                if (initialBrowseData.updatedSeries != null) {
                    return false;
                }
            }
            else if (!this.updatedSeries.equals(initialBrowseData.updatedSeries)) {
                return false;
            }
        }
        return true;
    }
    
    public List<Series> getAlphaSeries() {
        return this.alphaSeries;
    }
    
    public Categories getCategories() {
        return this.categories;
    }
    
    public List<Series> getPopularSeries() {
        return this.popularSeries;
    }
    
    public List<QueueEntry> getQueueList() {
        return this.queueList;
    }
    
    public List<Series> getSimulcastSeries() {
        return this.simulcastSeries;
    }
    
    public List<Series> getUpdatedSeries() {
        return this.updatedSeries;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.alphaSeries == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.alphaSeries.hashCode();
        }
        int hashCode3;
        if (this.categories == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.categories.hashCode();
        }
        int hashCode4;
        if (this.popularSeries == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.popularSeries.hashCode();
        }
        int hashCode5;
        if (this.simulcastSeries == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.simulcastSeries.hashCode();
        }
        if (this.updatedSeries != null) {
            hashCode = this.updatedSeries.hashCode();
        }
        return ((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode;
    }
    
    public void setAlphaSeries(final List<Series> alphaSeries) {
        this.alphaSeries = alphaSeries;
    }
    
    public void setCategories(final Categories categories) {
        this.categories = categories;
    }
    
    public void setPopularSeries(final List<Series> popularSeries) {
        this.popularSeries = popularSeries;
    }
    
    public void setQueueList(final List<QueueEntry> queueList) {
        this.queueList = queueList;
    }
    
    public void setSimulcastSeries(final List<Series> simulcastSeries) {
        this.simulcastSeries = simulcastSeries;
    }
    
    public void setUpdatedSeries(final List<Series> updatedSeries) {
        this.updatedSeries = updatedSeries;
    }
    
    @Override
    public String toString() {
        return "InitialBrowseData [popularSeries=" + this.popularSeries + ", simulcastSeries=" + this.simulcastSeries + ", updatedSeries=" + this.updatedSeries + ", alphaSeries=" + this.alphaSeries + ", categories=" + this.categories + "]";
    }
}
