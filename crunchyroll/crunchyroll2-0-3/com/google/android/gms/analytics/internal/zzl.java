// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import android.util.Pair;
import java.util.HashMap;
import android.text.TextUtils;
import com.google.android.gms.internal.zzns;
import android.content.Context;
import com.google.android.gms.analytics.CampaignTrackingService;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.analytics.AnalyticsService;
import com.google.android.gms.analytics.AnalyticsReceiver;
import android.database.sqlite.SQLiteException;
import java.util.Iterator;
import com.google.android.gms.internal.zzno;
import java.util.Map;
import com.google.android.gms.internal.zznx;
import com.google.android.gms.internal.zzio;
import com.google.android.gms.internal.zznq;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.analytics.zza;
import com.google.android.gms.internal.zzny;
import com.google.android.gms.common.internal.zzu;

class zzl extends zzd
{
    private boolean mStarted;
    private final zzj zzKn;
    private final zzah zzKo;
    private final zzag zzKp;
    private final zzi zzKq;
    private long zzKr;
    private final zzt zzKs;
    private final zzt zzKt;
    private final zzaj zzKu;
    private long zzKv;
    private boolean zzKw;
    
    protected zzl(final zzf zzf, final zzg zzg) {
        super(zzf);
        zzu.zzu(zzg);
        this.zzKr = Long.MIN_VALUE;
        this.zzKp = zzg.zzk(zzf);
        this.zzKn = zzg.zzm(zzf);
        this.zzKo = zzg.zzn(zzf);
        this.zzKq = zzg.zzo(zzf);
        this.zzKu = new zzaj(this.zzhP());
        this.zzKs = new zzt(zzf) {
            @Override
            public void run() {
                zzl.this.zziA();
            }
        };
        this.zzKt = new zzt(zzf) {
            @Override
            public void run() {
                zzl.this.zziB();
            }
        };
    }
    
    private void zza(final zzh zzh, final zzny zzny) {
        zzu.zzu(zzh);
        zzu.zzu(zzny);
        final zza zza = new zza(this.zzhM());
        zza.zzaI(zzh.zzij());
        zza.enableAdvertisingIdCollection(zzh.zzik());
        final zzno zzhc = zza.zzhc();
        final zzip zzip = zzhc.zze(zzip.class);
        zzip.zzaN("data");
        zzip.zzF(true);
        zzhc.zzb(zzny);
        final zzio zzio = zzhc.zze(zzio.class);
        final zznx zznx = zzhc.zze(zznx.class);
        for (final Map.Entry<String, String> entry : zzh.zzn().entrySet()) {
            final String s = entry.getKey();
            final String userId = entry.getValue();
            if ("an".equals(s)) {
                zznx.setAppName(userId);
            }
            else if ("av".equals(s)) {
                zznx.setAppVersion(userId);
            }
            else if ("aid".equals(s)) {
                zznx.setAppId(userId);
            }
            else if ("aiid".equals(s)) {
                zznx.setAppInstallerId(userId);
            }
            else if ("uid".equals(s)) {
                zzip.setUserId(userId);
            }
            else {
                zzio.set(s, userId);
            }
        }
        this.zzb("Sending installation campaign to", zzh.zzij(), zzny);
        zzhc.zzL(this.zzhU().zzkk());
        zzhc.zzvT();
    }
    
    private boolean zzba(final String s) {
        return this.getContext().checkCallingOrSelfPermission(s) == 0;
    }
    
    private void zziA() {
        this.zzb(new zzw() {
            @Override
            public void zzc(final Throwable t) {
                zzl.this.zziG();
            }
        });
    }
    
    private void zziB() {
        while (true) {
            try {
                this.zzKn.zzis();
                this.zziG();
                this.zzKt.zzt(this.zzhR().zzjy());
            }
            catch (SQLiteException ex) {
                this.zzd("Failed to delete stale hits", ex);
                continue;
            }
            break;
        }
    }
    
