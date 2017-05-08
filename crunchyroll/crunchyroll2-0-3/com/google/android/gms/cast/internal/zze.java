// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast.internal;

import java.util.concurrent.atomic.AtomicReference;
import android.os.Handler;
import android.os.Parcelable;
import com.google.android.gms.common.internal.BinderWrapper;
import android.text.TextUtils;
import android.os.IInterface;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import android.os.RemoteException;
import java.util.HashMap;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Looper;
import android.content.Context;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicLong;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza;
import java.util.Map;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.common.internal.zzi;

public final class zze extends zzi<zzi>
{
    private static final zzl zzQW;
    private static final Object zzUD;
    private static final Object zzUE;
    private final Cast.Listener zzQI;
    private double zzSh;
    private boolean zzSi;
    private final Map<Long, com.google.android.gms.common.api.zza.zzb<Status>> zzUA;
    private com.google.android.gms.common.api.zza.zzb<Cast.ApplicationConnectionResult> zzUB;
    private com.google.android.gms.common.api.zza.zzb<Status> zzUC;
    private ApplicationMetadata zzUl;
    private final CastDevice zzUm;
    private final Map<String, Cast.MessageReceivedCallback> zzUn;
    private final long zzUo;
    private zzb zzUp;
    private String zzUq;
    private boolean zzUr;
    private boolean zzUs;
    private boolean zzUt;
    private int zzUu;
    private int zzUv;
    private final AtomicLong zzUw;
    private String zzUx;
    private String zzUy;
    private Bundle zzUz;
    
    static {
        zzQW = new zzl("CastClientImpl");
        zzUD = new Object();
        zzUE = new Object();
    }
    
