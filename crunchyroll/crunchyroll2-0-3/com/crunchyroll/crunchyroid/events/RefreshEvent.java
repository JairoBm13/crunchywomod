// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

import com.crunchyroll.android.api.models.Category;
import java.util.ArrayList;

public class RefreshEvent
{
    private final String mFilter;
    private String mGAViewTrackingTag;
    private final String mMediaType;
    private ArrayList<Category> mSeasonItems;
    
    public RefreshEvent(final String s, final String s2) {
        this(s, s2, null);
    }
    
    public RefreshEvent(final String mMediaType, final String mFilter, final ArrayList<Category> mSeasonItems) {
        this.mMediaType = mMediaType;
        this.mFilter = mFilter;
        this.mSeasonItems = mSeasonItems;
    }
    
    public String getFilter() {
        return this.mFilter;
    }
    
    public String getGATrackingTag() {
        return this.mGAViewTrackingTag;
    }
    
    public String getMediaType() {
        return this.mMediaType;
    }
    
    public void setGAViewTrackingTag(final String mgaViewTrackingTag) {
        this.mGAViewTrackingTag = mgaViewTrackingTag;
    }
}
