// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzi$com.google.android.gms.common.internal.zzi$zza;
import android.content.ComponentName;
import android.os.Message;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import android.os.RemoteException;
import android.os.DeadObjectException;
import java.util.Collection;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.content.ServiceConnection;
import android.util.Log;
import java.util.Iterator;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.gms.common.internal.zzi$com.google.android.gms.common.internal.zzi$zze;
import com.google.android.gms.common.internal.zzi$com.google.android.gms.common.internal.zzi$zzc;
import java.util.ArrayList;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import android.accounts.Account;
import android.os.Handler;
import android.content.Context;
import com.google.android.gms.common.api.Api;
import android.os.IInterface;

public abstract class zzi<T extends IInterface> implements Client, zzj.zza
{
    public static final String[] zzaav;
    private final Context mContext;
    final Handler mHandler;
    private final Account zzMY;
    private final Set<Scope> zzWJ;
    private final Looper zzWt;
    private final com.google.android.gms.common.internal.zze zzXa;
    private final zzk zzaak;
    private zzp zzaal;
    private GoogleApiClient.ConnectionProgressReportCallbacks zzaam;
    private T zzaan;
    private final ArrayList<zzi$zzc<?>> zzaao;
    private zzi$zze zzaap;
    private int zzaaq;
    private GoogleApiClient.ConnectionCallbacks zzaar;
    private GoogleApiClient.OnConnectionFailedListener zzaas;
    private final int zzaat;
    protected AtomicInteger zzaau;
    private final Object zzqt;
    
    static {
        zzaav = new String[] { "service_esmobile", "service_googleme" };
    }
    
    protected zzi(final Context context, final Looper looper, final int zzaat, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zzqt = new Object();
        this.zzaao = new ArrayList<zzi$zzc<?>>();
        this.zzaaq = 1;
        this.zzaau = new AtomicInteger(0);
        this.mContext = zzu.zzu(context);
        this.zzWt = zzu.zzb(looper, "Looper must not be null");
        this.zzaak = zzk.zzah(context);
        this.mHandler = new zzb(looper);
        this.zzaat = zzaat;
        this.zzMY = null;
        this.zzWJ = Collections.emptySet();
        this.zzXa = new GoogleApiClient.Builder(context).zzmx();
        this.zzaar = zzu.zzu(connectionCallbacks);
        this.zzaas = zzu.zzu(onConnectionFailedListener);
    }
    
    protected zzi(final Context context, final Looper looper, final int n, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, final com.google.android.gms.common.internal.zze zze) {
        this(context, looper, zzk.zzah(context), n, zze, connectionCallbacks, onConnectionFailedListener);
    }
    
    protected zzi(final Context context, final Looper looper, final zzk zzk, final int zzaat, final com.google.android.gms.common.internal.zze zze) {
        this.zzqt = new Object();
        this.zzaao = new ArrayList<zzi$zzc<?>>();
        this.zzaaq = 1;
        this.zzaau = new AtomicInteger(0);
        this.mContext = zzu.zzb(context, "Context must not be null");
        this.zzWt = zzu.zzb(looper, "Looper must not be null");
        this.zzaak = zzu.zzb(zzk, "Supervisor must not be null");
        this.mHandler = new zzb(looper);
        this.zzaat = zzaat;
        this.zzXa = zzu.zzu(zze);
        this.zzMY = zze.getAccount();
        this.zzWJ = this.zzb(zze.zznw());
    }
    
