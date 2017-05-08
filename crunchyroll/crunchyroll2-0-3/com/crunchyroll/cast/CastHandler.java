// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import java.io.InputStream;
import android.graphics.BitmapFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import roboguice.util.SafeAsyncTask;
import android.os.Bundle;
import java.util.Iterator;
import com.google.android.gms.common.ConnectionResult;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.view.MenuItem;
import android.support.v7.app.MediaRouteDialogFactory;
import android.support.v7.app.MediaRouteButton;
import com.google.android.gms.cast.RemoteMediaPlayer;
import com.google.android.gms.cast.MediaStatus;
import android.net.Uri;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.Serializable;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import java.io.IOException;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastMediaControlIntent;
import android.util.Log;
import com.google.android.gms.cast.CastDevice;
import android.support.v7.media.MediaRouteSelector;
import java.util.ArrayList;
import android.graphics.Bitmap;
import com.crunchyroll.cast.model.CastInfo;
import android.content.Context;
import com.google.common.base.Optional;
import android.support.v7.media.MediaRouter;
import com.google.android.gms.common.api.GoogleApiClient;
import android.content.IntentFilter;

public class CastHandler
{
    public static final String CAST_ADS_START_EVENT = "CAST_ADS_START_EVENT";
    public static final String CAST_ADS_STOP_EVENT = "CAST_ADS_STOP_EVENT";
    public static final String CAST_INFO_EXTRA = "castInfo";
    public static final String CAST_NO_ROUTES_AVAILABLE_EVENT = "CAST_NO_ROUTES_AVAILABLE_EVENT";
    public static final String CAST_ROUTES_AVAILABLE_EVENT = "CAST_ROUTES_AVAILABLE_EVENT";
    public static final String CAST_SESSION_ENDED_EVENT = "CAST_SESSION_ENDED_EVENT";
    public static final String CAST_SESSION_LOST_EVENT = "CAST_SESSION_LOST_EVENT";
    public static final String CAST_SESSION_STARTED_EVENT = "CAST_SESSION_STARTED_EVENT";
    public static final String CAST_SESSION_STARTING_EVENT = "CAST_SESSION_STARTING_EVENT";
    public static final String CAST_VIDEO_COMPLETION_EVENT = "CAST_VIDEO_COMPLETION_EVENT";
    public static final String CAST_VIDEO_STARTED_EVENT = "CAST_VIDEO_STARTED_EVENT";
    public static final IntentFilter INTENT_FILTER;
    public static final IntentFilter SESSION_INTENT_FILTER;
    private static final String TAG;
    private static String deviceType;
    private static CastEventListener eventListener;
    private static String mCastApplicationName;
    private static CastHandler mCastHandler;
    private static String mLocale;
    private GoogleApiClient mApiClient;
    private MediaRouter.RouteInfo mAutoReconnectRouteInfo;
    private Optional<AutoResumeData> mAutoResumeData;
    private final Context mContext;
    private Optional<CastInfo> mCurrentCastInfo;
    private CustomChannel mCustomChannel;
    private boolean mIsAnyRouteAvailable;
    private boolean mIsAutoreconnectToSession;
    private boolean mIsConnectionSuspended;
    private Optional<Long> mLastKnownPlayhead;
    private LockScreenHelper mLockScreenHelper;
    private Bitmap mMediaImage;
    private ArrayList<OnMediaChangedListener> mMediaImageChangedListeners;
    private MediaRouter mMediaRouter;
    private NotificationHelper mNotificationHelper;
    private ArrayList<OnPlaybackStateChangedListener> mPlaybackStateChangedListeners;
    private PlayheadListener mPlayheadListener;
    private float mPlayheadThreshold;
    private RemotePlayer mRemotePlayer;
    private RemotePlayerListener mRemotePlayerListener;
    private Optional<Integer> mRequestedPlayState;
    private MediaRouteSelector mRouteSelector;
    private RouteStateChangeCallback mRouteStateChangeCallback;
    private CastDevice mSelectedDevice;
    private CastState mState;
    
    static {
        INTENT_FILTER = new IntentFilter();
        SESSION_INTENT_FILTER = new IntentFilter();
        CastHandler.INTENT_FILTER.addAction("CAST_VIDEO_STARTED_EVENT");
        CastHandler.INTENT_FILTER.addAction("CAST_VIDEO_COMPLETION_EVENT");
        CastHandler.INTENT_FILTER.addAction("CAST_ADS_START_EVENT");
        CastHandler.INTENT_FILTER.addAction("CAST_ADS_STOP_EVENT");
        CastHandler.SESSION_INTENT_FILTER.addAction("CAST_SESSION_STARTING_EVENT");
        CastHandler.SESSION_INTENT_FILTER.addAction("CAST_SESSION_STARTED_EVENT");
        CastHandler.SESSION_INTENT_FILTER.addAction("CAST_SESSION_ENDED_EVENT");
        CastHandler.SESSION_INTENT_FILTER.addAction("CAST_SESSION_LOST_EVENT");
        TAG = CastHandler.class.getName();
        CastHandler.mCastHandler = null;
        CastHandler.mCastApplicationName = null;
        CastHandler.eventListener = null;
        CastHandler.mLocale = "enUS";
    }
    
