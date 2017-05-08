// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast.types;

import java.io.Serializable;

public class MediaFile implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String mimeType;
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
            final MediaFile mediaFile = (MediaFile)o;
            if (this.id == null) {
                if (mediaFile.id != null) {
                    return false;
                }
            }
            else if (!this.id.equals(mediaFile.id)) {
                return false;
            }
            if (this.mimeType == null) {
                if (mediaFile.mimeType != null) {
                    return false;
                }
            }
            else if (!this.mimeType.equals(mediaFile.mimeType)) {
                return false;
            }
            if (this.url == null) {
                if (mediaFile.url != null) {
                    return false;
                }
            }
            else if (!this.url.equals(mediaFile.url)) {
                return false;
            }
        }
        return true;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getMimeType() {
        return this.mimeType;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.id == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.id.hashCode();
        }
        int hashCode3;
        if (this.mimeType == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.mimeType.hashCode();
        }
        if (this.url != null) {
            hashCode = this.url.hashCode();
        }
        return ((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    @Override
    public String toString() {
        return "MediaFile [id=" + this.id + ", mimeType=" + this.mimeType + ", url=" + this.url + "]";
    }
}