    public zze(final Context context, final Looper looper, final CastDevice zzUm, final long zzUo, final Cast.Listener zzQI, final GoogleApiClient.ConnectionCallbacks connectionCallbacks, final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 10, connectionCallbacks, onConnectionFailedListener);
        this.zzUm = zzUm;
        this.zzQI = zzQI;
        this.zzUo = zzUo;
        this.zzUn = new HashMap<String, Cast.MessageReceivedCallback>();
        this.zzUw = new AtomicLong(0L);
        this.zzUA = new HashMap<Long, com.google.android.gms.common.api.zza.zzb<Status>>();
        this.zzlL();
    }
    
    private void zza(final ApplicationStatus applicationStatus) {
        final String zzlI = applicationStatus.zzlI();
        boolean b;
        if (!com.google.android.gms.cast.internal.zzf.zza(zzlI, this.zzUq)) {
            this.zzUq = zzlI;
            b = true;
        }
        else {
            b = false;
        }
        zze.zzQW.zzb("hasChanged=%b, mFirstApplicationStatusUpdate=%b", b, this.zzUr);
        if (this.zzQI != null && (b || this.zzUr)) {
            this.zzQI.onApplicationStatusChanged();
        }
        this.zzUr = false;
    }
    
    private void zza(final DeviceStatus deviceStatus) {
        final ApplicationMetadata applicationMetadata = deviceStatus.getApplicationMetadata();
        if (!com.google.android.gms.cast.internal.zzf.zza(applicationMetadata, this.zzUl)) {
            this.zzUl = applicationMetadata;
            this.zzQI.onApplicationMetadataChanged(this.zzUl);
        }
        final double zzlO = deviceStatus.zzlO();
        boolean b;
        if (zzlO != Double.NaN && Math.abs(zzlO - this.zzSh) > 1.0E-7) {
            this.zzSh = zzlO;
            b = true;
        }
        else {
            b = false;
        }
        final boolean zzlX = deviceStatus.zzlX();
        if (zzlX != this.zzSi) {
            this.zzSi = zzlX;
            b = true;
        }
        zze.zzQW.zzb("hasVolumeChanged=%b, mFirstDeviceStatusUpdate=%b", b, this.zzUs);
        if (this.zzQI != null && (b || this.zzUs)) {
            this.zzQI.onVolumeChanged();
        }
        final int zzlP = deviceStatus.zzlP();
        boolean b2;
        if (zzlP != this.zzUu) {
            this.zzUu = zzlP;
            b2 = true;
        }
        else {
            b2 = false;
        }
        zze.zzQW.zzb("hasActiveInputChanged=%b, mFirstDeviceStatusUpdate=%b", b2, this.zzUs);
        if (this.zzQI != null && (b2 || this.zzUs)) {
            this.zzQI.onActiveInputStateChanged(this.zzUu);
        }
        final int zzlQ = deviceStatus.zzlQ();
        boolean b3;
        if (zzlQ != this.zzUv) {
            this.zzUv = zzlQ;
            b3 = true;
        }
        else {
            b3 = false;
        }
        zze.zzQW.zzb("hasStandbyStateChanged=%b, mFirstDeviceStatusUpdate=%b", b3, this.zzUs);
        if (this.zzQI != null && (b3 || this.zzUs)) {
            this.zzQI.onStandbyStateChanged(this.zzUv);
        }
        this.zzUs = false;
    }
    
    private void zzc(final com.google.android.gms.common.api.zza.zzb<Cast.ApplicationConnectionResult> zzUB) {
        synchronized (zze.zzUD) {
            if (this.zzUB != null) {
                this.zzUB.zzm(new zza(new Status(2002)));
            }
            this.zzUB = zzUB;
        }
    }
    
    private void zze(final com.google.android.gms.common.api.zza.zzb<Status> zzUC) {
        synchronized (zze.zzUE) {
            if (this.zzUC != null) {
                zzUC.zzm(new Status(2001));
                return;
            }
            this.zzUC = zzUC;
        }
    }
    
    private void zzlL() {
        this.zzUt = false;
        this.zzUu = -1;
        this.zzUv = -1;
        this.zzUl = null;
        this.zzUq = null;
        this.zzSh = 0.0;
        this.zzSi = false;
    }
    
    private void zzlR() {
        zze.zzQW.zzb("removing all MessageReceivedCallbacks", new Object[0]);
        synchronized (this.zzUn) {
            this.zzUn.clear();
        }
    }
    
    private void zzlS() throws IllegalStateException {
        if (!this.zzUt || this.zzUp == null || this.zzUp.isDisposed()) {
            throw new IllegalStateException("Not connected to a device");
        }
    }
    
    @Override
    public void disconnect() {
        zze.zzQW.zzb("disconnect(); ServiceListener=%s, isConnected=%b", this.zzUp, this.isConnected());
        final zzb zzUp = this.zzUp;
        this.zzUp = null;
        if (zzUp == null || zzUp.zzlW() == null) {
            zze.zzQW.zzb("already disposed, so short-circuiting", new Object[0]);
            return;
        }
        this.zzlR();
        try {
            if (this.isConnected() || this.isConnecting()) {
                this.zznM().disconnect();
            }
        }
        catch (RemoteException ex) {
            zze.zzQW.zzb((Throwable)ex, "Error while disconnecting the controller interface: %s", ex.getMessage());
        }
        finally {
            super.disconnect();
        }
    }
    
    public String getApplicationStatus() throws IllegalStateException {
        this.zzlS();
        return this.zzUq;
    }
    
    @Override
    protected String getServiceDescriptor() {
        return "com.google.android.gms.cast.internal.ICastDeviceController";
    }
    
    @Override
    protected String getStartServiceAction() {
        return "com.google.android.gms.cast.service.BIND_CAST_DEVICE_CONTROLLER_SERVICE";
    }
    
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        this.zzlR();
    }
    
    @Override
    protected void zza(final int n, final IBinder binder, final Bundle bundle, final int n2) {
        zze.zzQW.zzb("in onPostInitHandler; statusCode=%d", n);
        if (n == 0 || n == 1001) {
            this.zzUt = true;
            this.zzUr = true;
            this.zzUs = true;
        }
        else {
            this.zzUt = false;
        }
        int n3 = n;
        if (n == 1001) {
            (this.zzUz = new Bundle()).putBoolean("com.google.android.gms.cast.EXTRA_APP_NO_LONGER_RUNNING", true);
            n3 = 0;
        }
        super.zza(n3, binder, bundle, n2);
    }
    
    public void zza(final String s, final Cast.MessageReceivedCallback messageReceivedCallback) throws IllegalArgumentException, IllegalStateException, RemoteException {
        com.google.android.gms.cast.internal.zzf.zzbD(s);
        this.zzbC(s);
        if (messageReceivedCallback == null) {
            return;
        }
        synchronized (this.zzUn) {
            this.zzUn.put(s, messageReceivedCallback);
            // monitorexit(this.zzUn)
            this.zznM().zzbH(s);
        }
    }
    
    public void zza(final String s, final String s2, final com.google.android.gms.common.api.zza.zzb<Status> zzb) throws IllegalArgumentException, IllegalStateException, RemoteException {
        if (TextUtils.isEmpty((CharSequence)s2)) {
            throw new IllegalArgumentException("The message payload cannot be null or empty");
        }
        if (s2.length() > 65536) {
            throw new IllegalArgumentException("Message exceeds maximum size");
        }
        com.google.android.gms.cast.internal.zzf.zzbD(s);
        this.zzlS();
        final long incrementAndGet = this.zzUw.incrementAndGet();
        try {
            this.zzUA.put(incrementAndGet, zzb);
            this.zznM().zza(s, s2, incrementAndGet);
        }
        catch (Throwable t) {
            this.zzUA.remove(incrementAndGet);
            throw t;
        }
    }
    
    public void zza(final String s, final boolean b, final com.google.android.gms.common.api.zza.zzb<Cast.ApplicationConnectionResult> zzb) throws IllegalStateException, RemoteException {
        this.zzc(zzb);
        this.zznM().zzf(s, b);
    }
    
    protected zzi zzaw(final IBinder binder) {
        return com.google.android.gms.cast.internal.zzi.zza.zzax(binder);
    }
    
    public void zzb(final String s, final String s2, final com.google.android.gms.common.api.zza.zzb<Cast.ApplicationConnectionResult> zzb) throws IllegalStateException, RemoteException {
        this.zzc(zzb);
        this.zznM().zzr(s, s2);
    }
    
    public void zzbC(final String p0) throws IllegalArgumentException, RemoteException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //     4: ifeq            18
        //     7: new             Ljava/lang/IllegalArgumentException;
        //    10: dup            
        //    11: ldc_w           "Channel namespace cannot be null or empty"
        //    14: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    17: athrow         
        //    18: aload_0        
        //    19: getfield        com/google/android/gms/cast/internal/zze.zzUn:Ljava/util/Map;
        //    22: astore_2       
        //    23: aload_2        
        //    24: monitorenter   
        //    25: aload_0        
        //    26: getfield        com/google/android/gms/cast/internal/zze.zzUn:Ljava/util/Map;
        //    29: aload_1        
        //    30: invokeinterface java/util/Map.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    35: checkcast       Lcom/google/android/gms/cast/Cast$MessageReceivedCallback;
        //    38: astore_3       
        //    39: aload_2        
        //    40: monitorexit    
        //    41: aload_3        
        //    42: ifnull          58
        //    45: aload_0        
        //    46: invokevirtual   com/google/android/gms/cast/internal/zze.zznM:()Landroid/os/IInterface;
        //    49: checkcast       Lcom/google/android/gms/cast/internal/zzi;
        //    52: aload_1        
        //    53: invokeinterface com/google/android/gms/cast/internal/zzi.zzbI:(Ljava/lang/String;)V
        //    58: return         
        //    59: astore_1       
        //    60: aload_2        
        //    61: monitorexit    
        //    62: aload_1        
        //    63: athrow         
        //    64: astore_2       
        //    65: getstatic       com/google/android/gms/cast/internal/zze.zzQW:Lcom/google/android/gms/cast/internal/zzl;
        //    68: aload_2        
        //    69: ldc_w           "Error unregistering namespace (%s): %s"
        //    72: iconst_2       
        //    73: anewarray       Ljava/lang/Object;
        //    76: dup            
        //    77: iconst_0       
        //    78: aload_1        
        //    79: aastore        
        //    80: dup            
        //    81: iconst_1       
        //    82: aload_2        
        //    83: invokevirtual   java/lang/IllegalStateException.getMessage:()Ljava/lang/String;
        //    86: aastore        
        //    87: invokevirtual   com/google/android/gms/cast/internal/zzl.zzb:(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
        //    90: return         
        //    Exceptions:
        //  throws java.lang.IllegalArgumentException
        //  throws android.os.RemoteException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  25     41     59     64     Any
        //  45     58     64     91     Ljava/lang/IllegalStateException;
        //  60     62     59     64     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0058:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
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
    
    public void zzd(final double n) throws IllegalArgumentException, IllegalStateException, RemoteException {
        if (Double.isInfinite(n) || Double.isNaN(n)) {
            throw new IllegalArgumentException("Volume cannot be " + n);
        }
        this.zznM().zza(n, this.zzSh, this.zzSi);
    }
    
    public void zzd(final com.google.android.gms.common.api.zza.zzb<Status> zzb) throws IllegalStateException, RemoteException {
        this.zze(zzb);
        this.zznM().zzlY();
    }
    
    @Override
    protected Bundle zzkR() {
        final Bundle bundle = new Bundle();
        zze.zzQW.zzb("getRemoteService(): mLastApplicationId=%s, mLastSessionId=%s", this.zzUx, this.zzUy);
        this.zzUm.putInBundle(bundle);
        bundle.putLong("com.google.android.gms.cast.EXTRA_CAST_FLAGS", this.zzUo);
        this.zzUp = new zzb(this);
        bundle.putParcelable("listener", (Parcelable)new BinderWrapper(((com.google.android.gms.cast.internal.zzj.zza)this.zzUp).asBinder()));
        if (this.zzUx != null) {
            bundle.putString("last_application_id", this.zzUx);
            if (this.zzUy != null) {
                bundle.putString("last_session_id", this.zzUy);
            }
        }
        return bundle;
    }
    
    @Override
    public Bundle zzlM() {
        if (this.zzUz != null) {
            final Bundle zzUz = this.zzUz;
            this.zzUz = null;
            return zzUz;
        }
        return super.zzlM();
    }
    
    public double zzlO() throws IllegalStateException {
        this.zzlS();
        return this.zzSh;
    }
    
    private static final class zza implements ApplicationConnectionResult
    {
        private final String zzFE;
        private final Status zzOt;
        private final ApplicationMetadata zzUF;
        private final String zzUG;
        private final boolean zzUH;
        
        public zza(final Status status) {
            this(status, null, null, null, false);
        }
        
        public zza(final Status zzOt, final ApplicationMetadata zzUF, final String zzUG, final String zzFE, final boolean zzUH) {
            this.zzOt = zzOt;
            this.zzUF = zzUF;
            this.zzUG = zzUG;
            this.zzFE = zzFE;
            this.zzUH = zzUH;
        }
        
        @Override
        public String getSessionId() {
            return this.zzFE;
        }
        
        @Override
        public Status getStatus() {
            return this.zzOt;
        }
    }
    
    private static class zzb extends zzj.zza
    {
        private final Handler mHandler;
        private final AtomicReference<zze> zzUI;
        
        public zzb(final zze zze) {
            this.zzUI = new AtomicReference<zze>(zze);
            this.mHandler = new Handler(zze.getLooper());
        }
        
        private void zza(final zze zze, final long n, final int n2) {
            synchronized (zze.zzUA) {
                final com.google.android.gms.common.api.zza.zzb<Status> zzb = zze.zzUA.remove(n);
                // monitorexit(zze.zzg(zze))
                if (zzb != null) {
                    zzb.zzm(new Status(n2));
                }
            }
        }
        
        private boolean zza(final zze zze, final int n) {
            synchronized (zze.zzUE) {
                if (zze.zzUC != null) {
                    zze.zzUC.zzm(new Status(n));
                    zze.zzUC = null;
                    return true;
                }
                return false;
            }
        }
        
        public boolean isDisposed() {
            return this.zzUI.get() == null;
        }
        
        public void onApplicationDisconnected(final int n) {
            final zze zze = this.zzUI.get();
            if (zze != null) {
                zze.zzUx = null;
                zze.zzUy = null;
                this.zza(zze, n);
                if (zze.zzQI != null) {
                    this.mHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (zze.zzQI != null) {
                                zze.zzQI.onApplicationDisconnected(n);
                            }
                        }
                    });
                }
            }
        }
        
        public void zza(final ApplicationMetadata applicationMetadata, final String s, final String s2, final boolean b) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            zze.zzUl = applicationMetadata;
            zze.zzUx = applicationMetadata.getApplicationId();
            zze.zzUy = s2;
            synchronized (com.google.android.gms.cast.internal.zze.zzUD) {
                if (zze.zzUB != null) {
                    zze.zzUB.zzm(new zze.zza(new Status(0), applicationMetadata, s, s2, b));
                    zze.zzUB = null;
                }
            }
        }
        
        public void zza(final String s, final double n, final boolean b) {
            zze.zzQW.zzb("Deprecated callback: \"onStatusreceived\"", new Object[0]);
        }
        
        public void zza(final String s, final long n, final int n2) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            this.zza(zze, n, n2);
        }
        
        public void zzaM(final int n) {
            final zze zzlW = this.zzlW();
            if (zzlW != null) {
                zze.zzQW.zzb("ICastDeviceControllerListener.onDisconnected: %d", n);
                if (n != 0) {
                    zzlW.zzbs(2);
                }
            }
        }
        
        public void zzaN(final int n) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            synchronized (com.google.android.gms.cast.internal.zze.zzUD) {
                if (zze.zzUB != null) {
                    zze.zzUB.zzm(new zze.zza(new Status(n)));
                    zze.zzUB = null;
                }
            }
        }
        
        public void zzaO(final int n) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            this.zza(zze, n);
        }
        
        public void zzaP(final int n) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            this.zza(zze, n);
        }
        
        public void zzb(final ApplicationStatus applicationStatus) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            com.google.android.gms.cast.internal.zze.zzQW.zzb("onApplicationStatusChanged", new Object[0]);
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    zze.zza(applicationStatus);
                }
            });
        }
        
        public void zzb(final DeviceStatus deviceStatus) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            com.google.android.gms.cast.internal.zze.zzQW.zzb("onDeviceStatusChanged", new Object[0]);
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    zze.zza(deviceStatus);
                }
            });
        }
        
        public void zzb(final String s, final byte[] array) {
            if (this.zzUI.get() == null) {
                return;
            }
            zze.zzQW.zzb("IGNORING: Receive (type=binary, ns=%s) <%d bytes>", s, array.length);
        }
        
        public void zzd(final String s, final long n) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            this.zza(zze, n, 0);
        }
        
        public zze zzlW() {
            final zze zze = this.zzUI.getAndSet(null);
            if (zze == null) {
                return null;
            }
            zze.zzlL();
            return zze;
        }
        
        public void zzq(final String s, final String s2) {
            final zze zze = this.zzUI.get();
            if (zze == null) {
                return;
            }
            com.google.android.gms.cast.internal.zze.zzQW.zzb("Receive (type=text, ns=%s) %s", s, s2);
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    synchronized (zze.zzUn) {
                        final Cast.MessageReceivedCallback messageReceivedCallback = zze.zzUn.get(s);
                        // monitorexit(zze.zze(this.zzUJ))
                        if (messageReceivedCallback != null) {
                            messageReceivedCallback.onMessageReceived(zze.zzUm, s, s2);
                            return;
                        }
                    }
                    zze.zzQW.zzb("Discarded message for unknown namespace '%s'", s);
                }
            });
        }
    }
}
