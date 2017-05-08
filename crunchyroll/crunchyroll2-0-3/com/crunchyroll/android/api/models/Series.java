// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.google.common.base.Optional;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Series implements Serializable
{
    public static final int SERVER_RATING_MAXIMUM = 100;
    public static final float STAR_RATING_MAXIMUM = 5.0f;
    private static final long serialVersionUID = -1341647440586175012L;
    @JsonProperty("description")
    private String description;
    @JsonProperty("genres")
    private List<String> genres;
    @JsonProperty("in_queue")
    private Boolean inQueue;
    @JsonProperty("landscape_image")
    private ImageSet landscapeImage;
    @JsonProperty("media_count")
    private Integer mediaCount;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("portrait_image")
    private ImageSet portraitImage;
    @JsonProperty("publisher_name")
    private String publisherName;
    @JsonProperty("rating")
    private Integer rating;
    @JsonProperty("series_id")
    private Long seriesId;
    @JsonProperty("url")
    private String url;
    @JsonProperty("year")
    private String year;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final Series series = (Series)o;
            if (this.description == null) {
                if (series.description != null) {
                    return false;
                }
            }
            else if (!this.description.equals(series.description)) {
                return false;
            }
            if (this.inQueue == null) {
                if (series.inQueue != null) {
                    return false;
                }
            }
            else if (!this.inQueue.equals(series.inQueue)) {
                return false;
            }
            if (this.landscapeImage == null) {
                if (series.landscapeImage != null) {
                    return false;
                }
            }
            else if (!this.landscapeImage.equals(series.landscapeImage)) {
                return false;
            }
            if (this.mediaCount == null) {
                if (series.mediaCount != null) {
                    return false;
                }
            }
            else if (!this.mediaCount.equals(series.mediaCount)) {
                return false;
            }
            if (this.mediaType == null) {
                if (series.mediaType != null) {
                    return false;
                }
            }
            else if (!this.mediaType.equals(series.mediaType)) {
                return false;
            }
            if (this.name == null) {
                if (series.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(series.name)) {
                return false;
            }
            if (this.portraitImage == null) {
                if (series.portraitImage != null) {
                    return false;
                }
            }
            else if (!this.portraitImage.equals(series.portraitImage)) {
                return false;
            }
            if (this.publisherName == null) {
                if (series.publisherName != null) {
                    return false;
                }
            }
            else if (!this.publisherName.equals(series.publisherName)) {
                return false;
            }
            if (this.seriesId == null) {
                if (series.seriesId != null) {
                    return false;
                }
            }
            else if (!this.seriesId.equals(series.seriesId)) {
                return false;
            }
            if (this.year == null) {
                if (series.year != null) {
                    return false;
                }
            }
            else if (!this.year.equals(series.year)) {
                return false;
            }
            if (this.rating == null) {
                if (series.rating != null) {
                    return false;
                }
            }
            else if (!this.rating.equals(series.rating)) {
                return false;
            }
            if (this.url == null) {
                if (series.url != null) {
                    return false;
                }
            }
            else if (!this.url.equals(series.url)) {
                return false;
            }
        }
        return true;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public List<String> getGenres() {
        return this.genres;
    }
    
    public Optional<Boolean> getInQueue() {
        return Optional.fromNullable(this.inQueue);
    }
    
    public ImageSet getLandscapeImage() {
        if (this.portraitImage == null) {
            return ImageSet.DEFAULT;
        }
        return this.portraitImage;
    }
    
    public Optional<Integer> getMediaCount() {
        return Optional.fromNullable(this.mediaCount);
    }
    
    public Optional<String> getMediaType() {
        return Optional.fromNullable(this.mediaType);
    }
    
    public String getName() {
        return this.name;
    }
    
    public ImageSet getPortraitImage() {
        if (this.portraitImage == null) {
            return ImageSet.DEFAULT;
        }
        return this.portraitImage;
    }
    
    public Optional<String> getPublisherName() {
        return Optional.fromNullable(this.publisherName);
    }
    
    public float getRating() {
        if (this.rating == null) {
            return 0.0f;
        }
        return (float)(5.0 * (this.rating / 100.0));
    }
    
    public Long getSeriesId() {
        return this.seriesId;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public Optional<String> getYear() {
        return Optional.fromNullable(this.year);
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.description == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.description.hashCode();
        }
        int hashCode3;
        if (this.inQueue == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.inQueue.hashCode();
        }
        int hashCode4;
        if (this.landscapeImage == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.landscapeImage.hashCode();
        }
        int hashCode5;
        if (this.mediaCount == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.mediaCount.hashCode();
        }
        int hashCode6;
        if (this.mediaType == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.mediaType.hashCode();
        }
        int hashCode7;
        if (this.name == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.name.hashCode();
        }
        int hashCode8;
        if (this.portraitImage == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = this.portraitImage.hashCode();
        }
        int hashCode9;
        if (this.publisherName == null) {
            hashCode9 = 0;
        }
        else {
            hashCode9 = this.publisherName.hashCode();
        }
        int hashCode10;
        if (this.seriesId == null) {
            hashCode10 = 0;
        }
        else {
            hashCode10 = this.seriesId.hashCode();
        }
        int hashCode11;
        if (this.year == null) {
            hashCode11 = 0;
        }
        else {
            hashCode11 = this.year.hashCode();
        }
        int hashCode12;
        if (this.rating == null) {
            hashCode12 = 0;
        }
        else {
            hashCode12 = this.rating.hashCode();
        }
        if (this.url != null) {
            hashCode = this.url.hashCode();
        }
        return (((((((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode11) * 31 + hashCode12) * 31 + hashCode;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setInQueue(final Boolean inQueue) {
        this.inQueue = inQueue;
    }
    
    public void setLandscapeImage(final ImageSet landscapeImage) {
        this.landscapeImage = landscapeImage;
    }
    
    public void setMediaCount(final Integer mediaCount) {
        this.mediaCount = mediaCount;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPortraitImage(final ImageSet portraitImage) {
        this.portraitImage = portraitImage;
    }
    
    public void setPublisherName(final String publisherName) {
        this.publisherName = publisherName;
    }
    
    public void setRating(final Integer rating) {
        this.rating = rating;
    }
    
    public void setSeriesId(final Long seriesId) {
        this.seriesId = seriesId;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public void setYear(final String year) {
        this.year = year;
    }
    
    @Override
    public String toString() {
        return "Series [seriesId=" + this.seriesId + ", name=" + this.name + ", mediaType=" + this.mediaType + ", portraitImage=" + this.portraitImage + ", landscapeImage=" + this.landscapeImage + ", description=" + this.description + ", mediaCount=" + this.mediaCount + ", publisherName=" + this.publisherName + ", year=" + this.year + ", inQueue=" + this.inQueue + "]";
    }
}
