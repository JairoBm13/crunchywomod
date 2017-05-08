// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Ordering;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueEntry implements Serializable
{
    public static final Ordering<QueueEntry> listOrdering;
    private static final long serialVersionUID = 1735909171938133945L;
    @JsonProperty("last_watched_media")
    private Media lastWatchedMedia;
    @JsonProperty("most_likely_media")
    private Media mostLikelyMedia;
    @JsonProperty("ordering")
    private Integer ordering;
    @JsonProperty("queue_entry_id")
    private Long queueEntryId;
    @JsonProperty("series")
    private Series series;
    
    static {
        listOrdering = new Ordering<QueueEntry>() {
            @Override
            public int compare(final QueueEntry queueEntry, final QueueEntry queueEntry2) {
                return Ints.compare(queueEntry.getOrdering(), queueEntry2.getOrdering());
            }
        };
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
            final QueueEntry queueEntry = (QueueEntry)o;
            if (this.lastWatchedMedia == null) {
                if (queueEntry.lastWatchedMedia != null) {
                    return false;
                }
            }
            else if (!this.lastWatchedMedia.equals(queueEntry.lastWatchedMedia)) {
                return false;
            }
            if (this.mostLikelyMedia == null) {
                if (queueEntry.mostLikelyMedia != null) {
                    return false;
                }
            }
            else if (!this.mostLikelyMedia.equals(queueEntry.mostLikelyMedia)) {
                return false;
            }
            if (this.queueEntryId == null) {
                if (queueEntry.queueEntryId != null) {
                    return false;
                }
            }
            else if (!this.queueEntryId.equals(queueEntry.queueEntryId)) {
                return false;
            }
            if (this.series == null) {
                if (queueEntry.series != null) {
                    return false;
                }
            }
            else if (!this.series.equals(queueEntry.series)) {
                return false;
            }
        }
        return true;
    }
    
    public Optional<Media> getLastWatchedMedia() {
        return Optional.fromNullable(this.lastWatchedMedia);
    }
    
    public Optional<Media> getMostLikelyMedia() {
        return Optional.fromNullable(this.mostLikelyMedia);
    }
    
    public Integer getOrdering() {
        return this.ordering;
    }
    
    public Long getQueueEntryId() {
        return this.queueEntryId;
    }
    
    public Series getSeries() {
        return this.series;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.lastWatchedMedia == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.lastWatchedMedia.hashCode();
        }
        int hashCode3;
        if (this.mostLikelyMedia == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.mostLikelyMedia.hashCode();
        }
        int hashCode4;
        if (this.queueEntryId == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.queueEntryId.hashCode();
        }
        if (this.series != null) {
            hashCode = this.series.hashCode();
        }
        return (((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
    }
    
    public void setLastWatchedMedia(final Media lastWatchedMedia) {
        this.lastWatchedMedia = lastWatchedMedia;
    }
    
    public void setMostLikelyMedia(final Media mostLikelyMedia) {
        this.mostLikelyMedia = mostLikelyMedia;
    }
    
    public void setOrdering(final Integer ordering) {
        this.ordering = ordering;
    }
    
    public void setQueueEntryId(final Long queueEntryId) {
        this.queueEntryId = queueEntryId;
    }
    
    public void setSeries(final Series series) {
        this.series = series;
    }
    
    @Override
    public String toString() {
        return "QueueEntry [queueEntryId=" + this.queueEntryId + ", series=" + this.series + ", lastWatchedMedia=" + this.lastWatchedMedia + ", mostLikelyMedia=" + this.mostLikelyMedia + "]";
    }
}
