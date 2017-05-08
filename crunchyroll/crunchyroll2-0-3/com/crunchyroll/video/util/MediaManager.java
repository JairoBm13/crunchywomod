// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.util;

import java.util.Collections;
import com.crunchyroll.android.api.models.SeriesInfoWithMedia;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.android.api.ApiManager;
import android.app.Activity;
import java.util.ArrayList;
import com.crunchyroll.android.api.models.Media;
import java.util.List;
import com.crunchyroll.android.api.models.EpisodeInfo;

public class MediaManager
{
    private static MediaManager mInstance;
    private EpisodeInfo mCurrent;
    private boolean mFetching;
    private int mMediaIndex;
    private List<Media> mMediaList;
    private EpisodeInfo mNext;
    
    static {
        MediaManager.mInstance = null;
    }
    
    private MediaManager() {
        this.mMediaList = new ArrayList<Media>();
        this.mMediaIndex = 0;
        this.mCurrent = null;
        this.mNext = null;
        this.mFetching = false;
    }
    
    private String fetchMedia(final Activity activity, final int n, final MediaManagerListener mediaManagerListener) {
        if (n < this.mMediaList.size() && n >= 0) {
            final Media media = this.mMediaList.get(n);
            return ApiManager.getInstance(activity).getEpisodeInfoTask(media.getMediaId(), (long)media.getSeriesId(), new BaseListener<EpisodeInfo>() {
                @Override
                public void onException(final Exception ex) {
                    mediaManagerListener.onException(ex);
                }
                
                @Override
                public void onFinally() {
                    mediaManagerListener.onFinally();
                }
                
                @Override
                public void onSuccess(final EpisodeInfo episodeInfo) {
                    mediaManagerListener.onNextEpisodeReceived(episodeInfo);
                }
            });
        }
        return null;
    }
    
    public static MediaManager getInstance() {
        if (MediaManager.mInstance == null) {
            MediaManager.mInstance = new MediaManager();
        }
        return MediaManager.mInstance;
    }
    
    public String createPlaylistFromStartMedia(final Activity activity, final Media media, final CreatePlaylistListener createPlaylistListener) {
        if (media == null) {
            throw new IllegalArgumentException("The media value cannot be null");
        }
        this.init();
        return ApiManager.getInstance(activity).getMediaListData(new Long(media.getSeriesId()), null, new BaseListener<SeriesInfoWithMedia>() {
            @Override
            public void onException(final Exception ex) {
                createPlaylistListener.onException(ex);
            }
            
            @Override
            public void onSuccess(final SeriesInfoWithMedia seriesInfoWithMedia) {
                final int n = -1;
                int n2 = 0;
                int n3;
                while (true) {
                    n3 = n;
                    if (n2 >= seriesInfoWithMedia.getMedias().size()) {
                        break;
                    }
                    if (media.getMediaId().equals(seriesInfoWithMedia.getMedias().get(n2).getMediaId())) {
                        n3 = n2;
                        break;
                    }
                    ++n2;
                }
                MediaManager.this.mMediaList = seriesInfoWithMedia.getMedias().subList(0, n3 + 1);
                MediaManager.this.mMediaIndex = MediaManager.this.mMediaList.size();
                createPlaylistListener.onPlaylistCreated();
            }
        });
    }
    
    public String getNext(final Activity activity, final MediaManagerListener mediaManagerListener) {
        String fetchMedia = null;
        if (this.mCurrent != null && !this.isFetching()) {
            this.mFetching = true;
            this.mCurrent = this.mNext;
            --this.mMediaIndex;
            if (mediaManagerListener != null) {
                mediaManagerListener.onNextEpisodeReceived(this.mCurrent);
            }
            fetchMedia = this.fetchMedia(activity, this.mMediaIndex - 1, (MediaManagerListener)new BaseVideoManagerListener() {
                @Override
                public void onFinally() {
                    MediaManager.this.mFetching = false;
                }
                
                @Override
                public void onNextEpisodeReceived(final EpisodeInfo episodeInfo) {
                    MediaManager.this.mNext = episodeInfo;
                }
            });
        }
        else if (!this.isFetching()) {
            this.mFetching = true;
            final String fetchMedia2 = this.fetchMedia(activity, this.mMediaIndex - 1, (MediaManagerListener)new BaseVideoManagerListener() {
                @Override
                public void onException(final Exception ex) {
                    if (mediaManagerListener != null) {
                        mediaManagerListener.onException(ex);
                    }
                }
                
                @Override
                public void onFinally() {
                    MediaManager.this.mFetching = false;
                    if (mediaManagerListener != null) {
                        mediaManagerListener.onFinally();
                    }
                }
                
                @Override
                public void onNextEpisodeReceived(final EpisodeInfo episodeInfo) {
                    MediaManager.this.mCurrent = episodeInfo;
                    MediaManager.this.mMediaIndex--;
                    if (mediaManagerListener != null) {
                        mediaManagerListener.onNextEpisodeReceived(MediaManager.this.mCurrent);
                    }
                }
            });
            this.fetchMedia(activity, this.mMediaIndex - 2, (MediaManagerListener)new BaseVideoManagerListener() {
                @Override
                public void onNextEpisodeReceived(final EpisodeInfo episodeInfo) {
                    MediaManager.this.mNext = episodeInfo;
                }
            });
            return fetchMedia2;
        }
        return fetchMedia;
    }
    
    public List<Media> getRemainingPlaylist() {
        return Collections.synchronizedList(this.mMediaList).subList(0, this.mMediaIndex + 1);
    }
    
    public boolean hasMoreMedia() {
        return this.mMediaIndex > 0;
    }
    
    public void init() {
        this.mMediaIndex = 0;
        this.mCurrent = null;
        this.mNext = null;
        this.mFetching = false;
    }
    
    public boolean isFetching() {
        return this.mFetching;
    }
    
    public void reInit() {
        final List<Media> synchronizedList = Collections.synchronizedList(this.mMediaList);
        int n2;
        int n = n2 = synchronizedList.size() - 1;
        if (this.mCurrent != null) {
            int n3 = 0;
            while (true) {
                n2 = n;
                if (n3 >= synchronizedList.size()) {
                    break;
                }
                if (synchronizedList.get(n3).getMediaId().equals(this.mCurrent.getMedia().getMediaId())) {
                    n = n3;
                }
                ++n3;
            }
        }
        this.init();
        this.mMediaIndex = n2 + 1;
    }
    
    public void setPlaylist(final List<Media> mMediaList) {
        if (mMediaList != null) {
            this.init();
            this.mMediaIndex = mMediaList.size();
            this.mMediaList = mMediaList;
            return;
        }
        throw new IllegalArgumentException("The media list value cannot be null");
    }
    
    public abstract static class BaseVideoManagerListener implements MediaManagerListener
    {
        @Override
        public void onException(final Exception ex) {
        }
        
        @Override
        public void onFinally() {
        }
        
        @Override
        public void onNextEpisodeReceived(final EpisodeInfo episodeInfo) {
        }
    }
    
    public interface CreatePlaylistListener
    {
        void onException(final Exception p0);
        
        void onPlaylistCreated();
    }
    
    public interface MediaManagerListener
    {
        void onException(final Exception p0);
        
        void onFinally();
        
        void onNextEpisodeReceived(final EpisodeInfo p0);
    }
}
