// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads.vast;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.secondtv.android.ads.vast.types.MediaFile;
import java.util.Collection;
import java.io.Serializable;

public class LinearAd implements Serializable
{
    private static final String DEFAULT_ID = "";
    private static final String EMPTY_URL = "";
    private static final long serialVersionUID = 1L;
    private String clickThroughUrl;
    private final Collection<String> clickTrackingUrls;
    private String downstreamAdUrl;
    private final Collection<String> errorUrls;
    private String id;
    private final Collection<String> impressionUrls;
    private final Collection<MediaFile> mediaFiles;
    private final Map<TrackingType, Collection<String>> trackingUrls;
    
    public LinearAd() {
        this.impressionUrls = new HashSet<String>();
        this.trackingUrls = new HashMap<TrackingType, Collection<String>>(TrackingType.values().length);
        this.clickTrackingUrls = new HashSet<String>();
        this.errorUrls = new HashSet<String>();
        this.mediaFiles = new HashSet<MediaFile>();
    }
    
    public void addClickTrackingUrl(final String s) {
        this.clickTrackingUrls.add(s);
    }
    
    public void addErrorUrl(final String s) {
        this.errorUrls.add(s);
    }
    
    public void addImpressionUrl(final String s) {
        this.impressionUrls.add(s);
    }
    
    public void addMediaFile(final MediaFile mediaFile) {
        this.mediaFiles.add(mediaFile);
    }
    
    public void addTrackingUrlWithType(final String s, final TrackingType trackingType) {
        if (this.trackingUrls.containsKey(trackingType)) {
            this.trackingUrls.get(trackingType).add(s);
            return;
        }
        final HashSet<String> set = new HashSet<String>();
        set.add(s);
        this.trackingUrls.put(trackingType, set);
    }
    
    public void clearDownstreamAdUrl() {
        this.downstreamAdUrl = null;
    }
    
    public String getClickThroughUrl() {
        if (this.clickThroughUrl != null) {
            return this.clickThroughUrl;
        }
        return "";
    }
    
    public Collection<String> getClickTrackingUrls() {
        return Collections.unmodifiableCollection((Collection<? extends String>)this.clickTrackingUrls);
    }
    
    public String getDownstreamAdUrl() {
        if (this.downstreamAdUrl != null) {
            return this.downstreamAdUrl;
        }
        return "";
    }
    
    public Collection<String> getErrorUrls() {
        return Collections.unmodifiableCollection((Collection<? extends String>)this.errorUrls);
    }
    
    public String getId() {
        if (this.id != null) {
            return this.id;
        }
        return "";
    }
    
    public Collection<String> getImpressionUrls() {
        return Collections.unmodifiableCollection((Collection<? extends String>)this.impressionUrls);
    }
    
    public Collection<MediaFile> getMediaFiles() {
        return Collections.unmodifiableCollection((Collection<? extends MediaFile>)this.mediaFiles);
    }
    
    public Collection<String> getTrackingUrls(final TrackingType trackingType) {
        if (this.trackingUrls.containsKey(trackingType)) {
            return Collections.unmodifiableCollection((Collection<? extends String>)this.trackingUrls.get(trackingType));
        }
        return (Collection<String>)Collections.emptySet();
    }
    
    public boolean hasDownstreamAdUrl() {
        return !this.getDownstreamAdUrl().equals("");
    }
    
    public void setClickThroughUrl(final String clickThroughUrl) {
        this.clickThroughUrl = clickThroughUrl;
    }
    
    public void setDownstreamAdUrl(final String downstreamAdUrl) {
        this.downstreamAdUrl = downstreamAdUrl;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
}
