// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

public class LoadMore
{
    public static class HistoryEvent
    {
    }
    
    public static class SeriesEvent
    {
        private final String mFilter;
        private final String mMediaType;
        
        public SeriesEvent(final String mMediaType, final String mFilter) {
            this.mMediaType = mMediaType;
            this.mFilter = mFilter;
        }
        
        public String getFilter() {
            return this.mFilter;
        }
        
        public String getMediaType() {
            return this.mMediaType;
        }
    }
    
    public static class UpdatedEpisodesEvent
    {
    }
}
