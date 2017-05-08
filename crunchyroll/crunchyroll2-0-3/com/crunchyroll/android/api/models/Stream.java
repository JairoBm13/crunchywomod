// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import com.crunchyroll.android.util.DateFormatter;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public final class Stream implements Serializable
{
    private static final long serialVersionUID = 5095731797584310504L;
    @JsonProperty("bitrate")
    private Integer bitrate;
    @JsonProperty("expires")
    private Date expires;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("quality")
    private String quality;
    @JsonProperty("url")
    private String url;
    @JsonProperty("video_encode_id")
    private Long videoEncodeId;
    @JsonProperty("video_id")
    private Long videoId;
    @JsonProperty("width")
    private Integer width;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final Stream stream = (Stream)o;
            if (this.bitrate == null) {
                if (stream.bitrate != null) {
                    return false;
                }
            }
            else if (!this.bitrate.equals(stream.bitrate)) {
                return false;
            }
            if (this.expires == null) {
                if (stream.expires != null) {
                    return false;
                }
            }
            else if (!this.expires.equals(stream.expires)) {
                return false;
            }
            if (this.height == null) {
                if (stream.height != null) {
                    return false;
                }
            }
            else if (!this.height.equals(stream.height)) {
                return false;
            }
            if (this.quality == null) {
                if (stream.quality != null) {
                    return false;
                }
            }
            else if (!this.quality.equals(stream.quality)) {
                return false;
            }
            if (this.url == null) {
                if (stream.url != null) {
                    return false;
                }
            }
            else if (!this.url.equals(stream.url)) {
                return false;
            }
            if (this.videoEncodeId == null) {
                if (stream.videoEncodeId != null) {
                    return false;
                }
            }
            else if (!this.videoEncodeId.equals(stream.videoEncodeId)) {
                return false;
            }
            if (this.videoId == null) {
                if (stream.videoId != null) {
                    return false;
                }
            }
            else if (!this.videoId.equals(stream.videoId)) {
                return false;
            }
            if (this.width == null) {
                if (stream.width != null) {
                    return false;
                }
            }
            else if (!this.width.equals(stream.width)) {
                return false;
            }
        }
        return true;
    }
    
    public Integer getBitrate() {
        return this.bitrate;
    }
    
    public Date getExpires() {
        return this.expires;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public String getQuality() {
        return this.quality;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public Long getVideoEncodeId() {
        return this.videoEncodeId;
    }
    
    public Long getVideoId() {
        return this.videoId;
    }
    
    public Integer getWidth() {
        return this.width;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.bitrate == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.bitrate.hashCode();
        }
        int hashCode3;
        if (this.expires == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.expires.hashCode();
        }
        int hashCode4;
        if (this.height == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.height.hashCode();
        }
        int hashCode5;
        if (this.quality == null) {
            hashCode5 = 0;
        }
        else {
            hashCode5 = this.quality.hashCode();
        }
        int hashCode6;
        if (this.url == null) {
            hashCode6 = 0;
        }
        else {
            hashCode6 = this.url.hashCode();
        }
        int hashCode7;
        if (this.videoEncodeId == null) {
            hashCode7 = 0;
        }
        else {
            hashCode7 = this.videoEncodeId.hashCode();
        }
        int hashCode8;
        if (this.videoId == null) {
            hashCode8 = 0;
        }
        else {
            hashCode8 = this.videoId.hashCode();
        }
        if (this.width != null) {
            hashCode = this.width.hashCode();
        }
        return (((((((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode7) * 31 + hashCode8) * 31 + hashCode;
    }
    
    public void setBitrate(final Integer bitrate) {
        this.bitrate = bitrate;
    }
    
    public void setExpires(final String s) {
        this.expires = DateFormatter.parse(s);
    }
    
    public void setHeight(final Integer height) {
        this.height = height;
    }
    
    public void setQuality(final String quality) {
        this.quality = quality;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public void setVideoEncodeId(final Long videoEncodeId) {
        this.videoEncodeId = videoEncodeId;
    }
    
    public void setVideoId(final Long videoId) {
        this.videoId = videoId;
    }
    
    public void setWidth(final Integer width) {
        this.width = width;
    }
    
    @Override
    public String toString() {
        return "Stream [quality=" + this.quality + ", bitrate=" + this.bitrate + ", height=" + this.height + ", width=" + this.width + ", expires=" + this.expires + ", url=" + this.url + ", videoEncodeId=" + this.videoEncodeId + ", videoId=" + this.videoId + "]";
    }
}