    private boolean zziH() {
        return !this.zzKw && (!this.zzhR().zziW() || this.zzhR().zziX()) && this.zziN() > 0L;
    }
    
    private void zziI() {
        final zzv zzhT = this.zzhT();
        if (zzhT.zzjG() && !zzhT.zzbp()) {
            final long zzit = this.zzit();
            if (zzit != 0L && Math.abs(this.zzhP().currentTimeMillis() - zzit) <= this.zzhR().zzjg()) {
                this.zza("Dispatch alarm scheduled (ms)", this.zzhR().zzjf());
                zzhT.zzjH();
            }
        }
    }
    
    private void zziJ() {
        this.zziI();
        final long zziN = this.zziN();
        final long zzkm = this.zzhU().zzkm();
        long n;
        if (zzkm != 0L) {
            n = zziN - Math.abs(this.zzhP().currentTimeMillis() - zzkm);
            if (n <= 0L) {
                n = Math.min(this.zzhR().zzjd(), zziN);
            }
        }
        else {
            n = Math.min(this.zzhR().zzjd(), zziN);
        }
        this.zza("Dispatch scheduled (ms)", n);
        if (this.zzKs.zzbp()) {
            this.zzKs.zzu(Math.max(1L, n + this.zzKs.zzjD()));
            return;
        }
        this.zzKs.zzt(n);
    }
    
    private void zziK() {
        this.zziL();
        this.zziM();
    }
    
    private void zziL() {
        if (this.zzKs.zzbp()) {
            this.zzaT("All hits dispatched or no network/service. Going to power save mode");
        }
        this.zzKs.cancel();
    }
    
    private void zziM() {
        final zzv zzhT = this.zzhT();
        if (zzhT.zzbp()) {
            zzhT.cancel();
        }
    }
    
    private void zziy() {
        final Context context = this.zzhM().getContext();
        if (!AnalyticsReceiver.zzT(context)) {
            this.zzaW("AnalyticsReceiver is not registered or is disabled. Register the receiver for reliable dispatching on non-Google Play devices. See http://goo.gl/8Rd3yj for instructions.");
        }
        else if (!AnalyticsService.zzU(context)) {
            this.zzaX("AnalyticsService is not registered or is disabled. Analytics service at risk of not starting. See http://goo.gl/8Rd3yj for instructions.");
        }
        if (!CampaignTrackingReceiver.zzT(context)) {
            this.zzaW("CampaignTrackingReceiver is not registered, not exported or is disabled. Installation campaign tracking is not possible. See http://goo.gl/8Rd3yj for instructions.");
        }
        else if (!CampaignTrackingService.zzU(context)) {
            this.zzaW("CampaignTrackingService is not registered or is disabled. Installation campaign tracking is not possible. See http://goo.gl/8Rd3yj for instructions.");
        }
    }
    
    protected void onServiceConnected() {
        this.zzhO();
        if (!this.zzhR().zziW()) {
            this.zziD();
        }
    }
    
    void start() {
        this.zzia();
        zzu.zza(!this.mStarted, (Object)"Analytics backend already started");
        this.mStarted = true;
        if (!this.zzhR().zziW()) {
            this.zziy();
        }
        this.zzhS().zze(new Runnable() {
            @Override
            public void run() {
                zzl.this.zziz();
            }
        });
    }
    
    public void zzG(final boolean b) {
        this.zziG();
    }
    
