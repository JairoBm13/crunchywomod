// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.os.IBinder;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.support.v4.app.TaskStackBuilder;
import java.io.Serializable;
import com.crunchyroll.cast.model.CastInfo;
import android.app.PendingIntent;
import android.util.Log;
import android.content.Context;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.support.v4.app.NotificationCompat;
import android.app.Service;

public class NotificationService extends Service
{
    public static final String CastNotificationClose = "CastNotificationClose";
    public static final String CastNotificationPause = "CastNotificationPause";
    public static final String CastNotificationPlay = "CastNotificationPlay";
    public static final int NOTIFICATION_ID = 1;
    private static final int PENDING_INTENT_FLAGS = 134217728;
    private final String TAG;
    private NotificationCompat.Builder mBuilder;
    private CastHandler mCastHandler;
    private BroadcastReceiver mCastReceiver;
    private Intent mIntent;
    private NotificationManager mManager;
    private BroadcastReceiver mNotificationReceiver;
    CastHandler.OnPlaybackStateChangedListener mPlaybackStateListener;
    
    public NotificationService() {
        this.TAG = NotificationService.class.getName();
        this.mPlaybackStateListener = new CastHandler.OnPlaybackStateChangedListener() {
            @Override
            public void onPlaybackChanged(final int n) {
                NotificationService.this.updateNotification();
            }
            
            @Override
            public void onSuspendedStateChanged(final boolean b) {
            }
        };
        this.mCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("CAST_VIDEO_COMPLETION_EVENT") || action.equals("CAST_SESSION_ENDED_EVENT") || action.equals("CAST_SESSION_LOST_EVENT")) {
                    NotificationService.this.stopSelf();
                }
            }
        };
        this.mNotificationReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (action.equals("CastNotificationPause")) {
                    if (!NotificationService.this.mCastHandler.isConnectionSuspended()) {
                        NotificationService.this.mCastHandler.pause();
                        NotificationService.this.updateNotification();
                    }
                }
                else if (action.equals("CastNotificationPlay")) {
                    if (!NotificationService.this.mCastHandler.isConnectionSuspended()) {
                        NotificationService.this.mCastHandler.resume();
                        NotificationService.this.updateNotification();
                    }
                }
                else if (action.equals("CastNotificationClose")) {
                    NotificationService.this.mCastHandler.disconnect();
                }
            }
        };
        Log.d(this.TAG, "Constructor");
    }
    
    private PendingIntent getCloseIntent() {
        return PendingIntent.getBroadcast((Context)this, 0, new Intent("CastNotificationClose"), 0);
    }
    
    private PendingIntent getPauseIntent() {
        return PendingIntent.getBroadcast((Context)this, 0, new Intent("CastNotificationPause"), 0);
    }
    
    private PendingIntent getPlayIntent() {
        return PendingIntent.getBroadcast((Context)this, 0, new Intent("CastNotificationPlay"), 0);
    }
    
    private void updateNotification() {
        boolean b = false;
        Log.d(this.TAG, "updateNotification");
        final CastInfo castInfo = (CastInfo)this.mIntent.getSerializableExtra("castInfo");
        if (castInfo == null) {
            throw new NullPointerException("Cannot publish notification with no castInfo available");
        }
        final Intent intent = new Intent((Context)this, castInfo.getVideoPlayerClass());
        intent.setAction("com.crunchyroll.intent.action.CAST");
        intent.setFlags(67108864);
        intent.putExtra("castInfo", (Serializable)castInfo);
        Log.d(this.TAG, "Main activity not running, starting activity stack:: " + castInfo.getVideoPlayerClass());
        this.mBuilder.setContentIntent(TaskStackBuilder.create((Context)this).addParentStack(castInfo.getVideoPlayerClass()).addNextIntent(intent).getPendingIntent(0, 134217728));
        final RemoteViews content = new RemoteViews(this.getPackageName(), R.layout.notification_cast);
        content.setTextViewText(R.id.series_name, (CharSequence)castInfo.getSeriesName());
        content.setTextViewText(R.id.episode_name, (CharSequence)castInfo.getEpisodeName());
        content.setTextViewText(R.id.streaming_to_text, (CharSequence)String.format(castInfo.getLocalizedCastTo(), this.mCastHandler.getDeviceName()));
        final Bitmap mediaImage = this.mCastHandler.getMediaImage();
        if (mediaImage != null) {
            if (mediaImage.isRecycled()) {
                throw new RuntimeException("Bitmap has been recycled.");
            }
            content.setImageViewBitmap(R.id.image, mediaImage.copy(mediaImage.getConfig(), false));
            content.setViewVisibility(R.id.image, 0);
        }
        else {
            content.setViewVisibility(R.id.image, 4);
        }
        final int action_pause = R.id.action_pause;
        if (!this.mCastHandler.isConnectionSuspended()) {
            b = true;
        }
        content.setBoolean(action_pause, "setEnabled", b);
        if (this.mCastHandler.isPlaying()) {
            content.setOnClickPendingIntent(R.id.action_pause, this.getPauseIntent());
            content.setImageViewResource(R.id.action_pause, R.drawable.ic_action_pause);
        }
        else {
            content.setOnClickPendingIntent(R.id.action_pause, this.getPlayIntent());
            content.setImageViewResource(R.id.action_pause, R.drawable.ic_action_play);
        }
        content.setOnClickPendingIntent(R.id.action_close, this.getCloseIntent());
        this.mBuilder.setContent(content);
        this.mManager.notify(1, this.mBuilder.build());
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCreate() {
        super.onCreate();
        (this.mBuilder = new NotificationCompat.Builder((Context)this)).setSmallIcon(R.drawable.ic_notification_media_route);
        this.mBuilder.setWhen(0L);
        this.mBuilder.setOngoing(true);
        this.mManager = (NotificationManager)this.getSystemService("notification");
        this.mCastHandler = CastHandler.get();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CastNotificationPlay");
        intentFilter.addAction("CastNotificationPause");
        intentFilter.addAction("CastNotificationClose");
        this.registerReceiver(this.mNotificationReceiver, intentFilter);
        final IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("CAST_VIDEO_COMPLETION_EVENT");
        intentFilter2.addAction("CAST_SESSION_LOST_EVENT");
        intentFilter2.addAction("CAST_SESSION_ENDED_EVENT");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mCastReceiver, intentFilter2);
        this.mCastHandler.registerOnPlaybackStateChangedListener(this.mPlaybackStateListener);
    }
    
    public void onDestroy() {
        Log.v(this.TAG, "onDestroy");
        this.removeFromNotificationCenter();
        this.unregisterReceiver(this.mNotificationReceiver);
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mCastReceiver);
        this.mCastHandler.unregisterOnPlaybackStateChangedListener(this.mPlaybackStateListener);
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent mIntent, final int n, final int n2) {
        if (mIntent != null) {
            Log.d(this.TAG, "onStartCommand - has intent");
            if (mIntent.getAction() == null) {
                Log.v(this.TAG, "Intent has no action, will update notification");
                this.mIntent = mIntent;
                this.updateNotification();
            }
        }
        else {
            Log.v(this.TAG, "onStartCommand - empty intent, will stop.");
        }
        return 2;
    }
    
    public void removeFromNotificationCenter() {
        this.mManager.cancel(1);
    }
    
    private class CastHelper implements Serializable
    {
        private Bundle mBundle;
        private Class mCastClass;
        private String mEpisodeName;
        private String mLocalizedCastingToString;
        private String mSeriesName;
        
        public CastHelper(final Class mCastClass, final Bundle mBundle, final String mEpisodeName, final String mSeriesName, final String mLocalizedCastingToString) {
            this.mCastClass = null;
            this.mBundle = null;
            this.mEpisodeName = null;
            this.mSeriesName = null;
            this.mLocalizedCastingToString = null;
            this.mCastClass = mCastClass;
            this.mBundle = mBundle;
            this.mEpisodeName = mEpisodeName;
            this.mSeriesName = mSeriesName;
            this.mLocalizedCastingToString = mLocalizedCastingToString;
        }
        
        public Bundle getBundle() {
            return this.mBundle;
        }
        
        public Class getCastClass() {
            return this.mCastClass;
        }
        
        public String getEpisodeName() {
            return this.mEpisodeName;
        }
        
        public String getLocalizedCastingToString() {
            return this.mLocalizedCastingToString;
        }
        
        public String getSeriesName() {
            return this.mSeriesName;
        }
        
        public void setBundle(final Bundle mBundle) {
            this.mBundle = mBundle;
        }
        
        public void setCastClass(final Class mCastClass) {
            this.mCastClass = mCastClass;
        }
        
        public void setEpisodeName(final String mEpisodeName) {
            this.mEpisodeName = mEpisodeName;
        }
        
        public void setLocalizedCastingToString(final String mLocalizedCastingToString) {
            this.mLocalizedCastingToString = mLocalizedCastingToString;
        }
        
        public void setSeriesName(final String mSeriesName) {
            this.mSeriesName = mSeriesName;
        }
    }
}