    protected zzi(final Context context, final Looper looper, final zzk zzk, final int n, final com.google.android.gms.common.internal.zze zze, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzk, n, zze);
        this.zzaar = zzu.zzu(connectionCallbacks);
        this.zzaas = zzu.zzu(onConnectionFailedListener);
    }
    
    private void zza(final int zzaaq, final T zzaan) {
        boolean b = true;
        int n;
        if (zzaaq == 3) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n2;
        if (zzaan != null) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        if (n != n2) {
            b = false;
        }
        while (true) {
            zzu.zzV(b);
            Label_0100: {
                synchronized (this.zzqt) {
                    this.zzaaq = zzaaq;
                    this.zzaan = zzaan;
                    switch (zzaaq) {
                        case 2: {
                            this.zznH();
                            return;
                        }
                        case 3: {
                            break;
                        }
                        case 1: {
                            break Label_0100;
                        }
                        default: {
                            return;
                        }
                    }
                }
                this.zznG();
                return;
            }
            this.zznI();
        }
    }
    
    private void zza(final GoogleApiClient.ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        this.zzaam = zzu.zzb(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
    }
    
    private boolean zza(final int n, final int n2, final T t) {
        synchronized (this.zzqt) {
            if (this.zzaaq != n) {
                return false;
            }
            this.zza(n2, t);
            return true;
        }
    }
    
    private Set<Scope> zzb(final Set<Scope> set) {
        final Set<Scope> zza = this.zza(set);
        if (zza == null) {
            return zza;
        }
        final Iterator<Scope> iterator = zza.iterator();
        while (iterator.hasNext()) {
            if (!set.contains(iterator.next())) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return zza;
    }
    
    private void zznH() {
        if (this.zzaap != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect() for " + this.getStartServiceAction());
            this.zzaak.zzb(this.getStartServiceAction(), (ServiceConnection)this.zzaap, this.zzkQ());
            this.zzaau.incrementAndGet();
        }
        this.zzaap = new zze(this.zzaau.get());
        if (!this.zzaak.zza(this.getStartServiceAction(), (ServiceConnection)this.zzaap, this.zzkQ())) {
            Log.e("GmsClient", "unable to connect to service: " + this.getStartServiceAction());
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzaau.get(), 9));
        }
    }
    
    private void zznI() {
        if (this.zzaap != null) {
            this.zzaak.zzb(this.getStartServiceAction(), (ServiceConnection)this.zzaap, this.zzkQ());
            this.zzaap = null;
        }
    }
    
    @Override
    public void connect(final GoogleApiClient.ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        this.zza(connectionProgressReportCallbacks);
        this.zza(2, null);
    }
    
    @Override
    public void disconnect() {
        this.zzaau.incrementAndGet();
        synchronized (this.zzaao) {
            for (int size = this.zzaao.size(), i = 0; i < size; ++i) {
                ((zzc)this.zzaao.get(i)).zznR();
            }
            this.zzaao.clear();
            // monitorexit(this.zzaao)
            this.zza(1, null);
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, String[] zzaan) {
    Label_0137:
        while (true) {
            while (true) {
                Label_0127: {
                    Label_0117: {
                        Label_0107: {
                            synchronized (this.zzqt) {
                                final int zzaaq = this.zzaaq;
                                zzaan = (String[])(Object)this.zzaan;
                                // monitorexit(this.zzqt)
                                printWriter.append(s).append("mConnectState=");
                                switch (zzaaq) {
                                    default: {
                                        printWriter.print("UNKNOWN");
                                        printWriter.append(" mService=");
                                        if (zzaan == null) {
                                            printWriter.println("null");
                                            return;
                                        }
                                        break Label_0137;
                                    }
                                    case 2: {
                                        break;
                                    }
                                    case 3: {
                                        break Label_0107;
                                    }
                                    case 4: {
                                        break Label_0117;
                                    }
                                    case 1: {
                                        break Label_0127;
                                    }
                                }
                            }
                            printWriter.print("CONNECTING");
                            continue;
                        }
                        printWriter.print("CONNECTED");
                        continue;
                    }
                    printWriter.print("DISCONNECTING");
                    continue;
                }
                printWriter.print("DISCONNECTED");
                continue;
            }
        }
        printWriter.append(this.getServiceDescriptor()).append("@").println(Integer.toHexString(System.identityHashCode(((IInterface)(Object)zzaan).asBinder())));
    }
    
    public final Context getContext() {
        return this.mContext;
    }
    
    public final Looper getLooper() {
        return this.zzWt;
    }
    
    @Override
    public void getRemoteService(final IAccountAccessor accountAccessor, final Set<Scope> set) {
        try {
            final GetServiceRequest zzf = new GetServiceRequest(this.zzaat).zzcb(this.mContext.getPackageName()).zzf(this.zzkR());
            if (set != null) {
                zzf.zzb(set);
            }
            if (this.requiresSignIn()) {
                zzf.zzb(this.zznt()).zzb(accountAccessor);
            }
            else if (this.requiresAccount()) {
                zzf.zzb(this.zzMY);
            }
            this.zzaal.zza(new zzd(this, this.zzaau.get()), zzf);
        }
        catch (DeadObjectException ex2) {
            Log.w("GmsClient", "service died");
            this.zzbs(1);
        }
        catch (RemoteException ex) {
            Log.w("GmsClient", "Remote exception occurred", (Throwable)ex);
        }
    }
    
    protected abstract String getServiceDescriptor();
    
    protected abstract String getStartServiceAction();
    
    @Override
    public boolean isConnected() {
        while (true) {
            synchronized (this.zzqt) {
                if (this.zzaaq == 3) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean isConnecting() {
        while (true) {
            synchronized (this.zzqt) {
                if (this.zzaaq == 2) {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected void onConnectionFailed(final ConnectionResult connectionResult) {
    }
    
    protected void onConnectionSuspended(final int n) {
    }
    
    public boolean requiresAccount() {
        return false;
    }
    
    @Override
    public boolean requiresSignIn() {
        return false;
    }
    
    @Override
    public void validateAccount(final IAccountAccessor accountAccessor) {
        final ValidateAccountRequest validateAccountRequest = new ValidateAccountRequest(accountAccessor, this.zzWJ.toArray(new Scope[this.zzWJ.size()]), this.mContext.getPackageName(), this.zznN());
        try {
            this.zzaal.zza(new zzd(this, this.zzaau.get()), validateAccountRequest);
        }
        catch (DeadObjectException ex2) {
            Log.w("GmsClient", "service died");
            this.zzbs(1);
        }
        catch (RemoteException ex) {
            Log.w("GmsClient", "Remote exception occurred", (Throwable)ex);
        }
    }
    
    protected abstract T zzT(final IBinder p0);
    
    protected Set<Scope> zza(final Set<Scope> set) {
        return set;
    }
    
    protected void zza(final int n, final Bundle bundle, final int n2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, n2, -1, (Object)new zzi(n, bundle)));
    }
    
    protected void zza(final int n, final IBinder binder, final Bundle bundle, final int n2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, n2, -1, (Object)new zzg(n, binder, bundle)));
    }
    
    public void zzbs(final int n) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, this.zzaau.get(), n));
    }
    
    protected void zzbt(final int n) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, n, -1, (Object)new zzh()));
    }
    
    protected String zzkQ() {
        return this.zzXa.zznz();
    }
    
    protected Bundle zzkR() {
        return new Bundle();
    }
    
    @Override
    public Bundle zzlM() {
        return null;
    }
    
    protected void zznG() {
    }
    
    protected final void zznL() {
        if (!this.isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }
    
    public final T zznM() throws DeadObjectException {
        synchronized (this.zzqt) {
            if (this.zzaaq == 4) {
                throw new DeadObjectException();
            }
        }
        this.zznL();
        zzu.zza(this.zzaan != null, (Object)"Client is connected but service is null");
        final IInterface zzaan = this.zzaan;
        // monitorexit(o)
        return (T)zzaan;
    }
    
    protected Bundle zznN() {
        return null;
    }
    
    public final Account zznt() {
        if (this.zzMY != null) {
            return this.zzMY;
        }
        return new Account("<<default account>>", "com.google");
    }
    
    private abstract class zza extends zzi$zzc<Boolean>
    {
        public final int statusCode;
        public final Bundle zzaaw;
        
        protected zza(final int statusCode, final Bundle zzaaw) {
            super(true);
            this.statusCode = statusCode;
            this.zzaaw = zzaaw;
        }
        
        protected void zzc(final Boolean b) {
            final PendingIntent pendingIntent = null;
            if (b == null) {
                zzi.this.zza(1, null);
            }
            else {
                switch (this.statusCode) {
                    default: {
                        zzi.this.zza(1, null);
                        PendingIntent pendingIntent2 = pendingIntent;
                        if (this.zzaaw != null) {
                            pendingIntent2 = (PendingIntent)this.zzaaw.getParcelable("pendingIntent");
                        }
                        this.zzg(new ConnectionResult(this.statusCode, pendingIntent2));
                    }
                    case 0: {
                        if (!this.zznO()) {
                            zzi.this.zza(1, null);
                            this.zzg(new ConnectionResult(8, null));
                            return;
                        }
                        break;
                    }
                    case 10: {
                        zzi.this.zza(1, null);
                        throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                    }
                }
            }
        }
        
        protected abstract void zzg(final ConnectionResult p0);
        
        protected abstract boolean zznO();
        
        protected void zznP() {
        }
    }
    
    final class zzb extends Handler
    {
        public zzb(final Looper looper) {
            super(looper);
        }
        
        private void zza(final Message message) {
            final zzc zzc = (zzc)message.obj;
            zzc.zznP();
            zzc.unregister();
        }
        
        private boolean zzb(final Message message) {
            return message.what == 2 || message.what == 1 || message.what == 5 || message.what == 6;
        }
        
        public void handleMessage(final Message message) {
            if (zzi.this.zzaau.get() != message.arg1) {
                if (this.zzb(message)) {
                    this.zza(message);
                }
                return;
            }
            if ((message.what == 1 || message.what == 5 || message.what == 6) && !zzi.this.isConnecting()) {
                this.zza(message);
                return;
            }
            if (message.what == 3) {
                final ConnectionResult connectionResult = new ConnectionResult(message.arg2, null);
                zzi.this.zzaam.onReportServiceBinding(connectionResult);
                zzi.this.onConnectionFailed(connectionResult);
                return;
            }
            if (message.what == 4) {
                zzi.this.zza(4, null);
                if (zzi.this.zzaar != null) {
                    zzi.this.zzaar.onConnectionSuspended(message.arg2);
                }
                zzi.this.onConnectionSuspended(message.arg2);
                zzi.this.zza(4, 1, null);
                return;
            }
            if (message.what == 2 && !zzi.this.isConnected()) {
                this.zza(message);
                return;
            }
            if (this.zzb(message)) {
                ((zzc)message.obj).zznQ();
                return;
            }
            Log.wtf("GmsClient", "Don't know how to handle this message.");
        }
    }
    
    protected abstract class zzc<TListener>
    {
        private TListener mListener;
        private boolean zzaay;
        
        public zzc(final TListener mListener) {
            this.mListener = mListener;
            this.zzaay = false;
        }
        
        public void unregister() {
            this.zznR();
            synchronized (zzi.this.zzaao) {
                zzi.this.zzaao.remove(this);
            }
        }
        
        protected abstract void zznP();
        
        public void zznQ() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: monitorenter   
            //     2: aload_0        
            //     3: getfield        com/google/android/gms/common/internal/zzi$zzc.mListener:Ljava/lang/Object;
            //     6: astore_1       
            //     7: aload_0        
            //     8: getfield        com/google/android/gms/common/internal/zzi$zzc.zzaay:Z
            //    11: ifeq            44
            //    14: ldc             "GmsClient"
            //    16: new             Ljava/lang/StringBuilder;
            //    19: dup            
            //    20: invokespecial   java/lang/StringBuilder.<init>:()V
            //    23: ldc             "Callback proxy "
            //    25: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    28: aload_0        
            //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    32: ldc             " being reused. This is not safe."
            //    34: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    37: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    40: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
            //    43: pop            
            //    44: aload_0        
            //    45: monitorexit    
            //    46: aload_1        
            //    47: ifnull          81
            //    50: aload_0        
            //    51: aload_1        
            //    52: invokevirtual   com/google/android/gms/common/internal/zzi$zzc.zzr:(Ljava/lang/Object;)V
            //    55: aload_0        
            //    56: monitorenter   
            //    57: aload_0        
            //    58: iconst_1       
            //    59: putfield        com/google/android/gms/common/internal/zzi$zzc.zzaay:Z
            //    62: aload_0        
            //    63: monitorexit    
            //    64: aload_0        
            //    65: invokevirtual   com/google/android/gms/common/internal/zzi$zzc.unregister:()V
            //    68: return         
            //    69: astore_1       
            //    70: aload_0        
            //    71: monitorexit    
            //    72: aload_1        
            //    73: athrow         
            //    74: astore_1       
            //    75: aload_0        
            //    76: invokevirtual   com/google/android/gms/common/internal/zzi$zzc.zznP:()V
            //    79: aload_1        
            //    80: athrow         
            //    81: aload_0        
            //    82: invokevirtual   com/google/android/gms/common/internal/zzi$zzc.zznP:()V
            //    85: goto            55
            //    88: astore_1       
            //    89: aload_0        
            //    90: monitorexit    
            //    91: aload_1        
            //    92: athrow         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                        
            //  -----  -----  -----  -----  ----------------------------
            //  2      44     69     74     Any
            //  44     46     69     74     Any
            //  50     55     74     81     Ljava/lang/RuntimeException;
            //  57     64     88     93     Any
            //  70     72     69     74     Any
            //  89     91     88     93     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 56, Size: 56
            //     at java.util.ArrayList.rangeCheck(ArrayList.java:653)
            //     at java.util.ArrayList.get(ArrayList.java:429)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public void zznR() {
            synchronized (this) {
                this.mListener = null;
            }
        }
        
        protected abstract void zzr(final TListener p0);
    }
    
    public static final class zzd extends zzo.zza
    {
        private final int zzaaA;
        private zzi zzaaz;
        
        public zzd(final zzi zzaaz, final int zzaaA) {
            this.zzaaz = zzaaz;
            this.zzaaA = zzaaA;
        }
        
        private void zznS() {
            this.zzaaz = null;
        }
        
        public void zza(final int n, final IBinder binder, final Bundle bundle) {
            zzu.zzb(this.zzaaz, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzaaz.zza(n, binder, bundle, this.zzaaA);
            this.zznS();
        }
        
        public void zzb(final int n, final Bundle bundle) {
            zzu.zzb(this.zzaaz, "onAccountValidationComplete can be called only once per call to validateAccount");
            this.zzaaz.zza(n, bundle, this.zzaaA);
            this.zznS();
        }
    }
    
    public final class zze implements ServiceConnection
    {
        private final int zzaaA;
        
        public zze(final int zzaaA) {
            this.zzaaA = zzaaA;
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            zzu.zzb(binder, "Expecting a valid IBinder");
            zzi.this.zzaal = zzp.zza.zzaG(binder);
            zzi.this.zzbt(this.zzaaA);
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            zzi.this.mHandler.sendMessage(zzi.this.mHandler.obtainMessage(4, this.zzaaA, 1));
        }
    }
    
    protected class zzf implements ConnectionProgressReportCallbacks
    {
        @Override
        public void onReportAccountValidation(final ConnectionResult connectionResult) {
            throw new IllegalStateException("Legacy GmsClient received onReportAccountValidation callback.");
        }
        
        @Override
        public void onReportServiceBinding(final ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                zzi.this.getRemoteService(null, zzi.this.zzWJ);
            }
            else if (zzi.this.zzaas != null) {
                zzi.this.zzaas.onConnectionFailed(connectionResult);
            }
        }
    }
    
    protected final class zzg extends zzi$zza
    {
        public final IBinder zzaaB;
        
        public zzg(final int n, final IBinder zzaaB, final Bundle bundle) {
            super(n, bundle);
            this.zzaaB = zzaaB;
        }
        
        protected void zzg(final ConnectionResult connectionResult) {
            if (zzi.this.zzaas != null) {
                zzi.this.zzaas.onConnectionFailed(connectionResult);
            }
            zzi.this.onConnectionFailed(connectionResult);
        }
        
        protected boolean zznO() {
            while (true) {
                try {
                    final String interfaceDescriptor = this.zzaaB.getInterfaceDescriptor();
                    if (!zzi.this.getServiceDescriptor().equals(interfaceDescriptor)) {
                        Log.e("GmsClient", "service descriptor mismatch: " + zzi.this.getServiceDescriptor() + " vs. " + interfaceDescriptor);
                        return false;
                    }
                }
                catch (RemoteException ex) {
                    Log.w("GmsClient", "service probably died");
                    return false;
                }
                final IInterface zzT = zzi.this.zzT(this.zzaaB);
                if (zzT != null && zzi.this.zza(2, 3, zzT)) {
                    break;
                }
                return false;
            }
            final Bundle zzlM = zzi.this.zzlM();
            if (zzi.this.zzaar != null) {
                zzi.this.zzaar.onConnected(zzlM);
            }
            GooglePlayServicesUtil.zzac(zzi.this.mContext);
            return true;
        }
    }
    
    protected final class zzh extends zzi$zza
    {
        public zzh() {
            super(0, null);
        }
        
        protected void zzg(final ConnectionResult connectionResult) {
            zzi.this.zzaam.onReportServiceBinding(connectionResult);
            zzi.this.onConnectionFailed(connectionResult);
        }
        
        protected boolean zznO() {
            zzi.this.zzaam.onReportServiceBinding(ConnectionResult.zzVG);
            return true;
        }
    }
    
    protected final class zzi extends zzi$zza
    {
        public zzi(final int n, final Bundle bundle) {
            super(n, bundle);
        }
        
        protected void zzg(final ConnectionResult connectionResult) {
            com.google.android.gms.common.internal.zzi.this.zzaam.onReportAccountValidation(connectionResult);
            com.google.android.gms.common.internal.zzi.this.onConnectionFailed(connectionResult);
        }
        
        protected boolean zznO() {
            com.google.android.gms.common.internal.zzi.this.zzaam.onReportAccountValidation(ConnectionResult.zzVG);
            return true;
        }
    }
}
