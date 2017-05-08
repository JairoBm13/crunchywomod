// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast.model;

import java.io.Serializable;

public class CastInfo implements Serializable
{
    private final Integer mDuration;
    private final String mEpisodeName;
    private final String mFWideUrl;
    private final String mLocalizedCastTo;
    private final Long mMediaId;
    private Integer mPlayhead;
    private final String mSeriesName;
    private final Class mVideoPlayerClass;
    private final String mWideUrl;
    
    public CastInfo(final Class mVideoPlayerClass, final String mEpisodeName, final String mSeriesName, final Integer mPlayhead, final Long mMediaId, final String mfWideUrl, final String mWideUrl, final Integer mDuration, final String mLocalizedCastTo) {
        this.mVideoPlayerClass = mVideoPlayerClass;
        this.mEpisodeName = mEpisodeName;
        this.mSeriesName = mSeriesName;
        this.mPlayhead = mPlayhead;
        this.mMediaId = mMediaId;
        this.mFWideUrl = mfWideUrl;
        this.mWideUrl = mWideUrl;
        this.mDuration = mDuration;
        this.mLocalizedCastTo = mLocalizedCastTo;
    }
    
    public Integer getDuration() {
        return this.mDuration;
    }
    
    public String getEpisodeName() {
        return this.mEpisodeName;
    }
    
    public String getLocalizedCastTo() {
        return this.mLocalizedCastTo;
    }
    
    public Long getMediaId() {
        return this.mMediaId;
    }
    
    public Integer getPlayhead() {
        return this.mPlayhead;
    }
    
    public String getSeriesName() {
        return this.mSeriesName;
    }
    
    public Class getVideoPlayerClass() {
        return this.mVideoPlayerClass;
    }
    
    public String getWideUrl() {
        return this.mWideUrl;
    }
    
    public String getfWideUrl() {
        return this.mFWideUrl;
    }
    
    public void setPlayhead(final Integer mPlayhead) {
        this.mPlayhead = mPlayhead;
    }
}