    private CastHandler(final Context mContext, final String mCastApplicationName) {
        this.mIsAnyRouteAvailable = false;
        this.mState = CastState.IDLE;
        this.mIsAutoreconnectToSession = false;
        this.mIsConnectionSuspended = false;
        this.mAutoReconnectRouteInfo = null;
        this.mLastKnownPlayhead = Optional.absent();
        this.mCurrentCastInfo = Optional.absent();
        this.mAutoResumeData = Optional.absent();
        this.mRequestedPlayState = Optional.absent();
        Log.i(CastHandler.TAG, "CastService Constructor");
        this.mContext = mContext;
        CastHandler.mCastApplicationName = mCastApplicationName;
        this.mPlaybackStateChangedListeners = new ArrayList<OnPlaybackStateChangedListener>();
        this.mMediaImageChangedListeners = new ArrayList<OnMediaChangedListener>();
        if (isCastSupported(this.mContext)) {
            Log.i(CastHandler.TAG, "Chromecast supported, setting up...");
            this.mNotificationHelper = new NotificationHelper(this.mContext);
            this.mLockScreenHelper = new LockScreenHelper(this.mContext);
            this.mRouteSelector = new MediaRouteSelector.Builder().addControlCategory(CastMediaControlIntent.categoryForCast(CastHandler.mCastApplicationName)).build();
            this.mMediaRouter = MediaRouter.getInstance(this.mContext);
            this.mRouteStateChangeCallback = new RouteStateChangeCallback();
            this.removeFromNotificationCenter();
        }
    }
    
    private void attachChannels() {
        this.mRemotePlayerListener = new RemotePlayerListener();
        this.mRemotePlayer = new RemotePlayer(CastHandler.deviceType, (RemotePlayer.OnVideoMessageListener)this.mRemotePlayerListener);
        this.mCustomChannel = new CustomChannel(this.mApiClient);
        try {
            Cast.CastApi.setMessageReceivedCallbacks(this.mApiClient, this.mRemotePlayer.getNamespace(), this.mRemotePlayer);
            Cast.CastApi.setMessageReceivedCallbacks(this.mApiClient, CustomChannel.getNamespace(), this.mCustomChannel);
            if (true) {
                this.mRemotePlayer.requestStatus(this.mApiClient);
                this.setState(CastState.CONNECTED);
                return;
            }
            Log.e(CastHandler.TAG, "Remote channels were not attached.");
            this.teardown();
        }
        catch (IllegalStateException ex) {
            ex.printStackTrace();
            if (false) {
                this.mRemotePlayer.requestStatus(this.mApiClient);
                this.setState(CastState.CONNECTED);
                return;
            }
            Log.e(CastHandler.TAG, "Remote channels were not attached.");
            this.teardown();
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            if (false) {
                this.mRemotePlayer.requestStatus(this.mApiClient);
                this.setState(CastState.CONNECTED);
                return;
            }
            Log.e(CastHandler.TAG, "Remote channels were not attached.");
            this.teardown();
        }
        finally {
            while (true) {
                if (false) {
                    this.mRemotePlayer.requestStatus(this.mApiClient);
                    this.setState(CastState.CONNECTED);
                    break Label_0239;
                }
                Log.e(CastHandler.TAG, "Remote channels were not attached.");
                this.teardown();
                break Label_0239;
                continue;
            }
        }
    }
    
    private final void broadcastEvent(final String s) {
        Log.d(CastHandler.TAG, "Broadcast: " + s);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(new Intent(s));
    }
    
