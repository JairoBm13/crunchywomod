// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import android.annotation.TargetApi;
import android.media.RemoteControlClient$MetadataEditor;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.media.AudioManager$OnAudioFocusChangeListener;
import android.util.Log;
import android.media.RemoteControlClient$OnMetadataUpdateListener;
import android.media.RemoteControlClient$OnGetPlaybackPositionListener;
import android.media.RemoteControlClient$OnPlaybackPositionUpdateListener;
import android.os.Build$VERSION;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RemoteControlClient;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;

public class LockScreenHelper extends BroadcastReceiver
{
    @SuppressLint({ "InlinedApi" })
    public static final int PLAYSTATE_PAUSED = 2;
    @SuppressLint({ "InlinedApi" })
    public static final int PLAYSTATE_PLAYING = 3;
    private static final String TAG;
    private static LockScreenHelper sInstance;
    public boolean mAddedToLockScreen;
    private AudioManager mAudioManager;
    private Context mContext;
    ComponentName mEventReceiver;
    private CastHandler.OnPlaybackStateChangedListener mPlaybackStateChangedListener;
    private RemoteControlClient mRemoteControlClient;
    
    static {
        TAG = LockScreenHelper.class.getName();
        LockScreenHelper.sInstance = null;
    }
    
    public LockScreenHelper() {
        this.mAddedToLockScreen = false;
        this.mPlaybackStateChangedListener = new CastHandler.OnPlaybackStateChangedListener() {
            @Override
            public void onPlaybackChanged(final int n) {
                LockScreenHelper.this.updatePlaystate(n);
            }
            
            @Override
            public void onSuspendedStateChanged(final boolean b) {
            }
        };
    }
    
