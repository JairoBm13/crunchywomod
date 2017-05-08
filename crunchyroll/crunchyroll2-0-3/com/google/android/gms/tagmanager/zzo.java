// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import android.os.Message;
import android.os.Handler;
import com.google.android.gms.common.api.Status;

class zzo implements ContainerHolder
{
    private Status zzOt;
    private Container zzaKG;
    private Container zzaKH;
    private zzb zzaKI;
    private zza zzaKJ;
    private TagManager zzaKK;
    private boolean zzaea;
    
    String getContainerId() {
        if (this.zzaea) {
            zzbg.zzaz("getContainerId called on a released ContainerHolder.");
            return "";
        }
        return this.zzaKG.getContainerId();
    }
    
    @Override
    public Status getStatus() {
        return this.zzOt;
    }
    
    public void refresh() {
        synchronized (this) {
            if (this.zzaea) {
                zzbg.zzaz("Refreshing a released ContainerHolder.");
            }
            else {
                this.zzaKJ.zzyq();
            }
        }
    }
    
    @Override
    public void release() {
        synchronized (this) {
            if (this.zzaea) {
                zzbg.zzaz("Releasing a released ContainerHolder.");
            }
            else {
                this.zzaea = true;
                this.zzaKK.zzb(this);
                this.zzaKG.release();
                this.zzaKG = null;
                this.zzaKH = null;
                this.zzaKJ = null;
                this.zzaKI = null;
            }
        }
    }
    
    public void zzeh(final String s) {
        synchronized (this) {
            if (!this.zzaea) {
                this.zzaKG.zzeh(s);
            }
        }
    }
    
    void zzej(final String s) {
        if (this.zzaea) {
            zzbg.zzaz("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
            return;
        }
        this.zzaKJ.zzej(s);
    }
    
    String zzyo() {
        if (this.zzaea) {
            zzbg.zzaz("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
            return "";
        }
        return this.zzaKJ.zzyo();
    }
    
    public interface zza
    {
        void zzej(final String p0);
        
        String zzyo();
        
        void zzyq();
    }
    
    private class zzb extends Handler
    {
        private final ContainerAvailableListener zzaKL;
        final /* synthetic */ zzo zzaKM;
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    zzbg.zzaz("Don't know how to handle this message.");
                }
                case 1: {
                    this.zzel((String)message.obj);
                }
            }
        }
        
        protected void zzel(final String s) {
            this.zzaKL.onContainerAvailable(this.zzaKM, s);
        }
    }
}