    private void broadcastSessionEnded() {
        Intent intent2;
        if (this.isCastRouteAvailable(this.mRouteSelector)) {
            Log.d(CastHandler.TAG, "Session ended");
            if (this.mCustomChannel != null) {
                this.mCustomChannel.sendDisconnectMessage();
            }
            final Intent intent = new Intent("CAST_SESSION_ENDED_EVENT");
            Optional<Long> optional2;
            final Optional<Object> optional = (Optional<Object>)(optional2 = (Optional<Long>)Optional.absent());
            if (this.mRemotePlayer != null) {
                optional2 = (Optional<Long>)optional;
                if (this.mRemotePlayer.isOnlyConnectedDevice()) {
                    optional2 = Optional.of(this.mRemotePlayer.getApproximateStreamPosition());
                }
            }
            intent2 = intent;
            if (this.mCurrentCastInfo.isPresent()) {
                final CastInfo castInfo = this.mCurrentCastInfo.get();
                if (optional2.isPresent()) {
                    castInfo.setPlayhead((int)(Object)optional2.get() / 1000);
                    intent.putExtra("castInfo", (Serializable)castInfo);
                    Log.v(CastHandler.TAG, "Passing playhead " + optional2.get() + " for episode " + castInfo.getEpisodeName() + " in intent");
                    intent2 = intent;
                }
                else {
                    Log.v(CastHandler.TAG, "Not passing playhead in intent");
                    intent2 = intent;
                }
            }
        }
        else {
            Log.d(CastHandler.TAG, "Session lost");
            intent2 = new Intent("CAST_SESSION_LOST_EVENT");
        }
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent2);
    }
    
    private void checkAndPlayPendingMedia() {
        if (this.mCurrentCastInfo.isPresent() && this.mAutoResumeData.isPresent()) {
            Log.d(CastHandler.TAG, "Found auto-resume data");
            final AutoResumeData autoResumeData = this.mAutoResumeData.get();
            if (this.mRemotePlayer.getMediaId().isPresent() && this.mRemotePlayer.getMediaId().get() == autoResumeData.mediaId) {
                Log.d(CastHandler.TAG, "Auto-resume data has same mediaId " + autoResumeData.mediaId + " as current media - seeking.");
                this.seekTo(autoResumeData.playhead);
            }
            else {
                Log.d(CastHandler.TAG, "Auto-resume data is different from current media - loading new media.");
                this.mRemotePlayer.load(this.mApiClient, this.mCurrentCastInfo.get(), autoResumeData.mediaId, autoResumeData.playhead, CastHandler.mLocale, autoResumeData.auth);
            }
            this.mAutoResumeData = Optional.absent();
        }
    }
    
    private void clearAutoreconnectData() {
        Log.v(CastHandler.TAG, "clearAutoreconnectData");
        this.mAutoReconnectRouteInfo = null;
        this.mIsAutoreconnectToSession = false;
    }
    
    private void connectToDevice() {
        this.setState(CastState.CONNECTING);
        Log.d(CastHandler.TAG, "Will connect to device: " + this.mSelectedDevice.getFriendlyName());
        (this.mApiClient = new GoogleApiClient.Builder(this.mContext).addApi(Cast.API, Cast.CastOptions.builder(this.mSelectedDevice, new CastListener()).setVerboseLoggingEnabled(true).build()).addConnectionCallbacks(new DeviceConnectionCallbacks()).addOnConnectionFailedListener(new ApiClientConnectionFailedListener()).build()).connect();
    }
    
    public static CastHandler get() {
        if (CastHandler.mCastHandler == null) {
            Log.e(CastHandler.TAG, "CastHandler not yet initizalized.  Make sure you call init() first!!");
            throw new ExceptionInInitializerError();
        }
        return CastHandler.mCastHandler;
    }
    
    public static CastHandler init(final Context context, final String s, final String deviceType, final CastEventListener eventListener) {
        if (CastHandler.mCastHandler == null) {
            Log.d(CastHandler.TAG, "***********  Initializing CastHandler  ***********");
            CastHandler.mCastHandler = new CastHandler(context, s);
            CastHandler.deviceType = deviceType;
            CastHandler.eventListener = eventListener;
        }
        return CastHandler.mCastHandler;
    }
    
    private boolean isCastRouteAvailable(final MediaRouteSelector mediaRouteSelector) {
        return this.mMediaRouter.isRouteAvailable(mediaRouteSelector, 1);
    }
    
    public static boolean isCastSupported(final Context context) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0;
    }
    
    private void removeFromNotificationCenter() {
        this.mNotificationHelper.removeNotification();
    }
    
    private void setCurrentCastInfo(final Optional<CastInfo> mCurrentCastInfo) {
        if ((!mCurrentCastInfo.isPresent() || !this.isPlayingEpisode(mCurrentCastInfo.get().getMediaId())) && this.getMediaImage() != null) {
            Log.d(CastHandler.TAG, "Deleting media bitmap");
            this.setMediaImage(null);
        }
        if (mCurrentCastInfo.isPresent()) {
            final String getfWideUrl = mCurrentCastInfo.get().getfWideUrl();
            if (getfWideUrl != null) {
                new LoadImageTask(getfWideUrl).execute();
            }
            else {
                Log.e(CastHandler.TAG, "No episode image found");
            }
        }
        final String tag = CastHandler.TAG;
        final StringBuilder append = new StringBuilder().append("Set EpisodeInfo: ");
        String string;
        if (mCurrentCastInfo.isPresent()) {
            string = mCurrentCastInfo.get().getMediaId() + "";
        }
        else {
            string = "None";
        }
        Log.d(tag, append.append(string).toString());
        this.mCurrentCastInfo = mCurrentCastInfo;
    }
    
    private void setState(final CastState mState) {
        if (this.mState != mState) {
            this.mState = mState;
            Log.i(CastHandler.TAG, "New state: " + mState.name());
            switch (mState) {
                case CONNECTED: {
                    this.broadcastEvent("CAST_SESSION_STARTED_EVENT");
                }
                case IDLE: {
                    this.mSelectedDevice = null;
                }
                case CONNECTING: {
                    this.broadcastEvent("CAST_SESSION_STARTING_EVENT");
                }
            }
        }
    }
    
    private boolean shouldUpdateNotification() {
        return this.isShowingMedia() && this.mCurrentCastInfo.isPresent();
    }
    
    private void teardown() {
        Log.d(CastHandler.TAG, "*** Begin Cast Teardown ***");
        Label_0139: {
            if (this.mApiClient == null) {
                break Label_0139;
            }
            Label_0058: {
                if (this.mRemotePlayer == null) {
                    break Label_0058;
                }
            Label_0183_Outer:
                while (true) {
                    Log.v(CastHandler.TAG, "Removing mRemotePlayer");
                    while (true) {
                        while (true) {
                            try {
                                Cast.CastApi.removeMessageReceivedCallbacks(this.mApiClient, this.mRemotePlayer.getNamespace());
                                this.mRemotePlayer = null;
                                if (this.mApiClient.isConnected()) {
                                    Log.v(CastHandler.TAG, "Leave application");
                                    Cast.CastApi.leaveApplication(this.mApiClient).setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(final Status status) {
                                            final String access$100 = CastHandler.TAG;
                                            final StringBuilder append = new StringBuilder().append("Leave application result: ");
                                            String s;
                                            if (status.getStatus().isSuccess()) {
                                                s = "OK";
                                            }
                                            else {
                                                s = "FAIL";
                                            }
                                            Log.d(access$100, append.append(s).toString());
                                        }
                                    });
                                    Log.v(CastHandler.TAG, "Disconnect from device");
                                    this.mApiClient.disconnect();
                                }
                                Log.v(CastHandler.TAG, "Set mApiClient = null");
                                this.mApiClient = null;
                                if (!this.mMediaRouter.getSelectedRoute().equals(this.mMediaRouter.getDefaultRoute())) {
                                    Log.v(CastHandler.TAG, "Selecting default route for mediaRouter");
                                    this.mMediaRouter.selectRoute(this.mMediaRouter.getDefaultRoute());
                                    this.mSelectedDevice = null;
                                    this.mIsConnectionSuspended = false;
                                    this.setCurrentCastInfo(Optional.absent());
                                    this.mAutoResumeData = Optional.absent();
                                    this.mLockScreenHelper.removeFromLockScreen();
                                    Log.d(CastHandler.TAG, "*** End Cast Teardown ***");
                                    this.setState(CastState.IDLE);
                                    if (CastHandler.eventListener != null) {
                                        CastHandler.eventListener.onTeardown();
                                    }
                                    return;
                                }
                            }
                            catch (IllegalStateException ex) {
                                ex.printStackTrace();
                                continue Label_0183_Outer;
                            }
                            catch (IOException ex2) {
                                ex2.printStackTrace();
                                continue Label_0183_Outer;
                            }
                            break;
                        }
                        Log.v(CastHandler.TAG, "Default route for mediaRouter already set");
                        continue;
                    }
                }
            }
        }
    }
    
    public void activate() {
        if (!isCastSupported(this.mContext)) {
            throw new IllegalStateException("Can't activate cast when cast is not supported.");
        }
        Log.d(CastHandler.TAG, "Activate");
        this.mMediaRouter.addCallback(this.mRouteSelector, (MediaRouter.Callback)this.mRouteStateChangeCallback, 1);
    }
    
    public void deactivate() {
        Log.d(CastHandler.TAG, "Deactivate");
        if (this.getState() != CastState.IDLE) {
            this.teardown();
        }
        this.mMediaRouter.removeCallback((MediaRouter.Callback)this.mRouteStateChangeCallback);
    }
    
    public void decreaseVolume() {
        final double max = Math.max(0.0, Cast.CastApi.getVolume(this.mApiClient) - 0.10000000149011612);
        try {
            Cast.CastApi.setVolume(this.mApiClient, max);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        catch (IllegalStateException ex2) {
            ex2.printStackTrace();
        }
        catch (IOException ex3) {
            ex3.printStackTrace();
        }
    }
    
    public void disconnect() {
        if (!this.mMediaRouter.getSelectedRoute().equals(this.mMediaRouter.getDefaultRoute())) {
            this.mMediaRouter.selectRoute(this.mMediaRouter.getDefaultRoute());
        }
    }
    
    public Optional<String> getClickthroughUrl() {
        if (this.mRemotePlayer != null) {
            return this.mRemotePlayer.getClickthroughUrl();
        }
        return Optional.absent();
    }
    
    public Optional<CastInfo> getCurrentCastInfo() {
        return this.mCurrentCastInfo;
    }
    
    public String getDeviceName() {
        return this.mMediaRouter.getSelectedRoute().getName();
    }
    
    public long getDuration() {
        if (this.mRemotePlayer != null) {
            return this.mRemotePlayer.getStreamDuration();
        }
        return 0L;
    }
    
    public Bitmap getMediaImage() {
        return this.mMediaImage;
    }
    
    public long getPlayhead() {
        if (this.mRemotePlayer != null) {
            return this.mRemotePlayer.getApproximateStreamPosition();
        }
        if (this.mLastKnownPlayhead.isPresent()) {
            return this.mLastKnownPlayhead.get();
        }
        return 0L;
    }
    
    public PlayheadListener getPlayheadListener() {
        return this.mPlayheadListener;
    }
    
    public CastState getState() {
        return this.mState;
    }
    
    public Uri getVideoImageUrl() {
        if (this.mCurrentCastInfo.isPresent() && this.mCurrentCastInfo.get().getWideUrl() != null) {
            return Uri.parse(this.mCurrentCastInfo.get().getWideUrl());
        }
        return null;
    }
    
    public String getVideoTitle() {
        if (this.mCurrentCastInfo.isPresent()) {
            return this.mCurrentCastInfo.get().getEpisodeName();
        }
        return null;
    }
    
    public void increaseVolume() {
        final double min = Math.min(1.0, 0.10000000149011612 + Cast.CastApi.getVolume(this.mApiClient));
        try {
            Cast.CastApi.setVolume(this.mApiClient, min);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        catch (IllegalStateException ex2) {
            ex2.printStackTrace();
        }
        catch (IOException ex3) {
            ex3.printStackTrace();
        }
    }
    
    public boolean isAnyRouteAvailable() {
        return this.mIsAnyRouteAvailable;
    }
    
    public boolean isConnectionSuspended() {
        return this.mIsConnectionSuspended;
    }
    
    public boolean isPlaying() {
        final boolean b = false;
        boolean b2;
        if (this.mRequestedPlayState.isPresent()) {
            b2 = (this.mRequestedPlayState.get() == 2);
        }
        else {
            b2 = b;
            if (this.mRemotePlayer != null) {
                final MediaStatus mediaStatus = this.mRemotePlayer.getMediaStatus();
                b2 = b;
                if (mediaStatus != null) {
                    final int playerState = mediaStatus.getPlayerState();
                    if (playerState != 2) {
                        b2 = b;
                        if (playerState != 4) {
                            return b2;
                        }
                    }
                    return true;
                }
            }
        }
        return b2;
    }
    
    public boolean isPlayingAd() {
        return this.mRemotePlayer != null && this.mRemotePlayer.isPlayingAd();
    }
    
    public boolean isPlayingEpisode(final Long n) {
        return this.mRemotePlayer != null && this.mRemotePlayer.getMediaId().equals(Optional.of(n));
    }
    
    public boolean isShowingMedia() {
        boolean b2;
        final boolean b = b2 = false;
        if (this.mRemotePlayer != null) {
            b2 = b;
            if (this.mRemotePlayer.getMediaStatus() != null) {
                final int lastState = this.mRemotePlayer.getLastState();
                if (lastState != 3 && lastState != 2) {
                    b2 = b;
                    if (lastState != 4) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    public void onNewMediaLoaded(final CastInfo castInfo) {
        this.setCurrentCastInfo(Optional.of(castInfo));
        if (this.shouldUpdateNotification()) {
            Log.i(CastHandler.TAG, "Updating the notification");
            this.mNotificationHelper.publishNotification(this);
        }
        this.mLockScreenHelper.setMetadata(castInfo.getLocalizedCastTo(), castInfo.getSeriesName(), castInfo.getEpisodeName(), castInfo.getDuration());
    }
    
    public void pause() {
        if (this.mRemotePlayer == null || this.mIsConnectionSuspended) {
            return;
        }
        try {
            this.mRemotePlayer.pause(this.mApiClient);
            this.mRequestedPlayState = Optional.of(3);
        }
        catch (IllegalStateException ex) {}
    }
    
    public void playMedia(final CastInfo castInfo, int n, final String s) {
        if (this.getState() != CastState.CONNECTING && this.getState() != CastState.CONNECTED) {
            throw new IllegalStateException("Cannot cast outside states CONNECTING and CONNECTED");
        }
        n *= 1000;
        this.setCurrentCastInfo(Optional.of(castInfo));
        final long longValue = castInfo.getMediaId();
        if (this.getState() == CastState.CONNECTED) {
            final Optional<Long> mediaId = this.mRemotePlayer.getMediaId();
            if (mediaId.isPresent() && mediaId.get() == longValue) {
                Log.d(CastHandler.TAG, "Playing requested media, no open request");
                if (this.isPlaying()) {
                    return;
                }
                Log.d(CastHandler.TAG, "Is paused, request play");
                try {
                    this.mRemotePlayer.play(this.mApiClient);
                    this.mRequestedPlayState = Optional.of(2);
                    return;
                }
                catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            Log.d(CastHandler.TAG, "Not playing media, open new stream");
            this.mRemotePlayer.load(this.mApiClient, castInfo, longValue, n, CastHandler.mLocale, s);
            return;
        }
        Log.d(CastHandler.TAG, "state == CONNECTING, saving parameters for later playback");
        this.mAutoResumeData = Optional.of(new AutoResumeData(longValue, n, s));
    }
    
    public void playMedia(final CastInfo castInfo, final String s) {
        this.playMedia(castInfo, castInfo.getPlayhead(), s);
    }
    
    public void registerOnMediaImageChangedListener(final OnMediaChangedListener onMediaChangedListener) {
        if (this.mMediaImageChangedListeners.contains(onMediaChangedListener)) {
            throw new IllegalStateException("Listener is already registered");
        }
        this.mMediaImageChangedListeners.add(onMediaChangedListener);
    }
    
    public void registerOnPlaybackStateChangedListener(final OnPlaybackStateChangedListener onPlaybackStateChangedListener) {
        if (this.mPlaybackStateChangedListeners.contains(onPlaybackStateChangedListener)) {
            throw new IllegalStateException("Listener is already registered");
        }
        this.mPlaybackStateChangedListeners.add(onPlaybackStateChangedListener);
    }
    
    public void resume() {
        if (this.mRemotePlayer == null || this.mIsConnectionSuspended) {
            return;
        }
        try {
            this.mRemotePlayer.play(this.mApiClient);
            this.mRequestedPlayState = Optional.of(2);
        }
        catch (IllegalStateException ex) {}
    }
    
    public void seekTo(final long n) {
        if (this.mRemotePlayer == null) {
            throw new RuntimeException("Unable to seek when message stream is null.");
        }
        if (this.mApiClient != null && this.mApiClient.isConnected()) {
            Log.d(CastHandler.TAG, "Seek to position " + n + " msec");
            this.mRemotePlayer.seek(this.mApiClient, n, 0).setResultCallback(new ResultCallback<RemoteMediaPlayer.MediaChannelResult>() {
                @Override
                public void onResult(final RemoteMediaPlayer.MediaChannelResult mediaChannelResult) {
                    if (!mediaChannelResult.getStatus().isSuccess()) {
                        Log.w(CastHandler.TAG, "Unable to seek");
                    }
                }
            });
        }
    }
    
    public void setLocale(final String mLocale) {
        CastHandler.mLocale = mLocale;
        if (this.mCustomChannel != null) {
            this.mCustomChannel.sendSetLocaleMessage(CastHandler.mLocale);
        }
    }
    
    public void setMediaImage(final Bitmap mMediaImage) {
        if (this.mMediaImage != null && this.mMediaImage.isRecycled()) {
            this.mMediaImage.recycle();
        }
        this.mMediaImage = mMediaImage;
        for (int i = 0; i < this.mMediaImageChangedListeners.size(); ++i) {
            this.mMediaImageChangedListeners.get(i).onMediaChanged();
        }
    }
    
    public void setPlaybackStopListener(final CastEventListener eventListener) {
        CastHandler.eventListener = eventListener;
    }
    
    public void setPlayheadListener(final PlayheadListener mPlayheadListener) {
        this.mPlayheadListener = mPlayheadListener;
    }
    
    public void setRouteSelector(final MediaRouteButton mediaRouteButton, final MediaRouteDialogFactory dialogFactory) {
        mediaRouteButton.setRouteSelector(this.mRouteSelector);
        mediaRouteButton.setDialogFactory(dialogFactory);
    }
    
    public void setRouteSelector(final MenuItem menuItem, final MediaRouteDialogFactory dialogFactory) {
        final MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider)MenuItemCompat.getActionProvider(menuItem);
        mediaRouteActionProvider.setRouteSelector(this.mRouteSelector);
        mediaRouteActionProvider.setDialogFactory(dialogFactory);
    }
    
    public void setThreshold(final float mPlayheadThreshold) {
        this.mPlayheadThreshold = mPlayheadThreshold;
    }
    
    public void stop() {
        if (this.mRemotePlayer == null || this.mIsConnectionSuspended) {
            return;
        }
        try {
            this.mRemotePlayer.stop(this.mApiClient);
            this.mRequestedPlayState = Optional.of(1);
        }
        catch (IllegalStateException ex) {}
    }
    
    public void trackClickthrough() {
        if (this.mCustomChannel != null) {
            this.mCustomChannel.sendTrackClickthroughMessage();
            return;
        }
        Log.w(CastHandler.TAG, "Cannot track clickthrough - mCustomChannel == null");
    }
    
    public void unregisterOnMediaImageChangedListener(final OnMediaChangedListener onMediaChangedListener) {
        if (!this.mMediaImageChangedListeners.contains(onMediaChangedListener)) {
            throw new IllegalStateException("Listener is not registered");
        }
        this.mMediaImageChangedListeners.remove(onMediaChangedListener);
    }
    
    public void unregisterOnPlaybackStateChangedListener(final OnPlaybackStateChangedListener onPlaybackStateChangedListener) {
        if (!this.mPlaybackStateChangedListeners.contains(onPlaybackStateChangedListener)) {
            throw new IllegalStateException("Listener is not registered");
        }
        this.mPlaybackStateChangedListeners.remove(onPlaybackStateChangedListener);
    }
    
    private class ApiClientConnectionFailedListener implements OnConnectionFailedListener
    {
        @Override
        public void onConnectionFailed(final ConnectionResult connectionResult) {
            Log.e(CastHandler.TAG, "ApiClientConnectionFailedListener.onConnectionFailed: " + connectionResult);
            CastHandler.this.teardown();
        }
    }
    
    private class ApplicationConnectionResultCallback implements ResultCallback<Cast.ApplicationConnectionResult>
    {
        private void onConnectFail() {
            Log.d(CastHandler.TAG, "Application connect: FAIL");
            CastHandler.this.teardown();
        }
        
        private void onConnectSuccess(final String s) {
            Log.d(CastHandler.TAG, "Application connect: SUCCESS - sessionId=" + s);
            CastPersistentStore.setSessionId(CastHandler.this.mContext, s);
            CastHandler.this.attachChannels();
            if (CastHandler.this.mCustomChannel != null) {
                CastHandler.this.mCustomChannel.sendConnectMessage();
                CastHandler.this.mCustomChannel.sendSetLocaleMessage(CastHandler.mLocale);
            }
        }
        
        private void onReconnectFail() {
            Log.e(CastHandler.TAG, "Application reconnect: FAIL");
            CastHandler.this.teardown();
            CastHandler.this.clearAutoreconnectData();
            CastPersistentStore.setRouteId(CastHandler.this.mContext, null);
            CastPersistentStore.setSessionId(CastHandler.this.mContext, null);
        }
        
        private void onReconnectSuccess(final String s) {
            Log.d(CastHandler.TAG, "Application reconnect: SUCCESS - sessionId=" + s);
            final boolean b = false;
            final Iterator<MediaRouter.RouteInfo> iterator = CastHandler.this.mMediaRouter.getRoutes().iterator();
            while (true) {
                do {
                    final boolean b2 = b;
                    if (iterator.hasNext()) {
                        continue;
                    }
                    if (!b2) {
                        Log.e(CastHandler.TAG, "Route unavailable after reconnecting to session - cancel reconnecting");
                        this.onReconnectFail();
                    }
                    else {
                        CastHandler.this.attachChannels();
                    }
                    CastHandler.this.clearAutoreconnectData();
                    return;
                } while (!((MediaRouter.RouteInfo)iterator.next()).equals(CastHandler.this.mAutoReconnectRouteInfo));
                CastHandler.this.mMediaRouter.selectRoute(CastHandler.this.mAutoReconnectRouteInfo);
                final boolean b2 = true;
                continue;
            }
        }
        
        @Override
        public void onResult(final Cast.ApplicationConnectionResult applicationConnectionResult) {
            if (applicationConnectionResult.getStatus().isSuccess()) {
                if (CastHandler.this.mIsAutoreconnectToSession) {
                    this.onReconnectSuccess(applicationConnectionResult.getSessionId());
                    return;
                }
                this.onConnectSuccess(applicationConnectionResult.getSessionId());
            }
            else {
                if (CastHandler.this.mIsAutoreconnectToSession) {
                    this.onReconnectFail();
                    return;
                }
                this.onConnectFail();
            }
        }
    }
    
    private class AutoResumeData
    {
        String auth;
        long mediaId;
        int playhead;
        
        public AutoResumeData(final long mediaId, final int playhead, final String auth) {
            this.mediaId = mediaId;
            this.playhead = playhead;
            this.auth = auth;
        }
    }
    
    private class CastListener extends Listener
    {
        @Override
        public void onApplicationDisconnected(final int n) {
            super.onApplicationDisconnected(n);
            Log.d(CastHandler.TAG, "onApplicationDisconnected - will unselect device");
            CastHandler.this.mMediaRouter.selectRoute(CastHandler.this.mMediaRouter.getDefaultRoute());
        }
        
        @Override
        public void onApplicationStatusChanged() {
            super.onApplicationStatusChanged();
            if (CastHandler.this.mApiClient != null && CastHandler.this.mApiClient.isConnected()) {
                Log.d(CastHandler.TAG, "onApplicationStatusChanged: " + Cast.CastApi.getApplicationStatus(CastHandler.this.mApiClient));
                return;
            }
            Log.d(CastHandler.TAG, "onApplicationStatusChanged - not connected.");
        }
        
        @Override
        public void onVolumeChanged() {
            super.onVolumeChanged();
            if (CastHandler.this.mApiClient == null) {
                Log.w(CastHandler.TAG, "onVolumeChanged - mApiClient == null");
                return;
            }
            if (CastHandler.this.mApiClient.isConnected()) {
                Log.i(CastHandler.TAG, "onVolumeChanged - new volume: " + Cast.CastApi.getVolume(CastHandler.this.mApiClient));
                return;
            }
            Log.w(CastHandler.TAG, "onVolumeChanged - Not connected to device");
        }
    }
    
    private class DeviceConnectionCallbacks implements ConnectionCallbacks
    {
        DeviceConnectionCallbacks() {
            Log.i(CastHandler.TAG, "DeviceConnectionCallbacks constuctor");
        }
        
        @Override
        public void onConnected(final Bundle bundle) {
            Log.d(CastHandler.TAG, "Device connected");
            if (CastHandler.this.mApiClient == null) {
                Log.e(CastHandler.TAG, "Connection started but is null");
                return;
            }
            if (CastHandler.this.mIsConnectionSuspended) {
                Log.i(CastHandler.TAG, "Connection unsuspended");
                CastHandler.this.mIsConnectionSuspended = false;
                return;
            }
            Log.i(CastHandler.TAG, "Connected not after suspended");
            final String sessionId = CastPersistentStore.getSessionId(CastHandler.this.mContext);
            if (sessionId != null) {
                Log.d(CastHandler.TAG, "Found persistent sessionId " + sessionId + ", will try join");
                Cast.CastApi.joinApplication(CastHandler.this.mApiClient, CastHandler.mCastApplicationName, sessionId).setResultCallback(new ApplicationConnectionResultCallback());
                return;
            }
            Log.d(CastHandler.TAG, "No persistent sessionId found, will launch application");
            try {
                Cast.CastApi.launchApplication(CastHandler.this.mApiClient, CastHandler.mCastApplicationName).setResultCallback(new ApplicationConnectionResultCallback());
            }
            catch (IllegalStateException ex) {
                Log.e(CastHandler.TAG, "IllegalStateException: Failed to launch application", (Throwable)ex);
                ex.printStackTrace();
                CastHandler.this.teardown();
            }
        }
        
        @Override
        public void onConnectionSuspended(final int n) {
            final String access$100 = CastHandler.TAG;
            final StringBuilder append = new StringBuilder().append("Connection suspended - cause: ");
            String s;
            if (n == 2) {
                s = "Network lost";
            }
            else {
                s = "Service disconnected";
            }
            Log.w(access$100, append.append(s).toString());
            CastHandler.this.mIsConnectionSuspended = true;
        }
    }
    
    private class LoadImageTask extends SafeAsyncTask<Bitmap>
    {
        private String mUrl;
        
        public LoadImageTask(final String mUrl) {
            this.mUrl = mUrl;
        }
        
        @Override
        public Bitmap call() throws Exception {
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(((HttpURLConnection)new URL(this.mUrl).openConnection()).getInputStream());
            final Bitmap decodeStream = BitmapFactory.decodeStream((InputStream)bufferedInputStream);
            bufferedInputStream.close();
            return decodeStream;
        }
        
        public void onException(final Exception ex) {
            Log.e(CastHandler.TAG, "Media image download error", (Throwable)ex);
        }
        
        @Override
        protected void onPreExecute() throws Exception {
            super.onPreExecute();
            Log.v(CastHandler.TAG, "Begin download media image");
        }
        
        public void onSuccess(final Bitmap mediaImage) {
            Log.v(CastHandler.TAG, "Media image downloaded");
            CastHandler.this.setMediaImage(mediaImage);
            CastHandler.this.mLockScreenHelper.setArtworkBitmap(CastHandler.this.getMediaImage());
            CastHandler.this.mLockScreenHelper.setPlaybackState(3);
            if (CastHandler.this.getCurrentCastInfo().isPresent()) {
                CastHandler.this.mNotificationHelper.publishNotification(CastHandler.this);
            }
        }
    }
    
    public interface OnMediaChangedListener
    {
        void onMediaChanged();
    }
    
    public interface OnPlaybackStateChangedListener
    {
        void onPlaybackChanged(final int p0);
        
        void onSuspendedStateChanged(final boolean p0);
    }
    
    public interface PlayheadListener
    {
        void onPlaybackStop();
        
        void onThreshold();
    }
    
    private class RemotePlayerListener implements OnVideoMessageListener
    {
        @Override
        public void onAdStart(final String s) {
            if (s != null) {
                LocalBroadcastManager.getInstance(CastHandler.this.mContext).sendBroadcast(new Intent("CAST_ADS_START_EVENT"));
                return;
            }
            CastHandler.this.broadcastEvent("CAST_ADS_START_EVENT");
        }
        
        @Override
        public void onAdStop() {
            CastHandler.this.broadcastEvent("CAST_ADS_STOP_EVENT");
        }
        
        @Override
        public void onMediaIdChanged(final long n) {
            if (CastHandler.this.mAutoResumeData.isPresent()) {
                Log.d(CastHandler.TAG, "Ignoring new mediaId " + n + "  - pending auto-resume data with media id " + CastHandler.this.mAutoResumeData.get().mediaId + " exists");
            }
            else {
                if (CastHandler.eventListener != null) {
                    CastHandler.eventListener.onMediaChanged(n);
                }
                if (n <= 0L) {
                    CastHandler.this.removeFromNotificationCenter();
                }
            }
        }
        
        @Override
        public void onPlaybackStart() {
            int n = 3;
            final MediaStatus mediaStatus = CastHandler.this.mRemotePlayer.getMediaStatus();
            if (mediaStatus != null) {
                CastHandler.this.broadcastEvent("CAST_VIDEO_STARTED_EVENT");
                if (mediaStatus.getPlayerState() == 3) {
                    n = 2;
                }
                CastHandler.this.mLockScreenHelper.addToLockScreen(n);
                if (CastHandler.this.mCurrentCastInfo.isPresent()) {
                    CastHandler.this.mLockScreenHelper.setMetadata(CastHandler.this.mCurrentCastInfo.get().getLocalizedCastTo(), CastHandler.this.mCurrentCastInfo.get().getSeriesName(), CastHandler.this.mCurrentCastInfo.get().getEpisodeName(), CastHandler.this.mCurrentCastInfo.get().getDuration());
                }
                if (CastHandler.this.getMediaImage() != null) {
                    CastHandler.this.mLockScreenHelper.setArtworkBitmap(CastHandler.this.getMediaImage());
                }
            }
            if (CastHandler.this.shouldUpdateNotification()) {
                CastHandler.this.mNotificationHelper.publishNotification(CastHandler.this);
            }
        }
        
        @Override
        public void onPlaybackStateChanged(final int n) {
            for (int size = CastHandler.this.mPlaybackStateChangedListeners.size(), i = 0; i < size; ++i) {
                ((OnPlaybackStateChangedListener)CastHandler.this.mPlaybackStateChangedListeners.get(i)).onPlaybackChanged(n);
            }
        }
        
        @Override
        public void onPlaybackStatusUpdate() {
            final MediaStatus mediaStatus = CastHandler.this.mRemotePlayer.getMediaStatus();
            if (mediaStatus != null) {
                final int playerState = mediaStatus.getPlayerState();
                if (RemotePlayer.isPlaybackState(playerState) && CastHandler.this.mRequestedPlayState.or(Integer.valueOf(0)) == playerState) {
                    CastHandler.this.mRequestedPlayState = (Optional<Integer>)Optional.absent();
                }
            }
            CastHandler.this.checkAndPlayPendingMedia();
            if (!CastHandler.this.isPlayingAd() && mediaStatus != null && CastHandler.this.getCurrentCastInfo().isPresent() && CastHandler.this.getCurrentCastInfo().get().getDuration() != null && mediaStatus.getStreamPosition() / 1000L > Math.ceil(CastHandler.this.getCurrentCastInfo().get().getDuration() * CastHandler.this.mPlayheadThreshold) && CastHandler.this.mPlayheadListener != null) {
                CastHandler.this.mPlayheadListener.onThreshold();
            }
        }
        
        @Override
        public void onPlaybackStop() {
            Log.d(CastHandler.TAG, "onPlaybackStop");
            CastHandler.this.setCurrentCastInfo(Optional.absent());
            CastHandler.this.broadcastEvent("CAST_VIDEO_COMPLETION_EVENT");
            CastHandler.this.mLockScreenHelper.removeFromLockScreen();
            CastHandler.this.mRequestedPlayState = (Optional<Integer>)Optional.absent();
            if (CastHandler.this.mPlayheadListener != null) {
                CastHandler.this.mPlayheadListener.onPlaybackStop();
            }
            if (CastHandler.eventListener != null) {
                CastHandler.eventListener.onPlaybackStop();
            }
        }
    }
    
    private class RouteStateChangeCallback extends Callback
    {
        private int getFilteredRoutesCount() {
            int n = 0;
            final Iterator<MediaRouter.RouteInfo> iterator = CastHandler.this.mMediaRouter.getRoutes().iterator();
            while (iterator.hasNext()) {
                if (((RouteInfo)iterator.next()).matchesSelector(CastHandler.this.mRouteSelector)) {
                    ++n;
                }
            }
            return n;
        }
        
        @Override
        public void onRouteAdded(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            super.onRouteAdded(mediaRouter, routeInfo);
            Log.d(CastHandler.TAG, "Route added: " + routeInfo.getName() + " + (" + routeInfo.getId() + ")");
            if (this.getFilteredRoutesCount() == 1) {
                CastHandler.this.broadcastEvent("CAST_ROUTES_AVAILABLE_EVENT");
                CastHandler.this.mIsAnyRouteAvailable = true;
            }
            if (CastHandler.this.getState() == CastState.IDLE || CastHandler.this.getState() == CastState.CONNECTING) {
                final String routeId = CastPersistentStore.getRouteId(CastHandler.this.mContext);
                if (routeId != null && routeId.equals(routeInfo.getId())) {
                    Log.v(CastHandler.TAG, "Added route matches persistent - setting as reconnect route");
                    CastHandler.this.mAutoReconnectRouteInfo = routeInfo;
                    if (CastHandler.this.getState() == CastState.IDLE && !CastHandler.this.mIsAutoreconnectToSession) {
                        Log.v(CastHandler.TAG, "No reconnect in progress, will try reconnect to device");
                        CastHandler.this.mIsAutoreconnectToSession = true;
                        CastHandler.this.mSelectedDevice = CastDevice.getFromBundle(routeInfo.getExtras());
                        CastHandler.this.connectToDevice();
                    }
                }
            }
        }
        
        @Override
        public void onRouteRemoved(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            super.onRouteRemoved(mediaRouter, routeInfo);
            Log.d(CastHandler.TAG, "Route removed: " + routeInfo.getName() + " (" + routeInfo.getId() + ")");
            if (this.getFilteredRoutesCount() == 0) {
                CastHandler.this.broadcastEvent("CAST_NO_ROUTES_AVAILABLE_EVENT");
                CastHandler.this.mIsAnyRouteAvailable = false;
            }
        }
        
        @Override
        public void onRouteSelected(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            Log.d(CastHandler.TAG, "Route selected: " + routeInfo.getName());
            if (!routeInfo.isDefault()) {
                CastPersistentStore.setRouteId(CastHandler.this.mContext, routeInfo.getId());
                final CastDevice fromBundle = CastDevice.getFromBundle(routeInfo.getExtras());
                if (fromBundle != null) {
                    CastHandler.this.mSelectedDevice = fromBundle;
                }
                else {
                    CastHandler.this.mSelectedDevice = null;
                    CastHandler.this.mMediaRouter.getSelectedRoute().getName();
                }
                if (CastHandler.this.getState() != CastState.IDLE || CastHandler.this.mIsAutoreconnectToSession) {
                    Log.v(CastHandler.TAG, "State is not IDLE, will not connect again");
                    return;
                }
                CastHandler.this.connectToDevice();
            }
        }
        
        @Override
        public void onRouteUnselected(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
            Log.d(CastHandler.TAG, "Route unselected: " + routeInfo.getName());
            CastPersistentStore.setRouteId(CastHandler.this.mContext, null);
            CastPersistentStore.setSessionId(CastHandler.this.mContext, null);
            CastHandler.this.broadcastSessionEnded();
            CastHandler.this.teardown();
            CastHandler.this.removeFromNotificationCenter();
        }
    }
}