    public LockScreenHelper(final Context mContext) {
        this.mAddedToLockScreen = false;
        this.mPlaybackStateChangedListener = new CastHandler.OnPlaybackStateChangedListener() {
            @Override
            public void onPlaybackChanged(final int n) {
                LockScreenHelper.this.updatePlaystate(n);
            }
            
            @Override
            public void onSuspendedStateChanged(final boolean b) {
            }
        };
        this.mContext = mContext;
        this.mEventReceiver = new ComponentName(this.mContext.getPackageName(), this.getClass().getName());
        (this.mAudioManager = (AudioManager)this.mContext.getSystemService("audio")).registerMediaButtonEventReceiver(this.mEventReceiver);
        final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        intent.setComponent(this.mEventReceiver);
        this.mRemoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(this.mContext.getApplicationContext(), 0, intent, 0));
        if (Build$VERSION.SDK_INT >= 18) {
            this.mRemoteControlClient.setPlaybackPositionUpdateListener((RemoteControlClient$OnPlaybackPositionUpdateListener)new OnPlaybackPositionUpdateListener());
            this.mRemoteControlClient.setOnGetPlaybackPositionListener((RemoteControlClient$OnGetPlaybackPositionListener)new OnGetPlaybackPositionListener());
        }
        if (Build$VERSION.SDK_INT >= 19) {
            this.mRemoteControlClient.setMetadataUpdateListener((RemoteControlClient$OnMetadataUpdateListener)new OnMetadataUpdateListener());
        }
        this.mRemoteControlClient.setTransportControlFlags(this.getRemoteControlFlags());
        this.mAudioManager.registerRemoteControlClient(this.mRemoteControlClient);
    }
    
    public static LockScreenHelper getInstance() {
        return LockScreenHelper.sInstance;
    }
    
    @SuppressLint({ "InlinedApi" })
    private int getRemoteControlFlags() {
        int n = 60;
        if (Build$VERSION.SDK_INT >= 18) {
            n = (0x3C | 0x100);
        }
        return n;
    }
    
    private void updatePlaystate(final int n) {
        int playbackState = 3;
        if (n == 3) {
            playbackState = 2;
        }
        this.mRemoteControlClient.setPlaybackState(playbackState);
    }
    
    public void addToLockScreen(final int n) {
        if (!this.mAddedToLockScreen) {
            Log.i(LockScreenHelper.TAG, "Add to lock screen");
            if (this.mAudioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)null, 3, 3) != 1) {
                Log.w(LockScreenHelper.TAG, "Did not gain audio focus");
                this.mAudioManager = null;
                return;
            }
            this.mAddedToLockScreen = true;
            Log.d(LockScreenHelper.TAG, "Gained audio focus");
            LockScreenHelper.sInstance = this;
            CastHandler.get().registerOnPlaybackStateChangedListener(this.mPlaybackStateChangedListener);
        }
    }
    
    public boolean isAddedToLockScreen() {
        return this.mAddedToLockScreen;
    }
    
    public void onPlayPauseEvent() {
        final CastHandler value = CastHandler.get();
        if (!value.isConnectionSuspended()) {
            if (!value.isPlaying()) {
                value.resume();
                this.mRemoteControlClient.setPlaybackState(3);
                return;
            }
            value.pause();
            this.mRemoteControlClient.setPlaybackState(2);
        }
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction() == "android.intent.action.MEDIA_BUTTON") {
            final KeyEvent keyEvent = (KeyEvent)intent.getExtras().get("android.intent.extra.KEY_EVENT");
            if (keyEvent.getAction() == 1) {
                switch (keyEvent.getKeyCode()) {
                    case 85: {
                        if (getInstance() != null) {
                            getInstance().onPlayPauseEvent();
                            return;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    public void removeFromLockScreen() {
        if (this.mAddedToLockScreen) {
            Log.i(LockScreenHelper.TAG, "Remove from lock screen");
            this.mAudioManager.abandonAudioFocus((AudioManager$OnAudioFocusChangeListener)null);
            LockScreenHelper.sInstance = null;
            CastHandler.get().unregisterOnPlaybackStateChangedListener(this.mPlaybackStateChangedListener);
            this.mAddedToLockScreen = false;
        }
    }
    
    public void setArtworkBitmap(final Bitmap bitmap) {
        Log.d(LockScreenHelper.TAG, "Set bitmap");
        if (bitmap == null || this.mRemoteControlClient == null) {
            Log.d(LockScreenHelper.TAG, "Bitmap is null, can't set.");
            return;
        }
        if (bitmap.isRecycled()) {
            throw new RuntimeException("Bitmap is recycled");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RemoteControlClient$MetadataEditor editMetadata = LockScreenHelper.this.mRemoteControlClient.editMetadata(false);
                editMetadata.putBitmap(100, bitmap.copy(bitmap.getConfig(), false));
                editMetadata.apply();
            }
        }).start();
    }
    
    public void setMetadata(final String s, final String s2, final String s3, final long n) {
        Log.i(LockScreenHelper.TAG, "Set metadata: " + s2 + " - " + s3 + ", duration " + n);
        final RemoteControlClient$MetadataEditor editMetadata = this.mRemoteControlClient.editMetadata(true);
        editMetadata.putString(13, s2);
        editMetadata.putString(1, s);
        editMetadata.putString(2, s2);
        editMetadata.putString(7, s3);
        editMetadata.putLong(9, n);
        editMetadata.apply();
    }
    
    public void setPlaybackState(final int playbackState) {
        Log.d(LockScreenHelper.TAG, "Set playback state: " + playbackState);
        this.mRemoteControlClient.setPlaybackState(playbackState);
    }
    
    @TargetApi(18)
    public class OnGetPlaybackPositionListener implements RemoteControlClient$OnGetPlaybackPositionListener
    {
        public long onGetPlaybackPosition() {
            Log.d(LockScreenHelper.TAG, "onGetPlaybackPosition");
            return CastHandler.get().getPlayhead();
        }
    }
    
    @TargetApi(19)
    public class OnMetadataUpdateListener implements RemoteControlClient$OnMetadataUpdateListener
    {
        public void onMetadataUpdate(final int n, final Object o) {
            Log.d(LockScreenHelper.TAG, "Key: " + n + " new value: " + o);
        }
    }
    
    @TargetApi(18)
    public class OnPlaybackPositionUpdateListener implements RemoteControlClient$OnPlaybackPositionUpdateListener
    {
        public void onPlaybackPositionUpdate(final long n) {
            Log.d(LockScreenHelper.TAG, "onPlaybackPositionUpdate");
            CastHandler.get().seekTo(n);
        }
    }
}
