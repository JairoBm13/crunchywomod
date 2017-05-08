// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class StreamData implements Serializable
{
    private static final long serialVersionUID = -7984230243384741258L;
    @JsonProperty("audio_lang")
    private String audioLanguage;
    @JsonProperty("format")
    private String format;
    @JsonProperty("hardsub_lang")
    private String hardsubLanguage;
    @JsonProperty("streams")
    private List<Stream> streams;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final StreamData streamData = (StreamData)o;
            if (this.audioLanguage == null) {
                if (streamData.audioLanguage != null) {
                    return false;
                }
            }
            else if (!this.audioLanguage.equals(streamData.audioLanguage)) {
                return false;
            }
            if (this.format == null) {
                if (streamData.format != null) {
                    return false;
                }
            }
            else if (!this.format.equals(streamData.format)) {
                return false;
            }
            if (this.hardsubLanguage == null) {
                if (streamData.hardsubLanguage != null) {
                    return false;
                }
            }
            else if (!this.hardsubLanguage.equals(streamData.hardsubLanguage)) {
                return false;
            }
            if (this.streams == null) {
                if (streamData.streams != null) {
                    return false;
                }
            }
            else if (!this.streams.equals(streamData.streams)) {
                return false;
            }
        }
        return true;
    }
    
    public String getAudioLanguage() {
        return this.audioLanguage;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public String getHardsubLanguage() {
        return this.hardsubLanguage;
    }
    
    public List<Stream> getStreams() {
        return this.streams;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.audioLanguage == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.audioLanguage.hashCode();
        }
        int hashCode3;
        if (this.format == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.format.hashCode();
        }
        int hashCode4;
        if (this.hardsubLanguage == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.hardsubLanguage.hashCode();
        }
        if (this.streams != null) {
            hashCode = this.streams.hashCode();
        }
        return (((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
    }
    
    public void setAudioLanguage(final String audioLanguage) {
        this.audioLanguage = audioLanguage;
    }
    
    public void setFormat(final String format) {
        this.format = format;
    }
    
    public void setHardsubLanguage(final String hardsubLanguage) {
        this.hardsubLanguage = hardsubLanguage;
    }
    
    public void setStreams(final List<Stream> streams) {
        this.streams = streams;
    }
    
    @Override
    public String toString() {
        return "StreamData [hardsubLanguage=" + this.hardsubLanguage + ", audioLanguage=" + this.audioLanguage + ", format=" + this.format + ", streams=" + this.streams + "]";
    }
}
