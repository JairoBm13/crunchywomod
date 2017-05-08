// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.media.MediaDescriptionCompat;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import android.os.Message;
import android.view.KeyEvent;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.media.AudioManager;
import android.support.v4.media.RatingCompat;
import android.content.Intent;
import android.os.ResultReceiver;
import java.util.List;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.os.Handler;
import java.util.Iterator;
import android.os.Bundle;
import android.os.Build$VERSION;
import android.text.TextUtils;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import java.util.ArrayList;

public class MediaSessionCompat
{
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    private final ArrayList<OnActiveChangeListener> mActiveListeners;
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;
    
    private MediaSessionCompat(final Context context, final MediaSessionImpl mImpl) {
        this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
        this.mImpl = mImpl;
        this.mController = new MediaControllerCompat(context, this);
    }
    
    public MediaSessionCompat(final Context context, final String s, final ComponentName componentName, final PendingIntent mediaButtonReceiver) {
        this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("tag must not be null or empty");
        }
        if (Build$VERSION.SDK_INT >= 21) {
            (this.mImpl = (MediaSessionImpl)new MediaSessionImplApi21(context, s)).setMediaButtonReceiver(mediaButtonReceiver);
        }
        else {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplBase(context, s, componentName, mediaButtonReceiver);
        }
        this.mController = new MediaControllerCompat(context, this);
    }
    
    public static MediaSessionCompat obtain(final Context context, final Object o) {
        return new MediaSessionCompat(context, (MediaSessionImpl)new MediaSessionImplApi21(o));
    }
    
    public void addOnActiveChangeListener(final OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.add(onActiveChangeListener);
    }
    
    public MediaControllerCompat getController() {
        return this.mController;
    }
    
    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }
    
    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }
    
    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }
    
    public boolean isActive() {
        return this.mImpl.isActive();
    }
    
    public void release() {
        this.mImpl.release();
    }
    
    public void removeOnActiveChangeListener(final OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.remove(onActiveChangeListener);
    }
    
    public void sendSessionEvent(final String s, final Bundle bundle) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("event cannot be null or empty");
        }
        this.mImpl.sendSessionEvent(s, bundle);
    }
    
    public void setActive(final boolean active) {
        this.mImpl.setActive(active);
        final Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActiveChanged();
        }
    }
    
    public void setCallback(final Callback callback) {
        this.setCallback(callback, null);
    }
    
    public void setCallback(final Callback callback, Handler handler) {
        final MediaSessionImpl mImpl = this.mImpl;
        if (handler == null) {
            handler = new Handler();
        }
        mImpl.setCallback(callback, handler);
    }
    
    public void setExtras(final Bundle extras) {
        this.mImpl.setExtras(extras);
    }
    
    public void setFlags(final int flags) {
        this.mImpl.setFlags(flags);
    }
    
    public void setMediaButtonReceiver(final PendingIntent mediaButtonReceiver) {
        this.mImpl.setMediaButtonReceiver(mediaButtonReceiver);
    }
    
    public void setMetadata(final MediaMetadataCompat metadata) {
        this.mImpl.setMetadata(metadata);
    }
    
    public void setPlaybackState(final PlaybackStateCompat playbackState) {
        this.mImpl.setPlaybackState(playbackState);
    }
    
    public void setPlaybackToLocal(final int playbackToLocal) {
        this.mImpl.setPlaybackToLocal(playbackToLocal);
    }
    
    public void setPlaybackToRemote(final VolumeProviderCompat playbackToRemote) {
        if (playbackToRemote == null) {
            throw new IllegalArgumentException("volumeProvider may not be null!");
        }
        this.mImpl.setPlaybackToRemote(playbackToRemote);
    }
    
    public void setQueue(final List<QueueItem> queue) {
        this.mImpl.setQueue(queue);
    }
    
    public void setQueueTitle(final CharSequence queueTitle) {
        this.mImpl.setQueueTitle(queueTitle);
    }
    
    public void setRatingType(final int ratingType) {
        this.mImpl.setRatingType(ratingType);
    }
    
    public void setSessionActivity(final PendingIntent sessionActivity) {
        this.mImpl.setSessionActivity(sessionActivity);
    }
    
    public abstract static class Callback
    {
        final Object mCallbackObj;
        
        public Callback() {
            if (Build$VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaSessionCompatApi21.createCallback((MediaSessionCompatApi21.Callback)new StubApi21());
                return;
            }
            this.mCallbackObj = null;
        }
        
        public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
        }
        
        public void onCustomAction(final String s, final Bundle bundle) {
        }
        
        public void onFastForward() {
        }
        
        public boolean onMediaButtonEvent(final Intent intent) {
            return false;
        }
        
        public void onPause() {
        }
        
        public void onPlay() {
        }
        
        public void onPlayFromMediaId(final String s, final Bundle bundle) {
        }
        
        public void onPlayFromSearch(final String s, final Bundle bundle) {
        }
        
        public void onRewind() {
        }
        
        public void onSeekTo(final long n) {
        }
        
        public void onSetRating(final RatingCompat ratingCompat) {
        }
        
        public void onSkipToNext() {
        }
        
        public void onSkipToPrevious() {
        }
        
        public void onSkipToQueueItem(final long n) {
        }
        
        public void onStop() {
        }
        
        private class StubApi21 implements MediaSessionCompatApi21.Callback
        {
            @Override
            public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
                MediaSessionCompat.Callback.this.onCommand(s, bundle, resultReceiver);
            }
            
            @Override
            public void onCustomAction(final String s, final Bundle bundle) {
                MediaSessionCompat.Callback.this.onCustomAction(s, bundle);
            }
            
            @Override
            public void onFastForward() {
                MediaSessionCompat.Callback.this.onFastForward();
            }
            
            @Override
            public boolean onMediaButtonEvent(final Intent intent) {
                return MediaSessionCompat.Callback.this.onMediaButtonEvent(intent);
            }
            
            @Override
            public void onPause() {
                MediaSessionCompat.Callback.this.onPause();
            }
            
            @Override
            public void onPlay() {
                MediaSessionCompat.Callback.this.onPlay();
            }
            
            @Override
            public void onPlayFromMediaId(final String s, final Bundle bundle) {
                MediaSessionCompat.Callback.this.onPlayFromMediaId(s, bundle);
            }
            
            @Override
            public void onPlayFromSearch(final String s, final Bundle bundle) {
                MediaSessionCompat.Callback.this.onPlayFromSearch(s, bundle);
            }
            
            @Override
            public void onRewind() {
                MediaSessionCompat.Callback.this.onRewind();
            }
            
            @Override
            public void onSeekTo(final long n) {
                MediaSessionCompat.Callback.this.onSeekTo(n);
            }
            
            @Override
            public void onSetRating(final Object o) {
                MediaSessionCompat.Callback.this.onSetRating(RatingCompat.fromRating(o));
            }
            
            @Override
            public void onSkipToNext() {
                MediaSessionCompat.Callback.this.onSkipToNext();
            }
            
            @Override
            public void onSkipToPrevious() {
                MediaSessionCompat.Callback.this.onSkipToPrevious();
            }
            
            @Override
            public void onSkipToQueueItem(final long n) {
                MediaSessionCompat.Callback.this.onSkipToQueueItem(n);
            }
            
            @Override
            public void onStop() {
                MediaSessionCompat.Callback.this.onStop();
            }
        }
    }
    
    interface MediaSessionImpl
    {
        Object getMediaSession();
        
        Object getRemoteControlClient();
        
        Token getSessionToken();
        
        boolean isActive();
        
        void release();
        
        void sendSessionEvent(final String p0, final Bundle p1);
        
        void setActive(final boolean p0);
        
        void setCallback(final Callback p0, final Handler p1);
        
        void setExtras(final Bundle p0);
        
        void setFlags(final int p0);
        
        void setMediaButtonReceiver(final PendingIntent p0);
        
        void setMetadata(final MediaMetadataCompat p0);
        
        void setPlaybackState(final PlaybackStateCompat p0);
        
        void setPlaybackToLocal(final int p0);
        
        void setPlaybackToRemote(final VolumeProviderCompat p0);
        
        void setQueue(final List<QueueItem> p0);
        
        void setQueueTitle(final CharSequence p0);
        
        void setRatingType(final int p0);
        
        void setSessionActivity(final PendingIntent p0);
    }
    
    static class MediaSessionImplApi21 implements MediaSessionImpl
    {
        private PendingIntent mMediaButtonIntent;
        private final Object mSessionObj;
        private final Token mToken;
        
        public MediaSessionImplApi21(final Context context, final String s) {
            this.mSessionObj = MediaSessionCompatApi21.createSession(context, s);
            this.mToken = new Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
        }
        
        public MediaSessionImplApi21(final Object o) {
            this.mSessionObj = MediaSessionCompatApi21.verifySession(o);
            this.mToken = new Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
        }
        
        @Override
        public Object getMediaSession() {
            return this.mSessionObj;
        }
        
        @Override
        public Object getRemoteControlClient() {
            return null;
        }
        
        @Override
        public Token getSessionToken() {
            return this.mToken;
        }
        
        @Override
        public boolean isActive() {
            return MediaSessionCompatApi21.isActive(this.mSessionObj);
        }
        
        @Override
        public void release() {
            MediaSessionCompatApi21.release(this.mSessionObj);
        }
        
        @Override
        public void sendSessionEvent(final String s, final Bundle bundle) {
            MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, s, bundle);
        }
        
        @Override
        public void setActive(final boolean b) {
            MediaSessionCompatApi21.setActive(this.mSessionObj, b);
        }
        
        @Override
        public void setCallback(final Callback callback, final Handler handler) {
            MediaSessionCompatApi21.setCallback(this.mSessionObj, callback.mCallbackObj, handler);
        }
        
        @Override
        public void setExtras(final Bundle bundle) {
            MediaSessionCompatApi21.setExtras(this.mSessionObj, bundle);
        }
        
        @Override
        public void setFlags(final int n) {
            MediaSessionCompatApi21.setFlags(this.mSessionObj, n);
        }
        
        @Override
        public void setMediaButtonReceiver(final PendingIntent mMediaButtonIntent) {
            this.mMediaButtonIntent = mMediaButtonIntent;
            MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, mMediaButtonIntent);
        }
        
        @Override
        public void setMetadata(final MediaMetadataCompat mediaMetadataCompat) {
            MediaSessionCompatApi21.setMetadata(this.mSessionObj, mediaMetadataCompat.getMediaMetadata());
        }
        
        @Override
        public void setPlaybackState(final PlaybackStateCompat playbackStateCompat) {
            MediaSessionCompatApi21.setPlaybackState(this.mSessionObj, playbackStateCompat.getPlaybackState());
        }
        
        @Override
        public void setPlaybackToLocal(final int n) {
            MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, n);
        }
        
        @Override
        public void setPlaybackToRemote(final VolumeProviderCompat volumeProviderCompat) {
            MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, volumeProviderCompat.getVolumeProvider());
        }
        
        @Override
        public void setQueue(final List<QueueItem> list) {
            List<Object> list2 = null;
            if (list != null) {
                final ArrayList<Object> list3 = new ArrayList<Object>();
                final Iterator<QueueItem> iterator = list.iterator();
                while (true) {
                    list2 = list3;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    list3.add(iterator.next().getQueueItem());
                }
            }
            MediaSessionCompatApi21.setQueue(this.mSessionObj, list2);
        }
        
        @Override
        public void setQueueTitle(final CharSequence charSequence) {
            MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, charSequence);
        }
        
        @Override
        public void setRatingType(final int n) {
            if (Build$VERSION.SDK_INT < 22) {
                return;
            }
            MediaSessionCompatApi22.setRatingType(this.mSessionObj, n);
        }
        
        @Override
        public void setSessionActivity(final PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, pendingIntent);
        }
    }
    
    static class MediaSessionImplBase implements MediaSessionImpl
    {
        private final AudioManager mAudioManager;
        private Callback mCallback;
        private final ComponentName mComponentName;
        private final Context mContext;
        private final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks;
        private boolean mDestroyed;
        private Bundle mExtras;
        private int mFlags;
        private final MessageHandler mHandler;
        private boolean mIsActive;
        private boolean mIsMbrRegistered;
        private boolean mIsRccRegistered;
        private int mLocalStream;
        private final Object mLock;
        private final PendingIntent mMediaButtonEventReceiver;
        private MediaMetadataCompat mMetadata;
        private final String mPackageName;
        private List<QueueItem> mQueue;
        private CharSequence mQueueTitle;
        private int mRatingType;
        private final Object mRccObj;
        private PendingIntent mSessionActivity;
        private PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        private final String mTag;
        private final Token mToken;
        private VolumeProviderCompat.Callback mVolumeCallback;
        private VolumeProviderCompat mVolumeProvider;
        private int mVolumeType;
        
        public MediaSessionImplBase(final Context mContext, final String mTag, final ComponentName componentName, final PendingIntent pendingIntent) {
            this.mLock = new Object();
            this.mControllerCallbacks = (RemoteCallbackList<IMediaControllerCallback>)new RemoteCallbackList();
            this.mDestroyed = false;
            this.mIsActive = false;
            this.mIsRccRegistered = false;
            this.mIsMbrRegistered = false;
            this.mVolumeCallback = new VolumeProviderCompat.Callback() {
                @Override
                public void onVolumeChanged(final VolumeProviderCompat volumeProviderCompat) {
                    if (MediaSessionImplBase.this.mVolumeProvider != volumeProviderCompat) {
                        return;
                    }
                    MediaSessionImplBase.this.sendVolumeInfoChanged(new ParcelableVolumeInfo(MediaSessionImplBase.this.mVolumeType, MediaSessionImplBase.this.mLocalStream, volumeProviderCompat.getVolumeControl(), volumeProviderCompat.getMaxVolume(), volumeProviderCompat.getCurrentVolume()));
                }
            };
            if (componentName == null) {
                throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
            }
            PendingIntent broadcast;
            if ((broadcast = pendingIntent) == null) {
                final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                intent.setComponent(componentName);
                broadcast = PendingIntent.getBroadcast(mContext, 0, intent, 0);
            }
            this.mContext = mContext;
            this.mPackageName = mContext.getPackageName();
            this.mAudioManager = (AudioManager)mContext.getSystemService("audio");
            this.mTag = mTag;
            this.mComponentName = componentName;
            this.mMediaButtonEventReceiver = broadcast;
            this.mStub = new MediaSessionStub();
            this.mToken = new Token(this.mStub);
            this.mHandler = new MessageHandler(Looper.myLooper());
            this.mRatingType = 0;
            this.mVolumeType = 1;
            this.mLocalStream = 3;
            if (Build$VERSION.SDK_INT >= 14) {
                this.mRccObj = MediaSessionCompatApi14.createRemoteControlClient(broadcast);
                return;
            }
            this.mRccObj = null;
        }
        
        private void adjustVolume(final int n, final int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onAdjustVolume(n);
                }
                return;
            }
            this.mAudioManager.adjustStreamVolume(n, this.mLocalStream, n2);
        }
        
        private PlaybackStateCompat getStateWithUpdatedPosition() {
            Object o;
            while (true) {
                long lastPositionUpdateTime = -1L;
                o = this.mLock;
                while (true) {
                    Label_0205: {
                        synchronized (o) {
                            final PlaybackStateCompat mState = this.mState;
                            long long1 = lastPositionUpdateTime;
                            if (this.mMetadata != null) {
                                long1 = lastPositionUpdateTime;
                                if (this.mMetadata.containsKey("android.media.metadata.DURATION")) {
                                    long1 = this.mMetadata.getLong("android.media.metadata.DURATION");
                                }
                            }
                            // monitorexit(o)
                            final Object o2 = o = null;
                            Label_0189: {
                                if (mState != null) {
                                    if (mState.getState() != 3 && mState.getState() != 4) {
                                        o = o2;
                                        if (mState.getState() != 5) {
                                            break Label_0189;
                                        }
                                    }
                                    lastPositionUpdateTime = mState.getLastPositionUpdateTime();
                                    final long elapsedRealtime = SystemClock.elapsedRealtime();
                                    o = o2;
                                    if (lastPositionUpdateTime > 0L) {
                                        lastPositionUpdateTime = (long)(mState.getPlaybackSpeed() * (elapsedRealtime - lastPositionUpdateTime)) + mState.getPosition();
                                        if (long1 < 0L || lastPositionUpdateTime <= long1) {
                                            break Label_0205;
                                        }
                                        o = new PlaybackStateCompat.Builder(mState);
                                        ((PlaybackStateCompat.Builder)o).setState(mState.getState(), long1, mState.getPlaybackSpeed(), elapsedRealtime);
                                        o = ((PlaybackStateCompat.Builder)o).build();
                                    }
                                }
                            }
                            if (o == null) {
                                return mState;
                            }
                            break;
                        }
                    }
                    long long1 = lastPositionUpdateTime;
                    if (lastPositionUpdateTime < 0L) {
                        long1 = 0L;
                        continue;
                    }
                    continue;
                }
            }
            return (PlaybackStateCompat)o;
        }
        
        private void sendEvent(final String s, final Bundle bundle) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0036_Outer:
            while (true) {
                Label_0043: {
                    if (n < 0) {
                        break Label_0043;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onEvent(s, bundle);
                            --n;
                            continue Label_0036_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendMetadata(final MediaMetadataCompat mediaMetadataCompat) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0040: {
                    if (n < 0) {
                        break Label_0040;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onMetadataChanged(mediaMetadataCompat);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendQueue(final List<QueueItem> list) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0040: {
                    if (n < 0) {
                        break Label_0040;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onQueueChanged(list);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendQueueTitle(final CharSequence charSequence) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0040: {
                    if (n < 0) {
                        break Label_0040;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onQueueTitleChanged(charSequence);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendSessionDestroyed() {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0032_Outer:
            while (true) {
                Label_0039: {
                    if (n < 0) {
                        break Label_0039;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onSessionDestroyed();
                            --n;
                            continue Label_0032_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                            this.mControllerCallbacks.kill();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendState(final PlaybackStateCompat playbackStateCompat) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0040: {
                    if (n < 0) {
                        break Label_0040;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onPlaybackStateChanged(playbackStateCompat);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void sendVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) {
            int n = this.mControllerCallbacks.beginBroadcast() - 1;
        Label_0033_Outer:
            while (true) {
                Label_0040: {
                    if (n < 0) {
                        break Label_0040;
                    }
                    final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(n);
                    while (true) {
                        try {
                            mediaControllerCallback.onVolumeInfoChanged(parcelableVolumeInfo);
                            --n;
                            continue Label_0033_Outer;
                            this.mControllerCallbacks.finishBroadcast();
                        }
                        catch (RemoteException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void setVolumeTo(final int n, final int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onSetVolumeTo(n);
                }
                return;
            }
            this.mAudioManager.setStreamVolume(this.mLocalStream, n, n2);
        }
        
        private boolean update() {
            final boolean b = false;
            boolean b2;
            if (this.mIsActive) {
                if (Build$VERSION.SDK_INT >= 8) {
                    if (!this.mIsMbrRegistered && (this.mFlags & 0x1) != 0x0) {
                        if (Build$VERSION.SDK_INT >= 18) {
                            MediaSessionCompatApi18.registerMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver);
                        }
                        else {
                            MediaSessionCompatApi8.registerMediaButtonEventReceiver(this.mContext, this.mComponentName);
                        }
                        this.mIsMbrRegistered = true;
                    }
                    else if (this.mIsMbrRegistered && (this.mFlags & 0x1) == 0x0) {
                        if (Build$VERSION.SDK_INT >= 18) {
                            MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver);
                        }
                        else {
                            MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
                        }
                        this.mIsMbrRegistered = false;
                    }
                }
                b2 = b;
                if (Build$VERSION.SDK_INT >= 14) {
                    if (!this.mIsRccRegistered && (this.mFlags & 0x2) != 0x0) {
                        MediaSessionCompatApi14.registerRemoteControlClient(this.mContext, this.mRccObj);
                        this.mIsRccRegistered = true;
                        b2 = true;
                    }
                    else {
                        b2 = b;
                        if (this.mIsRccRegistered) {
                            b2 = b;
                            if ((this.mFlags & 0x2) == 0x0) {
                                MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
                                return this.mIsRccRegistered = false;
                            }
                        }
                    }
                }
            }
            else {
                if (this.mIsMbrRegistered) {
                    if (Build$VERSION.SDK_INT >= 18) {
                        MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver);
                    }
                    else {
                        MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
                    }
                    this.mIsMbrRegistered = false;
                }
                b2 = b;
                if (this.mIsRccRegistered) {
                    MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
                    return this.mIsRccRegistered = false;
                }
            }
            return b2;
        }
        
        @Override
        public Object getMediaSession() {
            return null;
        }
        
        @Override
        public Object getRemoteControlClient() {
            return this.mRccObj;
        }
        
        @Override
        public Token getSessionToken() {
            return this.mToken;
        }
        
        @Override
        public boolean isActive() {
            return this.mIsActive;
        }
        
        @Override
        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            this.update();
            this.sendSessionDestroyed();
        }
        
        @Override
        public void sendSessionEvent(final String s, final Bundle bundle) {
            this.sendEvent(s, bundle);
        }
        
        @Override
        public void setActive(final boolean mIsActive) {
            if (mIsActive != this.mIsActive) {
                this.mIsActive = mIsActive;
                if (this.update()) {
                    this.setMetadata(this.mMetadata);
                    this.setPlaybackState(this.mState);
                }
            }
        }
        
        @Override
        public void setCallback(final Callback mCallback, final Handler handler) {
            if (mCallback == this.mCallback) {
                return;
            }
            if (mCallback == null || Build$VERSION.SDK_INT < 18) {
                if (Build$VERSION.SDK_INT >= 18) {
                    MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, null);
                }
                if (Build$VERSION.SDK_INT >= 19) {
                    MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, null);
                }
            }
            else {
                if (handler == null) {
                    new Handler();
                }
                final MediaSessionCompatApi14.Callback callback = new MediaSessionCompatApi14.Callback() {
                    @Override
                    public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
                        mCallback.onCommand(s, bundle, resultReceiver);
                    }
                    
                    @Override
                    public void onFastForward() {
                        mCallback.onFastForward();
                    }
                    
                    @Override
                    public boolean onMediaButtonEvent(final Intent intent) {
                        return mCallback.onMediaButtonEvent(intent);
                    }
                    
                    @Override
                    public void onPause() {
                        mCallback.onPause();
                    }
                    
                    @Override
                    public void onPlay() {
                        mCallback.onPlay();
                    }
                    
                    @Override
                    public void onRewind() {
                        mCallback.onRewind();
                    }
                    
                    @Override
                    public void onSeekTo(final long n) {
                        mCallback.onSeekTo(n);
                    }
                    
                    @Override
                    public void onSetRating(final Object o) {
                        mCallback.onSetRating(RatingCompat.fromRating(o));
                    }
                    
                    @Override
                    public void onSkipToNext() {
                        mCallback.onSkipToNext();
                    }
                    
                    @Override
                    public void onSkipToPrevious() {
                        mCallback.onSkipToPrevious();
                    }
                    
                    @Override
                    public void onStop() {
                        mCallback.onStop();
                    }
                };
                if (Build$VERSION.SDK_INT >= 18) {
                    MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, MediaSessionCompatApi18.createPlaybackPositionUpdateListener(callback));
                }
                if (Build$VERSION.SDK_INT >= 19) {
                    MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, MediaSessionCompatApi19.createMetadataUpdateListener(callback));
                }
            }
            this.mCallback = mCallback;
        }
        
        @Override
        public void setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
        }
        
        @Override
        public void setFlags(final int mFlags) {
            synchronized (this.mLock) {
                this.mFlags = mFlags;
                // monitorexit(this.mLock)
                this.update();
            }
        }
        
        @Override
        public void setMediaButtonReceiver(final PendingIntent pendingIntent) {
        }
        
        @Override
        public void setMetadata(final MediaMetadataCompat mMetadata) {
            while (true) {
                final Bundle bundle = null;
                final Bundle bundle2 = null;
                synchronized (this.mLock) {
                    this.mMetadata = mMetadata;
                    // monitorexit(this.mLock)
                    this.sendMetadata(mMetadata);
                    if (!this.mIsActive) {
                        return;
                    }
                }
                final MediaMetadataCompat mediaMetadataCompat;
                if (Build$VERSION.SDK_INT >= 19) {
                    final Object mRccObj = this.mRccObj;
                    Bundle bundle3;
                    if (mediaMetadataCompat == null) {
                        bundle3 = bundle2;
                    }
                    else {
                        bundle3 = mediaMetadataCompat.getBundle();
                    }
                    long actions;
                    if (this.mState == null) {
                        actions = 0L;
                    }
                    else {
                        actions = this.mState.getActions();
                    }
                    MediaSessionCompatApi19.setMetadata(mRccObj, bundle3, actions);
                    return;
                }
                if (Build$VERSION.SDK_INT >= 14) {
                    final Object mRccObj2 = this.mRccObj;
                    Bundle bundle4;
                    if (mediaMetadataCompat == null) {
                        bundle4 = bundle;
                    }
                    else {
                        bundle4 = mediaMetadataCompat.getBundle();
                    }
                    MediaSessionCompatApi14.setMetadata(mRccObj2, bundle4);
                }
            }
        }
        
        @Override
        public void setPlaybackState(final PlaybackStateCompat mState) {
            while (true) {
                synchronized (this.mLock) {
                    this.mState = mState;
                    // monitorexit(this.mLock)
                    this.sendState(mState);
                    if (!this.mIsActive) {
                        return;
                    }
                }
                final PlaybackStateCompat playbackStateCompat;
                if (playbackStateCompat == null) {
                    if (Build$VERSION.SDK_INT >= 14) {
                        MediaSessionCompatApi14.setState(this.mRccObj, 0);
                        MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, 0L);
                    }
                }
                else {
                    if (Build$VERSION.SDK_INT >= 18) {
                        MediaSessionCompatApi18.setState(this.mRccObj, playbackStateCompat.getState(), playbackStateCompat.getPosition(), playbackStateCompat.getPlaybackSpeed(), playbackStateCompat.getLastPositionUpdateTime());
                    }
                    else if (Build$VERSION.SDK_INT >= 14) {
                        MediaSessionCompatApi14.setState(this.mRccObj, playbackStateCompat.getState());
                    }
                    if (Build$VERSION.SDK_INT >= 19) {
                        MediaSessionCompatApi19.setTransportControlFlags(this.mRccObj, playbackStateCompat.getActions());
                        return;
                    }
                    if (Build$VERSION.SDK_INT >= 18) {
                        MediaSessionCompatApi18.setTransportControlFlags(this.mRccObj, playbackStateCompat.getActions());
                        return;
                    }
                    if (Build$VERSION.SDK_INT >= 14) {
                        MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, playbackStateCompat.getActions());
                    }
                }
            }
        }
        
        @Override
        public void setPlaybackToLocal(final int n) {
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 1;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
        }
        
        @Override
        public void setPlaybackToRemote(final VolumeProviderCompat mVolumeProvider) {
            if (mVolumeProvider == null) {
                throw new IllegalArgumentException("volumeProvider may not be null");
            }
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 2;
            this.mVolumeProvider = mVolumeProvider;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
            mVolumeProvider.setCallback(this.mVolumeCallback);
        }
        
        @Override
        public void setQueue(final List<QueueItem> mQueue) {
            this.sendQueue(this.mQueue = mQueue);
        }
        
        @Override
        public void setQueueTitle(final CharSequence mQueueTitle) {
            this.sendQueueTitle(this.mQueueTitle = mQueueTitle);
        }
        
        @Override
        public void setRatingType(final int mRatingType) {
            this.mRatingType = mRatingType;
        }
        
        @Override
        public void setSessionActivity(final PendingIntent mSessionActivity) {
            synchronized (this.mLock) {
                this.mSessionActivity = mSessionActivity;
            }
        }
        
        private static final class Command
        {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;
            
            public Command(final String command, final Bundle extras, final ResultReceiver stub) {
                this.command = command;
                this.extras = extras;
                this.stub = stub;
            }
        }
        
        class MediaSessionStub extends Stub
        {
            public void adjustVolume(final int n, final int n2, final String s) {
                MediaSessionImplBase.this.adjustVolume(n, n2);
            }
            
            public void fastForward() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(9);
            }
            
            public Bundle getExtras() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mExtras;
                }
            }
            
            public long getFlags() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mFlags;
                }
            }
            
            public PendingIntent getLaunchPendingIntent() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mSessionActivity;
                }
            }
            
            public MediaMetadataCompat getMetadata() {
                return MediaSessionImplBase.this.mMetadata;
            }
            
            public String getPackageName() {
                return MediaSessionImplBase.this.mPackageName;
            }
            
            public PlaybackStateCompat getPlaybackState() {
                return MediaSessionImplBase.this.getStateWithUpdatedPosition();
            }
            
            public List<QueueItem> getQueue() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mQueue;
                }
            }
            
            public CharSequence getQueueTitle() {
                return MediaSessionImplBase.this.mQueueTitle;
            }
            
            public int getRatingType() {
                return MediaSessionImplBase.this.mRatingType;
            }
            
            public String getTag() {
                return MediaSessionImplBase.this.mTag;
            }
            
            public ParcelableVolumeInfo getVolumeAttributes() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    final int access$300 = MediaSessionImplBase.this.mVolumeType;
                    final int access$301 = MediaSessionImplBase.this.mLocalStream;
                    final VolumeProviderCompat access$302 = MediaSessionImplBase.this.mVolumeProvider;
                    int volumeControl;
                    int n;
                    int n2;
                    if (access$300 == 2) {
                        volumeControl = access$302.getVolumeControl();
                        n = access$302.getMaxVolume();
                        n2 = access$302.getCurrentVolume();
                    }
                    else {
                        volumeControl = 2;
                        n = MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(access$301);
                        n2 = MediaSessionImplBase.this.mAudioManager.getStreamVolume(access$301);
                    }
                    // monitorexit(MediaSessionImplBase.access$1300(this.this$0))
                    return new ParcelableVolumeInfo(access$300, access$301, volumeControl, n, n2);
                }
            }
            
            public boolean isTransportControlEnabled() {
                return (MediaSessionImplBase.this.mFlags & 0x2) != 0x0;
            }
            
            public void next() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(7);
            }
            
            public void pause() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(5);
            }
            
            public void play() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(1);
            }
            
            public void playFromMediaId(final String s, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(2, s, bundle);
            }
            
            public void playFromSearch(final String s, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(3, s, bundle);
            }
            
            public void previous() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(8);
            }
            
            public void rate(final RatingCompat ratingCompat) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(12, ratingCompat);
            }
            
            public void registerCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                Label_0017: {
                    if (!MediaSessionImplBase.this.mDestroyed) {
                        break Label_0017;
                    }
                    try {
                        mediaControllerCallback.onSessionDestroyed();
                        return;
                        MediaSessionImplBase.this.mControllerCallbacks.register((IInterface)mediaControllerCallback);
                    }
                    catch (Exception ex) {}
                }
            }
            
            public void rewind() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(10);
            }
            
            public void seekTo(final long n) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(11, n);
            }
            
            public void sendCommand(final String s, final Bundle bundle, final ResultReceiverWrapper resultReceiverWrapper) {
                MediaSessionImplBase.this.mHandler.post(15, new Command(s, bundle, resultReceiverWrapper.mResultReceiver));
            }
            
            public void sendCustomAction(final String s, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(13, s, bundle);
            }
            
            public boolean sendMediaButton(final KeyEvent keyEvent) {
                final boolean b = (MediaSessionImplBase.this.mFlags & 0x1) != 0x0;
                if (b) {
                    MediaSessionImplBase.this.mHandler.post(14, keyEvent);
                }
                return b;
            }
            
            public void setVolumeTo(final int n, final int n2, final String s) {
                MediaSessionImplBase.this.setVolumeTo(n, n2);
            }
            
            public void skipToQueueItem(final long n) {
                MediaSessionImplBase.this.mHandler.post(4, n);
            }
            
            public void stop() throws RemoteException {
                MediaSessionImplBase.this.mHandler.post(6);
            }
            
            public void unregisterCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                MediaSessionImplBase.this.mControllerCallbacks.unregister((IInterface)mediaControllerCallback);
            }
        }
        
        private class MessageHandler extends Handler
        {
            private static final int MSG_ADJUST_VOLUME = 16;
            private static final int MSG_COMMAND = 15;
            private static final int MSG_CUSTOM_ACTION = 13;
            private static final int MSG_FAST_FORWARD = 9;
            private static final int MSG_MEDIA_BUTTON = 14;
            private static final int MSG_NEXT = 7;
            private static final int MSG_PAUSE = 5;
            private static final int MSG_PLAY = 1;
            private static final int MSG_PLAY_MEDIA_ID = 2;
            private static final int MSG_PLAY_SEARCH = 3;
            private static final int MSG_PREVIOUS = 8;
            private static final int MSG_RATE = 12;
            private static final int MSG_REWIND = 10;
            private static final int MSG_SEEK_TO = 11;
            private static final int MSG_SET_VOLUME = 17;
            private static final int MSG_SKIP_TO_ITEM = 4;
            private static final int MSG_STOP = 6;
            
            public MessageHandler(final Looper looper) {
                super(looper);
            }
            
            public void handleMessage(final Message message) {
                if (MediaSessionImplBase.this.mCallback == null) {
                    return;
                }
                switch (message.what) {
                    default: {}
                    case 1: {
                        MediaSessionImplBase.this.mCallback.onPlay();
                    }
                    case 2: {
                        MediaSessionImplBase.this.mCallback.onPlayFromMediaId((String)message.obj, message.getData());
                    }
                    case 3: {
                        MediaSessionImplBase.this.mCallback.onPlayFromSearch((String)message.obj, message.getData());
                    }
                    case 4: {
                        MediaSessionImplBase.this.mCallback.onSkipToQueueItem((long)message.obj);
                    }
                    case 5: {
                        MediaSessionImplBase.this.mCallback.onPause();
                    }
                    case 6: {
                        MediaSessionImplBase.this.mCallback.onStop();
                    }
                    case 7: {
                        MediaSessionImplBase.this.mCallback.onSkipToNext();
                    }
                    case 8: {
                        MediaSessionImplBase.this.mCallback.onSkipToPrevious();
                    }
                    case 9: {
                        MediaSessionImplBase.this.mCallback.onFastForward();
                    }
                    case 10: {
                        MediaSessionImplBase.this.mCallback.onRewind();
                    }
                    case 11: {
                        MediaSessionImplBase.this.mCallback.onSeekTo((long)message.obj);
                    }
                    case 12: {
                        MediaSessionImplBase.this.mCallback.onSetRating((RatingCompat)message.obj);
                    }
                    case 13: {
                        MediaSessionImplBase.this.mCallback.onCustomAction((String)message.obj, message.getData());
                    }
                    case 14: {
                        MediaSessionImplBase.this.mCallback.onMediaButtonEvent((Intent)message.obj);
                    }
                    case 15: {
                        final Command command = (Command)message.obj;
                        MediaSessionImplBase.this.mCallback.onCommand(command.command, command.extras, command.stub);
                    }
                    case 16: {
                        MediaSessionImplBase.this.adjustVolume((int)message.obj, 0);
                    }
                    case 17: {
                        MediaSessionImplBase.this.setVolumeTo((int)message.obj, 0);
                    }
                }
            }
            
            public void post(final int n) {
                this.post(n, null);
            }
            
            public void post(final int n, final Object o) {
                this.obtainMessage(n, o).sendToTarget();
            }
            
            public void post(final int n, final Object o, final int n2) {
                this.obtainMessage(n, n2, 0, o).sendToTarget();
            }
            
            public void post(final int n, final Object o, final Bundle data) {
                final Message obtainMessage = this.obtainMessage(n, o);
                obtainMessage.setData(data);
                obtainMessage.sendToTarget();
            }
        }
    }
    
    public interface OnActiveChangeListener
    {
        void onActiveChanged();
    }
    
    public static final class QueueItem implements Parcelable
    {
        public static final Parcelable$Creator<QueueItem> CREATOR;
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<QueueItem>() {
                public QueueItem createFromParcel(final Parcel parcel) {
                    return new QueueItem(parcel);
                }
                
                public QueueItem[] newArray(final int n) {
                    return new QueueItem[n];
                }
            };
        }
        
        private QueueItem(final Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }
        
        public QueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final long n) {
            this(null, mediaDescriptionCompat, n);
        }
        
        private QueueItem(final Object mItem, final MediaDescriptionCompat mDescription, final long mId) {
            if (mDescription == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            }
            if (mId == -1L) {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            this.mDescription = mDescription;
            this.mId = mId;
            this.mItem = mItem;
        }
        
        public static QueueItem obtain(final Object o) {
            return new QueueItem(o, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(o)), MediaSessionCompatApi21.QueueItem.getQueueId(o));
        }
        
        public int describeContents() {
            return 0;
        }
        
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }
        
        public long getQueueId() {
            return this.mId;
        }
        
        public Object getQueueItem() {
            if (this.mItem != null || Build$VERSION.SDK_INT < 21) {
                return this.mItem;
            }
            return this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
        }
        
        @Override
        public String toString() {
            return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mDescription.writeToParcel(parcel, n);
            parcel.writeLong(this.mId);
        }
    }
    
    static final class ResultReceiverWrapper implements Parcelable
    {
        public static final Parcelable$Creator<ResultReceiverWrapper> CREATOR;
        private ResultReceiver mResultReceiver;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<ResultReceiverWrapper>() {
                public ResultReceiverWrapper createFromParcel(final Parcel parcel) {
                    return new ResultReceiverWrapper(parcel);
                }
                
                public ResultReceiverWrapper[] newArray(final int n) {
                    return new ResultReceiverWrapper[n];
                }
            };
        }
        
        ResultReceiverWrapper(final Parcel parcel) {
            this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }
        
        public ResultReceiverWrapper(final ResultReceiver mResultReceiver) {
            this.mResultReceiver = mResultReceiver;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mResultReceiver.writeToParcel(parcel, n);
        }
    }
    
    public static final class Token implements Parcelable
    {
        public static final Parcelable$Creator<Token> CREATOR;
        private final Object mInner;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<Token>() {
                public Token createFromParcel(final Parcel parcel) {
                    Object o;
                    if (Build$VERSION.SDK_INT >= 21) {
                        o = parcel.readParcelable((ClassLoader)null);
                    }
                    else {
                        o = parcel.readStrongBinder();
                    }
                    return new Token(o);
                }
                
                public Token[] newArray(final int n) {
                    return new Token[n];
                }
            };
        }
        
        Token(final Object mInner) {
            this.mInner = mInner;
        }
        
        public static Token fromToken(final Object o) {
            if (o == null || Build$VERSION.SDK_INT < 21) {
                return null;
            }
            return new Token(MediaSessionCompatApi21.verifyToken(o));
        }
        
        public int describeContents() {
            return 0;
        }
        
        public Object getToken() {
            return this.mInner;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            if (Build$VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable)this.mInner, n);
                return;
            }
            parcel.writeStrongBinder((IBinder)this.mInner);
        }
    }
}
