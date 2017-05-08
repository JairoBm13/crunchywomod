// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonProperty;
import android.os.Parcelable$Creator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Collection implements Parcelable
{
    public static final Parcelable$Creator<Collection> CREATOR;
    @JsonProperty("availability_notes")
    private String availabilityNotes;
    @JsonProperty("collection_id")
    private Long collectionId;
    @JsonProperty("complete")
    private Boolean complete;
    @JsonProperty("created")
    private String created;
    @JsonProperty("description")
    private String description;
    @JsonProperty("landscape_image")
    private ImageSet landscapeImage;
    @JsonProperty("mature")
    private Boolean mature;
    @JsonProperty("media_count")
    private Integer mediaCount;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("portrait_image")
    private ImageSet portraitImage;
    @JsonProperty("premium_only")
    private Boolean premiumOnly;
    @JsonProperty("season")
    private Integer season;
    @JsonProperty("series_id")
    private Long seriesId;
    @JsonProperty("series_name")
    private String seriesName;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Collection>() {
            public Collection createFromParcel(final Parcel parcel) {
                return new Collection(parcel, null);
            }
            
            public Collection[] newArray(final int n) {
                return new Collection[n];
            }
        };
    }
    
    public Collection() {
    }
    
    private Collection(final Parcel parcel) {
        this.collectionId = parcel.readLong();
        this.seriesId = parcel.readLong();
        this.name = parcel.readString();
        this.seriesName = parcel.readString();
        this.description = parcel.readString();
        this.mediaType = parcel.readString();
        this.season = parcel.readInt();
        this.created = parcel.readString();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getAvailabilityNotes() {
        return this.availabilityNotes;
    }
    
    public Long getCollectionId() {
        return this.collectionId;
    }
    
    public Boolean getComplete() {
        return this.complete;
    }
    
    public String getCreated() {
        return this.created;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public ImageSet getLandscapeImage() {
        return this.landscapeImage;
    }
    
    public Boolean getMature() {
        return this.mature;
    }
    
    public Integer getMediaCount() {
        return this.mediaCount;
    }
    
    public String getMediaType() {
        return this.mediaType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ImageSet getPortraitImage() {
        return this.portraitImage;
    }
    
    public Boolean getPremiumOnly() {
        return this.premiumOnly;
    }
    
    public Integer getSeason() {
        return this.season;
    }
    
    public Long getSeriesId() {
        return this.seriesId;
    }
    
    public String getSeriesName() {
        return this.seriesName;
    }
    
    public void setAvailabilityNotes(final String availabilityNotes) {
        this.availabilityNotes = availabilityNotes;
    }
    
    public void setCollectionId(final Long collectionId) {
        this.collectionId = collectionId;
    }
    
    public void setComplete(final Boolean complete) {
        this.complete = complete;
    }
    
    public void setCreated(final String created) {
        this.created = created;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setLandscapeImage(final ImageSet landscapeImage) {
        this.landscapeImage = landscapeImage;
    }
    
    public void setMature(final Boolean mature) {
        this.mature = mature;
    }
    
    public void setMediaCount(final Integer mediaCount) {
        this.mediaCount = mediaCount;
    }
    
    public void setMediaType(final String mediaType) {
        this.mediaType = mediaType;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPortraitImage(final ImageSet portraitImage) {
        this.portraitImage = portraitImage;
    }
    
    public void setPremiumOnly(final Boolean premiumOnly) {
        this.premiumOnly = premiumOnly;
    }
    
    public void setSeason(final Integer season) {
        this.season = season;
    }
    
    public void setSeriesId(final Long seriesId) {
        this.seriesId = seriesId;
    }
    
    public void setSeriesName(final String seriesName) {
        this.seriesName = seriesName;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeLong((long)this.collectionId);
        parcel.writeLong((long)this.seriesId);
        parcel.writeString(this.name);
        parcel.writeString(this.seriesName);
        parcel.writeString(this.description);
        parcel.writeString(this.mediaType);
        parcel.writeInt((int)this.season);
        parcel.writeString(this.created);
    }
}
