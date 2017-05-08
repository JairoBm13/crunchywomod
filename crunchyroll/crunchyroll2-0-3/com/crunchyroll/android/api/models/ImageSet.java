// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.google.common.base.Optional;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ImageSet implements Serializable
{
    public static final ImageSet DEFAULT;
    private static final long serialVersionUID = 2468618963718986617L;
    @JsonProperty("fwidestar_url")
    private String fWideStarUrl;
    @JsonProperty("fwide_url")
    private String fWideUrl;
    @JsonProperty("full_url")
    private String fullUrl;
    @JsonProperty("height")
    private String height;
    @JsonProperty("large_url")
    private String largeUrl;
    @JsonProperty("medium_url")
    private String mediumUrl;
    @JsonProperty("small_url")
    private String smallUrl;
    @JsonProperty("thumb_url")
    private String thumbUrl;
    @JsonProperty("widestar_url")
    private String wideStarUrl;
    @JsonProperty("wide_url")
    private String wideUrl;
    @JsonProperty("width")
    private String width;
    
    static {
        DEFAULT = new ImageSet();
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
            final ImageSet set = (ImageSet)o;
            if (this.fWideStarUrl == null) {
                if (set.fWideStarUrl != null) {
                    return false;
                }
            }
            else if (!this.fWideStarUrl.equals(set.fWideStarUrl)) {
                return false;
            }
            if (this.fWideUrl == null) {
                if (set.fWideUrl != null) {
                    return false;
                }
            }
            else if (!this.fWideUrl.equals(set.fWideUrl)) {
                return false;
            }
            if (this.fullUrl == null) {
                if (set.fullUrl != null) {
                    return false;
                }
            }
            else if (!this.fullUrl.equals(set.fullUrl)) {
                return false;
            }
            if (this.height == null) {
                if (set.height != null) {
                    return false;
                }
            }
            else if (!this.height.equals(set.height)) {
                return false;
            }
            if (this.largeUrl == null) {
                if (set.largeUrl != null) {
                    return false;
                }
            }
            else if (!this.largeUrl.equals(set.largeUrl)) {
                return false;
            }
            if (this.mediumUrl == null) {
                if (set.mediumUrl != null) {
                    return false;
                }
            }
            else if (!this.mediumUrl.equals(set.mediumUrl)) {
                return false;
            }
            if (this.smallUrl == null) {
                if (set.smallUrl != null) {
                    return false;
                }
            }
            else if (!this.smallUrl.equals(set.smallUrl)) {
                return false;
            }
            if (this.thumbUrl == null) {
                if (set.thumbUrl != null) {
                    return false;
                }
            }
            else if (!this.thumbUrl.equals(set.thumbUrl)) {
                return false;
            }
            if (this.wideStarUrl == null) {
                if (set.wideStarUrl != null) {
                    return false;
                }
            }
            else if (!this.wideStarUrl.equals(set.wideStarUrl)) {
                return false;
            }
            if (this.wideUrl == null) {
                if (set.wideUrl != null) {
                    return false;
                }
            }
            else if (!this.wideUrl.equals(set.wideUrl)) {
                return false;
            }
            if (this.width == null) {
                if (set.width != null) {
                    return false;
                }
            }
            else if (!this.width.equals(set.width)) {
                return false;
            }
        }
        return true;
    }
    
    public Optional<String> getFullUrl() {
        return Optional.fromNullable(this.fullUrl);
    }
    
    public String getHeight() {
        return this.height;
    }
    
    public Optional<String> getLargeUrl() {
        return Optional.fromNullable(this.largeUrl);
    }
    
    public Optional<String> getMediumUrl() {
        return Optional.fromNullable(this.mediumUrl);
    }
    
    public Optional<String> getSmallUrl() {
        return Optional.fromNullable(this.smallUrl);
    }
    
    public Optional<String> getThumbUrl() {
        return Optional.fromNullable(this.thumbUrl);
    }
    
    public Optional<String> getWideStarUrl() {
        return Optional.fromNullable(this.wideStarUrl);
    }
    
    public Optional<String> getWideUrl() {
        return Optional.fromNullable(this.wideUrl);
    }
    
    public String getWidth() {
        return this.width;
    }
    
    public Optional<String> getfWideStarUrl() {
        return Optional.fromNullable(this.fWideStarUrl);
    }
    
    public Optional<String> getfWideUrl() {
        return Optional.fromNullable(this.fWideUrl);
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.fWideStarUrl == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.fWideStarUrl.hashCode();
        }
        int hashCode3;
        if (this.fWideUrl == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.fWideUrl.hashCode();
        }
        int hashCode4;
        if (this.fullUrl == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.fullUrl.hashCode();
        }
        int hashCode5;
        if (this.height == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.height.hashCode();
        }
        int hashCode6;
        if (this.largeUrl == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.largeUrl.hashCode();
        }
        int hashCode7;
        if (this.mediumUrl == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.mediumUrl.hashCode();
        }
        int hashCode8;
        if (this.smallUrl == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = this.smallUrl.hashCode();
        }
        int hashCode9;
        if (this.thumbUrl == null) {
            hashCode9 = 0;
        }
        else {
            hashCode9 = this.thumbUrl.hashCode();
        }
        int hashCode10;
        if (this.wideStarUrl == null) {
            hashCode10 = 0;
        }
        else {
            hashCode10 = this.wideStarUrl.hashCode();
        }
        int hashCode11;
        if (this.wideUrl == null) {
            hashCode11 = 0;
        }
        else {
            hashCode11 = this.wideUrl.hashCode();
        }
        if (this.width != null) {
            hashCode = this.width.hashCode();
        }
        return ((((((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode11) * 31 + hashCode;
    }
    
    public void setFullUrl(final String fullUrl) {
        this.fullUrl = fullUrl;
    }
    
    public void setHeight(final String height) {
        this.height = height;
    }
    
    public void setLargeUrl(final String largeUrl) {
        this.largeUrl = largeUrl;
    }
    
    public void setMediumUrl(final String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }
    
    public void setSmallUrl(final String smallUrl) {
        this.smallUrl = smallUrl;
    }
    
    public void setThumbUrl(final String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    
    public void setWideStarUrl(final String wideStarUrl) {
        this.wideStarUrl = wideStarUrl;
    }
    
    public void setWideUrl(final String wideUrl) {
        this.wideUrl = wideUrl;
    }
    
    public void setWidth(final String width) {
        this.width = width;
    }
    
    public void setfWideStarUrl(final String fWideStarUrl) {
        this.fWideStarUrl = fWideStarUrl;
    }
    
    public void setfWideUrl(final String fWideUrl) {
        this.fWideUrl = fWideUrl;
    }
    
    @Override
    public String toString() {
        return "ImageSet [thumbUrl=" + this.thumbUrl + ", smallUrl=" + this.smallUrl + ", mediumUrl=" + this.mediumUrl + ", largeUrl=" + this.largeUrl + ", fullUrl=" + this.fullUrl + ", wideUrl=" + this.wideUrl + ", wideStarUrl=" + this.wideStarUrl + ", fWideUrl=" + this.fWideUrl + ", fWideStarUrl=" + this.fWideStarUrl + ", width=" + this.width + ", height=" + this.height + "]";
    }
}
