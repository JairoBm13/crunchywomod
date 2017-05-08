// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.google.common.base.Optional;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Media implements Serializable
{
    private static final long serialVersionUID = 73414016181507867L;
    @JsonProperty("availability_notes")
    private String availabilityNotes;
    @JsonProperty("available_time")
    private String availableTime;
    @JsonProperty("bif_url")
    private String bifUrl;
    @JsonProperty("collection_id")
    private int collectionId;
    @JsonProperty("collection_name")
    private String collectionName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("episode_number")
    private String episodeNumber;
    @JsonProperty("free_available")
    private Boolean isFreeAvailable;
    @JsonProperty("premium_available")
    private Boolean isPremiumAvailable;
    @JsonProperty("media_id")
    private Long mediaId;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("playhead")
    private Integer playhead;
    @JsonProperty("screenshot_image")
    private ImageSet screenshotImage;
    @JsonProperty("series_id")
    private int seriesId;
    @JsonProperty("series_name")
    private String seriesName;
    @JsonProperty("stream_data")
    private StreamData streamData;
    @JsonProperty("url")
    private String url;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final Media media = (Media)o;
            if (this.availabilityNotes == null) {
                if (media.availabilityNotes != null) {
                    return false;
                }
            }
            else if (!this.availabilityNotes.equals(media.availabilityNotes)) {
                return false;
            }
            if (this.description == null) {
                if (media.description != null) {
                    return false;
                }
            }
            else if (!this.description.equals(media.description)) {
                return false;
            }
            if (this.episodeNumber == null) {
                if (media.episodeNumber != null) {
                    return false;
                }
            }
            else if (!this.episodeNumber.equals(media.episodeNumber)) {
                return false;
            }
            if (this.isFreeAvailable == null) {
                if (media.isFreeAvailable != null) {
                    return false;
                }
            }
            else if (!this.isFreeAvailable.equals(media.isFreeAvailable)) {
                return false;
            }
            if (this.isPremiumAvailable == null) {
                if (media.isPremiumAvailable != null) {
                    return false;
                }
            }
            else if (!this.isPremiumAvailable.equals(media.isPremiumAvailable)) {
                return false;
            }
            if (this.mediaId == null) {
                if (media.mediaId != null) {
                    return false;
                }
            }
            else if (!this.mediaId.equals(media.mediaId)) {
                return false;
            }
            if (this.mediaType == null) {
                if (media.mediaType != null) {
                    return false;
                }
            }
            else if (!this.mediaType.equals(media.mediaType)) {
                return false;
            }
            if (this.name == null) {
                if (media.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(media.name)) {
                return false;
            }
            if (this.screenshotImage == null) {
                if (media.screenshotImage != null) {
                    return false;
                }
            }
            else if (!this.screenshotImage.equals(media.screenshotImage)) {
                return false;
            }
            if (this.streamData == null) {
                if (media.streamData != null) {
                    return false;
                }
            }
            else if (!this.streamData.equals(media.streamData)) {
                return false;
            }
        }
        return true;
    }
    
    public String getAvailabilityNotes() {
        return this.availabilityNotes;
    }
    
    public String getAvailableTime() {
        return this.availableTime;
    }
    
    public String getBIFUrl() {
        return this.bifUrl;
    }
    
    public int getCollectionId() {
        return this.collectionId;
    }
    
    public String getCollectionName() {
        return this.collectionName;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Optional<Integer> getDuration() {
        return Optional.fromNullable(this.duration);
    }
    
    public String getEpisodeNumber() {
        return this.episodeNumber;
    }
    
    public Long getMediaId() {
        return this.mediaId;
    }
    
    public Optional<String> getMediaType() {
        return Optional.fromNullable(this.mediaType);
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getPercentWatched() {
        double n;
        if (this.playhead == null || this.duration == null || this.duration == 0) {
            n = 0.0;
        }
        else {
            n = this.playhead / this.duration;
            if (this.playhead < 30) {
                return 0.0;
            }
        }
        return n;
    }
    
    public Optional<Integer> getPlayhead() {
        return Optional.fromNullable(this.playhead);
    }
    
    public Optional<ImageSet> getScreenshotImage() {
        return Optional.fromNullable(this.screenshotImage);
    }
    
    public int getSeriesId() {
        return this.seriesId;
    }
    
    public Optional<String> getSeriesName() {
        return Optional.fromNullable(this.seriesName);
    }
    
    public Optional<StreamData> getStreamData() {
        return Optional.fromNullable(this.streamData);
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.availabilityNotes == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.availabilityNotes.hashCode();
        }
        int hashCode3;
        if (this.description == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.description.hashCode();
        }
        int hashCode4;
        if (this.episodeNumber == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.episodeNumber.hashCode();
        }
        int hashCode5;
        if (this.isFreeAvailable == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.isFreeAvailable.hashCode();
        }
        int hashCode6;
        if (this.isPremiumAvailable == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.isPremiumAvailable.hashCode();
        }
        int hashCode7;
        if (this.mediaId == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.mediaId.hashCode();
        }
        int hashCode8;
        if (this.mediaType == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = this.mediaType.hashCode();
        }
        int hashCode9;
        if (this.name == null) {
            hashCode9 = 0;
        }
        else {
            hashCode9 = this.name.hashCode();
        }
        int hashCode10;
        if (this.screenshotImage == null) {
            hashCode10 = 0;
        }
        else {
            hashCode10 = this.screenshotImage.hashCode();
        }
        if (this.streamData != null) {
            hashCode = this.streamData.hashCode();
        }
        return (((((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode;
    }
    
    public Optional<Boolean> isFreeAvailable() {
        return Optional.fromNullable(this.isFreeAvailable);
    }
    
    public Optional<Boolean> isPremiumAvailable() {
        return Optional.fromNullable(this.isPremiumAvailable);
    }
    
    public void setAvailabilityNotes(final String availabilityNotes) {
        this.availabilityNotes = availabilityNotes;
    }
    
    public void setAvailableTime(final String availableTime) {
        this.availableTime = availableTime;
    }
    
    public void setCollectionId(final int collectionId) {
        this.collectionId = collectionId;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }
    
    public void setEpisodeNumber(final String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
    
    public void setFreeAvailable(final Boolean isFreeAvailable) {
        this.isFreeAvailable = isFreeAvailable;
    }
    
    public void setMediaId(final Long mediaId) {
        this.mediaId = mediaId;
    }
    
    public void setMediaType(final String mediaType) {
        this.mediaType = mediaType;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPlayhead(final Integer playhead) {
        this.playhead = playhead;
    }
    
    public void setPremiumAvailable(final Boolean isPremiumAvailable) {
        this.isPremiumAvailable = isPremiumAvailable;
    }
    
    public void setScreenshotImage(final ImageSet screenshotImage) {
        this.screenshotImage = screenshotImage;
    }
    
    public void setSeriesId(final int seriesId) {
        this.seriesId = seriesId;
    }
    
    public void setSeriesName(final String seriesName) {
        this.seriesName = seriesName;
    }
    
    public void setStreamData(final StreamData streamData) {
        this.streamData = streamData;
    }
    
    @Override
    public String toString() {
        return "Media [mediaId=" + this.mediaId + ", mediaType=" + this.mediaType + ", episodeNumber=" + this.episodeNumber + ", name=" + this.name + ", description=" + this.description + ", screenshotImage=" + this.screenshotImage + ", streamData=" + this.streamData + ", isPremiumAvailable=" + this.isPremiumAvailable + ", isFreeAvailable=" + this.isFreeAvailable + ", BIF url=" + this.bifUrl + ", availabilityNotes=" + this.availabilityNotes + "]";
    }
}
