// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.os.IBinder;
import android.os.IInterface;
import com.google.android.gms.common.internal.zze;
import android.os.Looper;
import android.content.Context;
import java.util.Set;
import com.google.android.gms.common.internal.IAccountAccessor;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import com.google.android.gms.common.internal.zzu;
import java.util.ArrayList;

public final class Api<O extends ApiOptions>
{
    private final String mName;
    private final ClientKey<?> zzVu;
    private final zza<?, O> zzWi;
    private final zzc<?, O> zzWj;
    private final zzd<?> zzWk;
    private final ArrayList<Scope> zzWl;
    
    public Api(final String mName, final zza<C, O> zzWi, final ClientKey<C> zzVu, final Scope... array) {
        zzu.zzb(zzWi, "Cannot construct an Api with a null ClientBuilder");
        zzu.zzb(zzVu, "Cannot construct an Api with a null ClientKey");
        this.mName = mName;
        this.zzWi = zzWi;
        this.zzWj = null;
        this.zzVu = zzVu;
        this.zzWk = null;
        this.zzWl = new ArrayList<Scope>(Arrays.asList(array));
    }
    
    public String getName() {
        return this.mName;
    }
    
    public zza<?, O> zzmp() {
        zzu.zza(this.zzWi != null, (Object)"This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.zzWi;
    }
    
    public zzc<?, O> zzmq() {
        zzu.zza(this.zzWj != null, (Object)"This API was constructed with a ClientBuilder. Use getClientBuilder");
        return this.zzWj;
    }
    
    public List<Scope> zzmr() {
        return this.zzWl;
    }
    
    public ClientKey<?> zzms() {
        zzu.zza(this.zzVu != null, (Object)"This API was constructed with a SimpleClientKey. Use getSimpleClientKey");
        return this.zzVu;
    }
    
    public boolean zzmt() {
        return this.zzWk != null;
    }
    
    public interface ApiOptions
    {
        public interface HasOptions extends ApiOptions
        {
        }
        
        public static final class NoOptions implements NotRequiredOptions
        {
        }
        
        public interface NotRequiredOptions extends ApiOptions
        {
        }
        
        public interface Optional extends HasOptions, NotRequiredOptions
        {
        }
    }
    
    public interface Client
    {
        void connect(final GoogleApiClient.ConnectionProgressReportCallbacks p0);
        
        void disconnect();
        
        void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);
        
        void getRemoteService(final IAccountAccessor p0, final Set<Scope> p1);
        
        boolean isConnected();
        
        boolean requiresSignIn();
        
        void validateAccount(final IAccountAccessor p0);
    }
    
    public static final class ClientKey<C extends Client>
    {
    }
    
    public interface zza<T extends Client, O>
    {
        int getPriority();
        
        T zza(final Context p0, final Looper p1, final zze p2, final O p3, final GoogleApiClient.ConnectionCallbacks p4, final GoogleApiClient.OnConnectionFailedListener p5);
    }
    
    public interface zzb<T extends IInterface>
    {
        String getServiceDescriptor();
        
        String getStartServiceAction();
        
        T zzT(final IBinder p0);
    }
    
    public interface zzc<T extends zzb, O>
    {
        T zzl(final O p0);
        
        int zzmu();
    }
    
    public static final class zzd<C extends zzb>
    {
    }
}
