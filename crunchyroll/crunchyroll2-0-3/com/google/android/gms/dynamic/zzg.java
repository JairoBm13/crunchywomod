// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.dynamic;

import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzu;
import android.content.Context;

public abstract class zzg<T>
{
    private final String zzaju;
    private T zzajv;
    
    protected zzg(final String zzaju) {
        this.zzaju = zzaju;
    }
    
    protected final T zzak(Context remoteContext) throws zza {
        Label_0058: {
            if (this.zzajv != null) {
                break Label_0058;
            }
            zzu.zzu(remoteContext);
            remoteContext = GooglePlayServicesUtil.getRemoteContext(remoteContext);
            if (remoteContext == null) {
                throw new zza("Could not get remote context.");
            }
            final ClassLoader classLoader = remoteContext.getClassLoader();
            try {
                this.zzajv = this.zzd((IBinder)classLoader.loadClass(this.zzaju).newInstance());
                return this.zzajv;
            }
            catch (ClassNotFoundException ex) {
                throw new zza("Could not load creator class.", ex);
            }
            catch (InstantiationException ex2) {
                throw new zza("Could not instantiate creator.", ex2);
            }
            catch (IllegalAccessException ex3) {
                throw new zza("Could not access creator.", ex3);
            }
        }
    }
    
    protected abstract T zzd(final IBinder p0);
    
    public static class zza extends Exception
    {
        public zza(final String s) {
            super(s);
        }
        
        public zza(final String s, final Throwable t) {
            super(s, t);
        }
    }
}
