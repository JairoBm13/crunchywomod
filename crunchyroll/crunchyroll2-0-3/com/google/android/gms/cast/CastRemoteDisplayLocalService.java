// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import android.content.Intent;
import android.os.Binder;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.internal.zzu;
import android.content.BroadcastReceiver;
import com.google.android.gms.R;
import android.os.IBinder;
import android.support.v7.media.MediaRouter;
import android.content.ServiceConnection;
import android.content.Context;
import android.view.Display;
import com.google.android.gms.common.api.GoogleApiClient;
import android.app.Notification;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.android.gms.cast.internal.zzl;
import android.app.Service;

public abstract class CastRemoteDisplayLocalService extends Service
{
    private static final zzl zzQW;
    private static final int zzQX;
    private static final Object zzQY;
    private static AtomicBoolean zzQZ;
    private static CastRemoteDisplayLocalService zzRn;
    private Notification mNotification;
    private String zzQv;
    private GoogleApiClient zzRa;
    private Callbacks zzRc;
    private zzb zzRd;
    private NotificationSettings zzRe;
    private CastDevice zzRh;
    private Display zzRi;
    private Context zzRj;
    private ServiceConnection zzRk;
    private MediaRouter zzRl;
    private final MediaRouter.Callback zzRm;
    private final IBinder zzRo;
    
    static {
        zzQW = new zzl("CastRemoteDisplayLocalService");
        zzQX = R.id.cast_notification_id;
        zzQY = new Object();
        CastRemoteDisplayLocalService.zzQZ = new AtomicBoolean(false);
    }
    
    public CastRemoteDisplayLocalService() {
        this.zzRm = new MediaRouter.Callback() {
            @Override
            public void onRouteUnselected(final MediaRouter mediaRouter, final RouteInfo routeInfo) {
                CastRemoteDisplayLocalService.zzQW.zzb("onRouteUnselected", new Object[0]);
                if (CastRemoteDisplayLocalService.this.zzRh == null) {
                    CastRemoteDisplayLocalService.zzQW.zzb("onRouteUnselected, no device was selected", new Object[0]);
                    return;
                }
                if (!CastDevice.getFromBundle(routeInfo.getExtras()).getDeviceId().equals(CastRemoteDisplayLocalService.this.zzRh.getDeviceId())) {
                    CastRemoteDisplayLocalService.zzQW.zzb("onRouteUnselected, device does not match", new Object[0]);
                    return;
                }
                CastRemoteDisplayLocalService.stopService();
            }
        };
        this.zzRo = (IBinder)new zza();
    }
    
    public static void stopService() {
        zzM(false);
    }
    
    private static void zzM(final boolean b) {
        CastRemoteDisplayLocalService.zzQW.zzb("Stopping Service", new Object[0]);
        CastRemoteDisplayLocalService.zzQZ.set(false);
        synchronized (CastRemoteDisplayLocalService.zzQY) {
            if (CastRemoteDisplayLocalService.zzRn == null) {
                CastRemoteDisplayLocalService.zzQW.zzc("Service is already being stopped", new Object[0]);
                return;
            }
            final CastRemoteDisplayLocalService zzRn = CastRemoteDisplayLocalService.zzRn;
            CastRemoteDisplayLocalService.zzRn = null;
            // monitorexit(CastRemoteDisplayLocalService.zzQY)
            if (!b && zzRn.zzRl != null) {
                CastRemoteDisplayLocalService.zzQW.zzb("Setting default route", new Object[0]);
                zzRn.zzRl.selectRoute(zzRn.zzRl.getDefaultRoute());
            }
            if (zzRn.zzRd != null) {
                CastRemoteDisplayLocalService.zzQW.zzb("Unregistering notification receiver", new Object[0]);
                zzRn.unregisterReceiver((BroadcastReceiver)zzRn.zzRd);
            }
            zzRn.zzlk();
            zzRn.zzll();
            zzRn.zzlg();
            if (zzRn.zzRj != null && zzRn.zzRk != null) {
                zzRn.zzRj.unbindService(zzRn.zzRk);
                zzRn.zzRk = null;
                zzRn.zzRj = null;
            }
            zzRn.zzRc = null;
            zzRn.zzQv = null;
            zzRn.zzRa = null;
            zzRn.zzRh = null;
            zzRn.zzRe = null;
            zzRn.mNotification = null;
            zzRn.zzRi = null;
        }
    }
    
    private void zzlg() {
        if (this.zzRl != null) {
            zzu.zzbY("CastRemoteDisplayLocalService calls must be done on the main thread");
            this.zzRl.removeCallback(this.zzRm);
        }
    }
    
    private void zzli() {
        CastRemoteDisplayLocalService.zzQW.zzb("stopRemoteDisplay", new Object[0]);
        if (this.zzRa == null || !this.zzRa.isConnected()) {
            CastRemoteDisplayLocalService.zzQW.zzc("Unable to stop the remote display as the API client is not ready", new Object[0]);
            return;
        }
        CastRemoteDisplay.CastRemoteDisplayApi.stopRemoteDisplay(this.zzRa).setResultCallback(new ResultCallback<CastRemoteDisplay.CastRemoteDisplaySessionResult>() {
            public void zza(final CastRemoteDisplay.CastRemoteDisplaySessionResult castRemoteDisplaySessionResult) {
                if (!castRemoteDisplaySessionResult.getStatus().isSuccess()) {
                    CastRemoteDisplayLocalService.zzQW.zzb("Unable to stop the remote display, result unsuccessful", new Object[0]);
                }
                else {
                    CastRemoteDisplayLocalService.zzQW.zzb("remote display stopped", new Object[0]);
                }
                CastRemoteDisplayLocalService.this.zzRi = null;
            }
        });
    }
    
    private void zzlk() {
        CastRemoteDisplayLocalService.zzQW.zzb("stopRemoteDisplaySession", new Object[0]);
        this.zzli();
        this.onDismissPresentation();
    }
    
    private void zzll() {
        CastRemoteDisplayLocalService.zzQW.zzb("Stopping the remote display Service", new Object[0]);
        this.stopForeground(true);
        this.stopSelf();
    }
    
    public abstract void onDismissPresentation();
    
    public interface Callbacks
    {
    }
    
    public static final class NotificationSettings
    {
    }
    
    private class zza extends Binder
    {
    }
    
    private static final class zzb extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("com.google.android.gms.cast.remote_display.ACTION_NOTIFICATION_DISCONNECT")) {
                CastRemoteDisplayLocalService.zzQW.zzb("disconnecting", new Object[0]);
                CastRemoteDisplayLocalService.stopService();
            }
        }
    }
}
