// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class UpdatedEpisode implements Serializable
{
    private static final long serialVersionUID = 73414016181507867L;
    @JsonProperty("in_queue")
    private Boolean inQueue;
    @JsonProperty("most_recent_media")
    private Media media;
    @JsonProperty("name")
    private String name;
    @JsonProperty("series_id")
    private Long seriesId;
    @JsonProperty("url")
    private String url;
    
    public Media getMedia() {
        return this.media;
    }
    
    public Series getSeries() {
        final Series series = new Series();
        series.setSeriesId(this.seriesId);
        series.setName(this.name);
        series.setInQueue(this.inQueue);
        series.setUrl(this.url);
        return series;
    }
}