    public long zza(final zzh p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokestatic    com/google/android/gms/common/internal/zzu.zzu:(Ljava/lang/Object;)Ljava/lang/Object;
        //     4: pop            
        //     5: aload_0        
        //     6: invokevirtual   com/google/android/gms/analytics/internal/zzl.zzia:()V
        //     9: aload_0        
        //    10: invokevirtual   com/google/android/gms/analytics/internal/zzl.zzhO:()V
        //    13: aload_0        
        //    14: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //    17: invokevirtual   com/google/android/gms/analytics/internal/zzj.beginTransaction:()V
        //    20: aload_0        
        //    21: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //    24: aload_1        
        //    25: invokevirtual   com/google/android/gms/analytics/internal/zzh.zzii:()J
        //    28: aload_1        
        //    29: invokevirtual   com/google/android/gms/analytics/internal/zzh.getClientId:()Ljava/lang/String;
        //    32: invokevirtual   com/google/android/gms/analytics/internal/zzj.zza:(JLjava/lang/String;)V
        //    35: aload_0        
        //    36: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //    39: aload_1        
        //    40: invokevirtual   com/google/android/gms/analytics/internal/zzh.zzii:()J
        //    43: aload_1        
        //    44: invokevirtual   com/google/android/gms/analytics/internal/zzh.getClientId:()Ljava/lang/String;
        //    47: aload_1        
        //    48: invokevirtual   com/google/android/gms/analytics/internal/zzh.zzij:()Ljava/lang/String;
        //    51: invokevirtual   com/google/android/gms/analytics/internal/zzj.zza:(JLjava/lang/String;Ljava/lang/String;)J
        //    54: lstore_3       
        //    55: iload_2        
        //    56: ifne            88
        //    59: aload_1        
        //    60: lload_3        
        //    61: invokevirtual   com/google/android/gms/analytics/internal/zzh.zzn:(J)V
        //    64: aload_0        
        //    65: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //    68: aload_1        
        //    69: invokevirtual   com/google/android/gms/analytics/internal/zzj.zzb:(Lcom/google/android/gms/analytics/internal/zzh;)V
        //    72: aload_0        
        //    73: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //    76: invokevirtual   com/google/android/gms/analytics/internal/zzj.setTransactionSuccessful:()V
        //    79: aload_0        
        //    80: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //    83: invokevirtual   com/google/android/gms/analytics/internal/zzj.endTransaction:()V
        //    86: lload_3        
        //    87: lreturn        
        //    88: aload_1        
        //    89: lconst_1       
        //    90: lload_3        
        //    91: ladd           
        //    92: invokevirtual   com/google/android/gms/analytics/internal/zzh.zzn:(J)V
        //    95: goto            64
        //    98: astore_1       
        //    99: aload_0        
        //   100: ldc_w           "Failed to update Analytics property"
        //   103: aload_1        
        //   104: invokevirtual   com/google/android/gms/analytics/internal/zzl.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   107: aload_0        
        //   108: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //   111: invokevirtual   com/google/android/gms/analytics/internal/zzj.endTransaction:()V
        //   114: ldc2_w          -1
        //   117: lreturn        
        //   118: astore_1       
        //   119: aload_0        
        //   120: ldc_w           "Failed to end transaction"
        //   123: aload_1        
        //   124: invokevirtual   com/google/android/gms/analytics/internal/zzl.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   127: ldc2_w          -1
        //   130: lreturn        
        //   131: astore_1       
        //   132: aload_0        
        //   133: ldc_w           "Failed to end transaction"
        //   136: aload_1        
        //   137: invokevirtual   com/google/android/gms/analytics/internal/zzl.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   140: lload_3        
        //   141: lreturn        
        //   142: astore_1       
        //   143: aload_0        
        //   144: getfield        com/google/android/gms/analytics/internal/zzl.zzKn:Lcom/google/android/gms/analytics/internal/zzj;
        //   147: invokevirtual   com/google/android/gms/analytics/internal/zzj.endTransaction:()V
        //   150: aload_1        
        //   151: athrow         
        //   152: astore          5
        //   154: aload_0        
        //   155: ldc_w           "Failed to end transaction"
        //   158: aload           5
        //   160: invokevirtual   com/google/android/gms/analytics/internal/zzl.zze:(Ljava/lang/String;Ljava/lang/Object;)V
        //   163: goto            150
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  13     55     98     131    Landroid/database/sqlite/SQLiteException;
        //  13     55     142    166    Any
        //  59     64     98     131    Landroid/database/sqlite/SQLiteException;
        //  59     64     142    166    Any
        //  64     79     98     131    Landroid/database/sqlite/SQLiteException;
        //  64     79     142    166    Any
        //  79     86     131    142    Landroid/database/sqlite/SQLiteException;
        //  88     95     98     131    Landroid/database/sqlite/SQLiteException;
        //  88     95     142    166    Any
        //  99     107    142    166    Any
        //  107    114    118    131    Landroid/database/sqlite/SQLiteException;
        //  143    150    152    166    Landroid/database/sqlite/SQLiteException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0088:
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
    
    public void zza(zzab zzf) {
        zzu.zzu(zzf);
        zzns.zzhO();
        this.zzia();
        if (this.zzKw) {
            this.zzaU("Hit delivery not possible. Missing network permissions. See http://goo.gl/8Rd3yj for instructions");
        }
        else {
            this.zza("Delivering hit", zzf);
        }
        zzf = this.zzf(zzf);
        this.zziC();
        if (this.zzKq.zzb(zzf)) {
            this.zzaU("Hit sent to the device AnalyticsService for delivery");
            return;
        }
        if (this.zzhR().zziW()) {
            this.zzhQ().zza(zzf, "Service unavailable on package side");
            return;
        }
        try {
            this.zzKn.zzc(zzf);
            this.zziG();
        }
        catch (SQLiteException ex) {
            this.zze("Delivery failed to save hit to a database", ex);
            this.zzhQ().zza(zzf, "deliver: failed to insert hit to database");
        }
    }
    
    public void zza(final zzw zzw, final long n) {
        zzns.zzhO();
        this.zzia();
        long abs = -1L;
        final long zzkm = this.zzhU().zzkm();
        if (zzkm != 0L) {
            abs = Math.abs(this.zzhP().currentTimeMillis() - zzkm);
        }
        this.zzb("Dispatching local hits. Elapsed time since last dispatch (ms)", abs);
        if (!this.zzhR().zziW()) {
            this.zziC();
        }
        try {
            if (this.zziE()) {
                this.zzhS().zze(new Runnable() {
                    @Override
                    public void run() {
                        zzl.this.zza(zzw, n);
                    }
                });
                return;
            }
            this.zzhU().zzkn();
            this.zziG();
            if (zzw != null) {
                zzw.zzc(null);
            }
            if (this.zzKv != n) {
                this.zzKp.zzkf();
            }
        }
        catch (Throwable t) {
            this.zze("Local dispatch failed", t);
            this.zzhU().zzkn();
            this.zziG();
            if (zzw != null) {
                zzw.zzc(t);
            }
        }
    }
    
    public void zzb(final zzw zzw) {
        this.zza(zzw, this.zzKv);
    }
    
    public void zzbb(final String s) {
        zzu.zzcj(s);
        this.zzhO();
        this.zzhN();
        final zzny zza = zzam.zza(this.zzhQ(), s);
        if (zza == null) {
            this.zzd("Parsing failed. Ignoring invalid campaign data", s);
        }
        else {
            final String zzko = this.zzhU().zzko();
            if (s.equals(zzko)) {
                this.zzaW("Ignoring duplicate install campaign");
                return;
            }
            if (!TextUtils.isEmpty((CharSequence)zzko)) {
                this.zzd("Ignoring multiple install campaigns. original, new", zzko, s);
                return;
            }
            this.zzhU().zzbf(s);
            if (this.zzhU().zzkl().zzv(this.zzhR().zzjB())) {
                this.zzd("Campaign received too late, ignoring", zza);
                return;
            }
            this.zzb("Received installation campaign", zza);
            final Iterator<zzh> iterator = this.zzKn.zzr(0L).iterator();
            while (iterator.hasNext()) {
                this.zza(iterator.next(), zza);
            }
        }
    }
    
    protected void zzc(final zzh zzh) {
        this.zzhO();
        this.zzb("Sending first hit to property", zzh.zzij());
        if (!this.zzhU().zzkl().zzv(this.zzhR().zzjB())) {
            final String zzko = this.zzhU().zzko();
            if (!TextUtils.isEmpty((CharSequence)zzko)) {
                final zzny zza = zzam.zza(this.zzhQ(), zzko);
                this.zzb("Found relevant installation campaign", zza);
                this.zza(zzh, zza);
            }
        }
    }
    
    zzab zzf(final zzab zzab) {
        if (TextUtils.isEmpty((CharSequence)zzab.zzka())) {
            final Pair<String, Long> zzks = this.zzhU().zzkp().zzks();
            if (zzks != null) {
                final String string = zzks.second + ":" + (String)zzks.first;
                final HashMap<String, String> hashMap = new HashMap<String, String>(zzab.zzn());
                hashMap.put("_m", string);
                return zzab.zza(this, zzab, hashMap);
            }
        }
        return zzab;
    }
    
    public void zzhJ() {
        zzns.zzhO();
        this.zzia();
        this.zzaT("Service disconnected");
    }
    
    void zzhL() {
        this.zzhO();
        this.zzKv = this.zzhP().currentTimeMillis();
    }
    
    @Override
    protected void zzhn() {
        this.zzKn.zza();
        this.zzKo.zza();
        this.zzKq.zza();
    }
    
    protected void zziC() {
        if (!this.zzKw && this.zzhR().zziY() && !this.zzKq.isConnected() && this.zzKu.zzv(this.zzhR().zzjt())) {
            this.zzKu.start();
            this.zzaT("Connecting to service");
            if (this.zzKq.connect()) {
                this.zzaT("Connected to service");
                this.zzKu.clear();
                this.onServiceConnected();
            }
        }
    }
    
    public void zziD() {
        zzns.zzhO();
        this.zzia();
        this.zzhN();
        if (!this.zzhR().zziY()) {
            this.zzaW("Service client disabled. Can't dispatch local hits to device AnalyticsService");
        }
        if (!this.zzKq.isConnected()) {
            this.zzaT("Service not connected");
        }
        else if (!this.zzKn.isEmpty()) {
            this.zzaT("Dispatching local hits to device AnalyticsService");
            while (true) {
                Label_0126: {
                    List<zzab> zzp;
                    try {
                        zzp = this.zzKn.zzp(this.zzhR().zzjh());
                        if (zzp.isEmpty()) {
                            this.zziG();
                            return;
                        }
                        break Label_0126;
                    }
                    catch (SQLiteException ex) {
                        this.zze("Failed to read hits from store", ex);
                        this.zziK();
                        return;
                    }
                    while (true) {
                        zzab zzab = null;
                        zzp.remove(zzab);
                        try {
                            this.zzKn.zzq(zzab.zzjV());
                            if (zzp.isEmpty()) {
                                break;
                            }
                            zzab = zzp.get(0);
                            if (!this.zzKq.zzb(zzab)) {
                                this.zziG();
                                return;
                            }
                            continue;
                        }
                        catch (SQLiteException ex2) {
                            this.zze("Failed to remove hit that was send for delivery", ex2);
                            this.zziK();
                        }
                    }
                }
            }
        }
    }
    
    protected boolean zziE() {
        boolean b = true;
        zzns.zzhO();
        this.zzia();
        this.zzaT("Dispatching a batch of local hits");
        boolean b2;
        if (!this.zzKq.isConnected() && !this.zzhR().zziW()) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if (this.zzKo.zzkg()) {
            b = false;
        }
        if (b2 && b) {
            this.zzaT("No network or service available. Will retry later");
            return false;
        }
        final long n = Math.max(this.zzhR().zzjh(), this.zzhR().zzji());
        final ArrayList<Long> list = new ArrayList<Long>();
        long n2 = 0L;
    Label_0416_Outer:
        while (true) {
            while (true) {
                long n3 = 0L;
                Label_0788: {
                    try {
                        while (true) {
                            this.zzKn.beginTransaction();
                            list.clear();
                            List<zzab> zzp = null;
                            Label_0345: {
                                try {
                                    zzp = this.zzKn.zzp(n);
                                    if (zzp.isEmpty()) {
                                        this.zzaT("Store is empty, nothing to dispatch");
                                        this.zziK();
                                        try {
                                            this.zzKn.setTransactionSuccessful();
                                            this.zzKn.endTransaction();
                                            return false;
                                        }
                                        catch (SQLiteException ex) {
                                            this.zze("Failed to commit local dispatch transaction", ex);
                                            this.zziK();
                                            return false;
                                        }
                                    }
                                    this.zza("Hits loaded from store. count", zzp.size());
                                    final Iterator<zzab> iterator = zzp.iterator();
                                    Block_19: {
                                        while (iterator.hasNext()) {
                                            if (iterator.next().zzjV() == n2) {
                                                break Block_19;
                                            }
                                        }
                                        break Label_0345;
                                    }
                                    this.zzd("Database contains successfully uploaded hit", n2, zzp.size());
                                    this.zziK();
                                    try {
                                        this.zzKn.setTransactionSuccessful();
                                        this.zzKn.endTransaction();
                                        return false;
                                    }
                                    catch (SQLiteException ex2) {
                                        this.zze("Failed to commit local dispatch transaction", ex2);
                                        this.zziK();
                                        return false;
                                    }
                                }
                                catch (SQLiteException ex3) {
                                    this.zzd("Failed to read hits from persisted store", ex3);
                                    this.zziK();
                                    try {
                                        this.zzKn.setTransactionSuccessful();
                                        this.zzKn.endTransaction();
                                        return false;
                                    }
                                    catch (SQLiteException ex4) {
                                        this.zze("Failed to commit local dispatch transaction", ex4);
                                        this.zziK();
                                        return false;
                                    }
                                }
                            }
                            n3 = n2;
                            if (!this.zzKq.isConnected()) {
                                break;
                            }
                            n3 = n2;
                            if (this.zzhR().zziW()) {
                                break;
                            }
                            this.zzaT("Service connected, sending hits to the service");
                            Label_0620: {
                                List<Long> zzf;
                                while (true) {
                                    n3 = n2;
                                    if (zzp.isEmpty()) {
                                        break Label_0788;
                                    }
                                    final zzab zzab = zzp.get(0);
                                    if (this.zzKq.zzb(zzab)) {
                                        n2 = Math.max(n2, zzab.zzjV());
                                        zzp.remove(zzab);
                                        this.zzb("Hit sent do device AnalyticsService for delivery", zzab);
                                        try {
                                            this.zzKn.zzq(zzab.zzjV());
                                            list.add(zzab.zzjV());
                                            continue Label_0416_Outer;
                                        }
                                        catch (SQLiteException ex5) {
                                            this.zze("Failed to remove hit that was send for delivery", ex5);
                                            this.zziK();
                                            try {
                                                this.zzKn.setTransactionSuccessful();
                                                this.zzKn.endTransaction();
                                                return false;
                                            }
                                            catch (SQLiteException ex6) {
                                                this.zze("Failed to commit local dispatch transaction", ex6);
                                                this.zziK();
                                                return false;
                                            }
                                        }
                                        break;
                                    }
                                    n3 = n2;
                                    if (this.zzKo.zzkg()) {
                                        zzf = this.zzKo.zzf(zzp);
                                        final Iterator<Long> iterator2 = zzf.iterator();
                                        while (iterator2.hasNext()) {
                                            n2 = Math.max(n2, iterator2.next());
                                        }
                                        break;
                                    }
                                    break Label_0620;
                                }
                                zzp.removeAll(zzf);
                                try {
                                    this.zzKn.zzd(zzf);
                                    list.addAll((Collection<?>)zzf);
                                    n3 = n2;
                                    if (list.isEmpty()) {
                                        try {
                                            this.zzKn.setTransactionSuccessful();
                                            this.zzKn.endTransaction();
                                            return false;
                                        }
                                        catch (SQLiteException ex7) {
                                            this.zze("Failed to commit local dispatch transaction", ex7);
                                            this.zziK();
                                            return false;
                                        }
                                    }
                                }
                                catch (SQLiteException ex8) {
                                    this.zze("Failed to remove successfully uploaded hits", ex8);
                                    this.zziK();
                                    try {
                                        this.zzKn.setTransactionSuccessful();
                                        this.zzKn.endTransaction();
                                        return false;
                                    }
                                    catch (SQLiteException ex9) {
                                        this.zze("Failed to commit local dispatch transaction", ex9);
                                        this.zziK();
                                        return false;
                                    }
                                }
                            }
                            try {
                                this.zzKn.setTransactionSuccessful();
                                this.zzKn.endTransaction();
                                n2 = n3;
                            }
                            catch (SQLiteException ex10) {
                                this.zze("Failed to commit local dispatch transaction", ex10);
                                this.zziK();
                                return false;
                            }
                        }
                    }
                    finally {
                        try {
                            this.zzKn.setTransactionSuccessful();
                            this.zzKn.endTransaction();
                        }
                        catch (SQLiteException list) {
                            this.zze("Failed to commit local dispatch transaction", list);
                            this.zziK();
                            return false;
                        }
                    }
                }
                n2 = n3;
                continue;
            }
        }
    }
    
    public void zziG() {
        this.zzhM().zzhO();
        this.zzia();
        if (!this.zziH()) {
            this.zzKp.unregister();
            this.zziK();
            return;
        }
        if (this.zzKn.isEmpty()) {
            this.zzKp.unregister();
            this.zziK();
            return;
        }
        int connected;
        if (!zzy.zzLI.get()) {
            this.zzKp.zzkd();
            connected = (this.zzKp.isConnected() ? 1 : 0);
        }
        else {
            connected = 1;
        }
        if (connected != 0) {
            this.zziJ();
            return;
        }
        this.zziK();
        this.zziI();
    }
    
    public long zziN() {
        long n;
        if (this.zzKr != Long.MIN_VALUE) {
            n = this.zzKr;
        }
        else {
            n = this.zzhR().zzje();
            if (this.zzhm().zzjQ()) {
                return this.zzhm().zzkH() * 1000L;
            }
        }
        return n;
    }
    
    public void zziO() {
        this.zzia();
        this.zzhO();
        this.zzKw = true;
        this.zzKq.disconnect();
        this.zziG();
    }
    
    public long zzit() {
        zzns.zzhO();
        this.zzia();
        try {
            return this.zzKn.zzit();
        }
        catch (SQLiteException ex) {
            this.zze("Failed to get min/max hit times from local store", ex);
            return 0L;
        }
    }
    
    protected void zziz() {
        this.zzia();
        this.zzhU().zzkk();
        if (!this.zzba("android.permission.ACCESS_NETWORK_STATE")) {
            this.zzaX("Missing required android.permission.ACCESS_NETWORK_STATE. Google Analytics disabled. See http://goo.gl/8Rd3yj for instructions");
            this.zziO();
        }
        if (!this.zzba("android.permission.INTERNET")) {
            this.zzaX("Missing required android.permission.INTERNET. Google Analytics disabled. See http://goo.gl/8Rd3yj for instructions");
            this.zziO();
        }
        if (AnalyticsService.zzU(this.getContext())) {
            this.zzaT("AnalyticsService registered in the app manifest and enabled");
        }
        else if (this.zzhR().zziW()) {
            this.zzaX("Device AnalyticsService not registered! Hits will not be delivered reliably.");
        }
        else {
            this.zzaW("AnalyticsService not registered in the app manifest. Hits might not be delivered reliably. See http://goo.gl/8Rd3yj for instructions.");
        }
        if (!this.zzKw && !this.zzhR().zziW() && !this.zzKn.isEmpty()) {
            this.zziC();
        }
        this.zziG();
    }
}
