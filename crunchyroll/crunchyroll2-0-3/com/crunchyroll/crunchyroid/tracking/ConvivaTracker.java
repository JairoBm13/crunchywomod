// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.tracking;

import com.crunchyroll.video.widget.AbstractVideoView;
import com.conviva.LivePass;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.content.Context;
import com.google.common.base.Optional;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.android.api.models.Media;
import com.conviva.ConvivaContentInfo;
import com.conviva.streamerProxies.brightcove.BrightCoveProxy;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.android.util.Logger;

public class ConvivaTracker
{
    private static final String ACCESS_TYPE_KEY = "accessType";
    private static final String CATEGORY_KEY = "category";
    private static final String CHANNEL_KEY = "channel";
    private static final String CONTENT_ID_KEY = "contentId";
    private static final String EPISODE_NAME_KEY = "episodeName";
    private static final String EPISODE_NUMBER_KEY = "episodeNumber";
    private static final String GENRES_KEY = "genres";
    private static final String PLAYER_VENDOR_KEY = "playerVendor";
    private static final String PLAYER_VERSION_KEY = "playerVersion";
    private static final String PUB_DATE_KEY = "pubDate";
    private static final String SEASON_ID_KEY = "seasonId";
    private static final String SHOW_ID_KEY = "showId";
    private static final String SHOW_TITLE_KEY = "showTitle";
    private static final String SITE_KEY = "site";
    private static final String VIDEO_QUALITY_KEY = "videoQuality";
    private final Logger logger;
    private ApplicationState mApplicationState;
    private BrightCoveProxy mBrightCoveProxy;
    private ConvivaContentInfo mConvivaMetadata;
    private Media mMedia;
    private Series mSeries;
    private Optional<Integer> mSessionId;
    private String mUserType;
    private final String mVideoQuality;
    
    public ConvivaTracker(final Context context, final Media mMedia, final Series mSeries) {
        this.logger = new Logger("ConvivaTracker");
        this.mSessionId = Optional.absent();
        this.mApplicationState = CrunchyrollApplication.getApp(context).getApplicationState();
        this.mMedia = mMedia;
        this.mSeries = mSeries;
        this.mUserType = CrunchyrollApplication.getApp(context).getUserState();
        this.mVideoQuality = CrunchyrollApplication.getApp(context).getApplicationState().getVideoQualityPreference(true);
    }
    
    public void adEnd() {
        if (!this.mSessionId.isPresent()) {
            return;
        }
        this.logger.debug("adEnd", new Object[0]);
        try {
            LivePass.adEnd(this.mSessionId.get());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void adStart() {
        if (!this.mSessionId.isPresent()) {
            return;
        }
        this.logger.debug("adStart", new Object[0]);
        try {
            LivePass.adStart(this.mSessionId.get());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void endSession() {
        if (this.mSessionId.isPresent()) {
            this.logger.debug("endSession", new Object[0]);
            LivePass.cleanupSession(this.mSessionId.get());
            this.mSessionId = Optional.absent();
        }
        if (this.mBrightCoveProxy != null) {
            this.mBrightCoveProxy.Cleanup();
        }
        this.mBrightCoveProxy = null;
    }
    
    public void reportError(final int n) {
        this.logger.debug("reportError", new Object[0]);
        if (!this.mSessionId.isPresent()) {
            return;
        }
        Label_0080: {
            String s = null;
            switch (n) {
                default: {
                    s = "Unknown error";
                    break;
                }
                case -1004: {
                    break Label_0080;
                }
                case -1010: {
                    break Label_0080;
                }
            }
            try {
                while (true) {
                    LivePass.reportError(this.mSessionId.get(), s, 1);
                    return;
                    s = "Media unsupported";
                    continue;
                    s = "Source not found";
                    continue;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void startSession(final AbstractVideoView abstractVideoView) {
        if (abstractVideoView == null) {
            throw new NullPointerException("Can't start session with null videoView");
        }
        if (this.mSessionId.isPresent()) {}
    }
}
