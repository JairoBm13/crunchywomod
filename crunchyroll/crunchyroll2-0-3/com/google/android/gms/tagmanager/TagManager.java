// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import android.net.Uri;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.ComponentCallbacks2;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import android.content.Context;

public class TagManager
{
    private static TagManager zzaOb;
    private final Context mContext;
    private final DataLayer zzaKz;
    private final zzs zzaMV;
    private final zza zzaNY;
    private final zzct zzaNZ;
    private final ConcurrentMap<zzo, Boolean> zzaOa;
    
    TagManager(final Context context, final zza zzaNY, final DataLayer zzaKz, final zzct zzaNZ) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }
        this.mContext = context.getApplicationContext();
        this.zzaNZ = zzaNZ;
        this.zzaNY = zzaNY;
        this.zzaOa = new ConcurrentHashMap<zzo, Boolean>();
        (this.zzaKz = zzaKz).zza((DataLayer.zzb)new DataLayer.zzb() {
            @Override
            public void zzF(final Map<String, Object> map) {
                final Object value = map.get("event");
                if (value != null) {
                    TagManager.this.zzeF(value.toString());
                }
            }
        });
        this.zzaKz.zza((DataLayer.zzb)new zzd(this.mContext));
        this.zzaMV = new zzs();
        this.zzzE();
    }
    
    public static TagManager getInstance(final Context context) {
        Label_0068: {
            synchronized (TagManager.class) {
                if (TagManager.zzaOb != null) {
                    break Label_0068;
                }
                if (context == null) {
                    zzbg.zzaz("TagManager.getInstance requires non-null context.");
                    throw new NullPointerException();
                }
            }
            final Context context2;
            TagManager.zzaOb = new TagManager(context2, (zza)new zza() {}, new DataLayer((DataLayer.zzc)new zzw(context2)), zzcu.zzzz());
        }
        // monitorexit(TagManager.class)
        return TagManager.zzaOb;
    }
    
    private void zzeF(final String s) {
        final Iterator<zzo> iterator = this.zzaOa.keySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().zzeh(s);
        }
    }
    
    private void zzzE() {
        if (Build$VERSION.SDK_INT >= 14) {
            this.mContext.registerComponentCallbacks((ComponentCallbacks)new ComponentCallbacks2() {
                public void onConfigurationChanged(final Configuration configuration) {
                }
                
                public void onLowMemory() {
                }
                
                public void onTrimMemory(final int n) {
                    if (n == 20) {
                        TagManager.this.dispatch();
                    }
                }
            });
        }
    }
    
    public void dispatch() {
        this.zzaNZ.dispatch();
    }
    
    boolean zzb(final zzo zzo) {
        return this.zzaOa.remove(zzo) != null;
    }
    
    boolean zzl(final Uri uri) {
    Label_0064_Outer:
        while (true) {
            while (true) {
                final zzcb zzzf;
                final String containerId;
                Label_0139: {
                    synchronized (this) {
                        zzzf = zzcb.zzzf();
                        if (zzzf.zzl(uri)) {
                            containerId = zzzf.getContainerId();
                            switch (TagManager$4.zzaOd[zzzf.zzzg().ordinal()]) {
                                case 1: {
                                    for (final zzo zzo : this.zzaOa.keySet()) {
                                        if (zzo.getContainerId().equals(containerId)) {
                                            zzo.zzej(null);
                                            zzo.refresh();
                                        }
                                    }
                                    break;
                                }
                                case 2:
                                case 3: {
                                    break Label_0139;
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                }
                for (final zzo zzo2 : this.zzaOa.keySet()) {
                    if (zzo2.getContainerId().equals(containerId)) {
                        zzo2.zzej(zzzf.zzzh());
                        zzo2.refresh();
                    }
                    else {
                        if (zzo2.zzyo() == null) {
                            continue Label_0064_Outer;
                        }
                        zzo2.zzej(null);
                        zzo2.refresh();
                    }
                }
                continue;
            }
            return false;
        }
    }
    
    public interface zza
    {
    }
}
