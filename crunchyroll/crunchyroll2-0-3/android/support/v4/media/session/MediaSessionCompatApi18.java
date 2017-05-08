// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import android.os.SystemClock;
import android.media.RemoteControlClient$OnPlaybackPositionUpdateListener;
import android.media.RemoteControlClient;
import android.media.AudioManager;
import android.app.PendingIntent;
import android.content.Context;

public class MediaSessionCompatApi18
{
    private static final long ACTION_SEEK_TO = 256L;
    
    public static Object createPlaybackPositionUpdateListener(final MediaSessionCompatApi14.Callback callback) {
        return new OnPlaybackPositionUpdateListener(callback);
    }
    
    static int getRccTransportControlFlagsFromActions(final long n) {
        int rccTransportControlFlagsFromActions = MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(n);
        if ((0x100L & n) != 0x0L) {
            rccTransportControlFlagsFromActions |= 0x100;
        }
        return rccTransportControlFlagsFromActions;
    }
    
    public static void registerMediaButtonEventReceiver(final Context context, final PendingIntent pendingIntent) {
        ((AudioManager)context.getSystemService("audio")).registerMediaButtonEventReceiver(pendingIntent);
    }
    
    public static void setOnPlaybackPositionUpdateListener(final Object o, final Object o2) {
        ((RemoteControlClient)o).setPlaybackPositionUpdateListener((RemoteControlClient$OnPlaybackPositionUpdateListener)o2);
    }
    
    public static void setState(final Object o, int rccStateFromState, final long n, final float n2, long n3) {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        long n4 = n;
        if (rccStateFromState == 3) {
            n4 = n;
            if (n > 0L) {
                long n5 = 0L;
                if (n3 > 0L) {
                    n3 = (n5 = elapsedRealtime - n3);
                    if (n2 > 0.0f) {
                        n5 = n3;
                        if (n2 != 1.0f) {
                            n5 = (long)(n3 * n2);
                        }
                    }
                }
                n4 = n + n5;
            }
        }
        rccStateFromState = MediaSessionCompatApi14.getRccStateFromState(rccStateFromState);
        ((RemoteControlClient)o).setPlaybackState(rccStateFromState, n4, n2);
    }
    
    public static void setTransportControlFlags(final Object o, final long n) {
        ((RemoteControlClient)o).setTransportControlFlags(getRccTransportControlFlagsFromActions(n));
    }
    
    public static void unregisterMediaButtonEventReceiver(final Context context, final PendingIntent pendingIntent) {
        ((AudioManager)context.getSystemService("audio")).unregisterMediaButtonEventReceiver(pendingIntent);
    }
    
    static class OnPlaybackPositionUpdateListener<T extends MediaSessionCompatApi14.Callback> implements RemoteControlClient$OnPlaybackPositionUpdateListener
    {
        protected final T mCallback;
        
        public OnPlaybackPositionUpdateListener(final T mCallback) {
            this.mCallback = mCallback;
        }
        
        public void onPlaybackPositionUpdate(final long n) {
            ((MediaSessionCompatApi14.Callback)this.mCallback).onSeekTo(n);
        }
    }
}
