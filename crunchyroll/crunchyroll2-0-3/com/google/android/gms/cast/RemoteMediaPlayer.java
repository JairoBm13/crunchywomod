// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import com.google.android.gms.cast.internal.zzb;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.cast.internal.zzo;
import java.io.IOException;
import java.util.Locale;
import com.google.android.gms.common.api.Status;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.cast.internal.zze;
import org.json.JSONObject;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.cast.internal.zzn;
import com.google.android.gms.cast.internal.zzm;

public class RemoteMediaPlayer implements MessageReceivedCallback
{
    public static final int RESUME_STATE_PAUSE = 2;
    public static final int RESUME_STATE_PLAY = 1;
    public static final int RESUME_STATE_UNCHANGED = 0;
    public static final int STATUS_CANCELED = 2101;
    public static final int STATUS_FAILED = 2100;
    public static final int STATUS_REPLACED = 2103;
    public static final int STATUS_SUCCEEDED = 0;
    public static final int STATUS_TIMED_OUT = 2102;
    private final zzm zzSt;
    private final zza zzSu;
    private OnPreloadStatusUpdatedListener zzSv;
    private OnQueueStatusUpdatedListener zzSw;
    private OnMetadataUpdatedListener zzSx;
    private OnStatusUpdatedListener zzSy;
    private final Object zzqt;
    
    public RemoteMediaPlayer() {
        this.zzqt = new Object();
        this.zzSu = new zza();
        (this.zzSt = new zzm(null) {
            @Override
            protected void onMetadataUpdated() {
                RemoteMediaPlayer.this.onMetadataUpdated();
            }
            
            @Override
            protected void onPreloadStatusUpdated() {
                RemoteMediaPlayer.this.onPreloadStatusUpdated();
            }
            
            @Override
            protected void onQueueStatusUpdated() {
                RemoteMediaPlayer.this.onQueueStatusUpdated();
            }
            
            @Override
            protected void onStatusUpdated() {
                RemoteMediaPlayer.this.onStatusUpdated();
            }
        }).zza(this.zzSu);
    }
    
    private void onMetadataUpdated() {
        if (this.zzSx != null) {
            this.zzSx.onMetadataUpdated();
        }
    }
    
    private void onPreloadStatusUpdated() {
        if (this.zzSv != null) {
            this.zzSv.onPreloadStatusUpdated();
        }
    }
    
    private void onQueueStatusUpdated() {
        if (this.zzSw != null) {
            this.zzSw.onQueueStatusUpdated();
        }
    }
    
    private void onStatusUpdated() {
        if (this.zzSy != null) {
            this.zzSy.onStatusUpdated();
        }
    }
    
    private int zzaH(final int n) {
        final MediaStatus mediaStatus = this.getMediaStatus();
        for (int i = 0; i < mediaStatus.getQueueItemCount(); ++i) {
            if (mediaStatus.getQueueItem(i).getItemId() == n) {
                return i;
            }
        }
        return -1;
    }
    
    public long getApproximateStreamPosition() {
        synchronized (this.zzqt) {
            return this.zzSt.getApproximateStreamPosition();
        }
    }
    
    public MediaInfo getMediaInfo() {
        synchronized (this.zzqt) {
            return this.zzSt.getMediaInfo();
        }
    }
    
    public MediaStatus getMediaStatus() {
        synchronized (this.zzqt) {
            return this.zzSt.getMediaStatus();
        }
    }
    
    public String getNamespace() {
        return this.zzSt.getNamespace();
    }
    
    public long getStreamDuration() {
        synchronized (this.zzqt) {
            return this.zzSt.getStreamDuration();
        }
    }
    
    public PendingResult<MediaChannelResult> load(final GoogleApiClient googleApiClient, final MediaInfo mediaInfo) {
        return this.load(googleApiClient, mediaInfo, true, 0L, null, null);
    }
    
    public PendingResult<MediaChannelResult> load(final GoogleApiClient googleApiClient, final MediaInfo mediaInfo, final boolean b) {
        return this.load(googleApiClient, mediaInfo, b, 0L, null, null);
    }
    
    public PendingResult<MediaChannelResult> load(final GoogleApiClient googleApiClient, final MediaInfo mediaInfo, final boolean b, final long n) {
        return this.load(googleApiClient, mediaInfo, b, n, null, null);
    }
    
    public PendingResult<MediaChannelResult> load(final GoogleApiClient googleApiClient, final MediaInfo mediaInfo, final boolean b, final long n, final JSONObject jsonObject) {
        return this.load(googleApiClient, mediaInfo, b, n, null, jsonObject);
    }
    
    public PendingResult<MediaChannelResult> load(final GoogleApiClient googleApiClient, final MediaInfo mediaInfo, final boolean b, final long n, final long[] array, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSM:Lcom/google/android/gms/cast/MediaInfo;
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSN:Z
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSO:J
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSP:[J
                //    51: aload_0        
                //    52: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSG:Lorg/json/JSONObject;
                //    55: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;Lcom/google/android/gms/cast/MediaInfo;ZJ[JLorg/json/JSONObject;)J
                //    58: pop2           
                //    59: aload_0        
                //    60: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    63: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    66: aconst_null    
                //    67: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    70: aload_1        
                //    71: monitorexit    
                //    72: return         
                //    73: astore_2       
                //    74: aload_0        
                //    75: aload_0        
                //    76: new             Lcom/google/android/gms/common/api/Status;
                //    79: dup            
                //    80: sipush          2100
                //    83: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    86: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$12.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    89: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$12.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    92: aload_0        
                //    93: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    96: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    99: aconst_null    
                //   100: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   103: goto            70
                //   106: astore_2       
                //   107: aload_1        
                //   108: monitorexit    
                //   109: aload_2        
                //   110: athrow         
                //   111: astore_2       
                //   112: aload_0        
                //   113: getfield        com/google/android/gms/cast/RemoteMediaPlayer$12.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   116: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   119: aconst_null    
                //   120: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   123: aload_2        
                //   124: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     106    111    Any
                //  24     59     73     106    Ljava/io/IOException;
                //  24     59     111    125    Any
                //  59     70     106    111    Any
                //  70     72     106    111    Any
                //  74     92     111    125    Any
                //  92     103    106    111    Any
                //  107    109    106    111    Any
                //  112    125    106    111    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0070:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    @Override
    public void onMessageReceived(final CastDevice castDevice, final String s, final String s2) {
        this.zzSt.zzbB(s2);
    }
    
    public PendingResult<MediaChannelResult> pause(final GoogleApiClient googleApiClient) {
        return this.pause(googleApiClient, null);
    }
    
    public PendingResult<MediaChannelResult> pause(final GoogleApiClient googleApiClient, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSG:Lorg/json/JSONObject;
                //    39: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;Lorg/json/JSONObject;)J
                //    42: pop2           
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    47: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    50: aconst_null    
                //    51: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    54: aload_1        
                //    55: monitorexit    
                //    56: return         
                //    57: astore_2       
                //    58: aload_0        
                //    59: aload_0        
                //    60: new             Lcom/google/android/gms/common/api/Status;
                //    63: dup            
                //    64: sipush          2100
                //    67: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    70: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$16.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    73: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$16.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    76: aload_0        
                //    77: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    80: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    83: aconst_null    
                //    84: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    87: goto            54
                //    90: astore_2       
                //    91: aload_1        
                //    92: monitorexit    
                //    93: aload_2        
                //    94: athrow         
                //    95: astore_2       
                //    96: aload_0        
                //    97: getfield        com/google/android/gms/cast/RemoteMediaPlayer$16.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   100: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   103: aconst_null    
                //   104: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   107: aload_2        
                //   108: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     90     95     Any
                //  24     43     57     90     Ljava/io/IOException;
                //  24     43     95     109    Any
                //  43     54     90     95     Any
                //  54     56     90     95     Any
                //  58     76     95     109    Any
                //  76     87     90     95     Any
                //  91     93     90     95     Any
                //  96     109    90     95     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0054:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> play(final GoogleApiClient googleApiClient) {
        return this.play(googleApiClient, null);
    }
    
    public PendingResult<MediaChannelResult> play(final GoogleApiClient googleApiClient, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSG:Lorg/json/JSONObject;
                //    39: invokevirtual   com/google/android/gms/cast/internal/zzm.zzc:(Lcom/google/android/gms/cast/internal/zzo;Lorg/json/JSONObject;)J
                //    42: pop2           
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    47: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    50: aconst_null    
                //    51: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    54: aload_1        
                //    55: monitorexit    
                //    56: return         
                //    57: astore_2       
                //    58: aload_0        
                //    59: aload_0        
                //    60: new             Lcom/google/android/gms/common/api/Status;
                //    63: dup            
                //    64: sipush          2100
                //    67: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    70: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$18.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    73: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$18.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    76: aload_0        
                //    77: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    80: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    83: aconst_null    
                //    84: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    87: goto            54
                //    90: astore_2       
                //    91: aload_1        
                //    92: monitorexit    
                //    93: aload_2        
                //    94: athrow         
                //    95: astore_2       
                //    96: aload_0        
                //    97: getfield        com/google/android/gms/cast/RemoteMediaPlayer$18.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   100: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   103: aconst_null    
                //   104: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   107: aload_2        
                //   108: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     90     95     Any
                //  24     43     57     90     Ljava/io/IOException;
                //  24     43     95     109    Any
                //  43     54     90     95     Any
                //  54     56     90     95     Any
                //  58     76     95     109    Any
                //  76     87     90     95     Any
                //  91     93     90     95     Any
                //  96     109    90     95     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0054:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueAppendItem(final GoogleApiClient googleApiClient, final MediaQueueItem mediaQueueItem, final JSONObject jsonObject) throws IllegalArgumentException {
        return this.queueInsertItems(googleApiClient, new MediaQueueItem[] { mediaQueueItem }, 0, jsonObject);
    }
    
    public PendingResult<MediaChannelResult> queueInsertItems(final GoogleApiClient googleApiClient, final MediaQueueItem[] array, final int n, final JSONObject jsonObject) throws IllegalArgumentException {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSH:[Lcom/google/android/gms/cast/MediaQueueItem;
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSI:I
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSG:Lorg/json/JSONObject;
                //    47: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;[Lcom/google/android/gms/cast/MediaQueueItem;ILorg/json/JSONObject;)J
                //    50: pop2           
                //    51: aload_0        
                //    52: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    55: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    58: aconst_null    
                //    59: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    62: aload_1        
                //    63: monitorexit    
                //    64: return         
                //    65: astore_2       
                //    66: aload_0        
                //    67: aload_0        
                //    68: new             Lcom/google/android/gms/common/api/Status;
                //    71: dup            
                //    72: sipush          2100
                //    75: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    78: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$5.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    81: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$5.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    84: aload_0        
                //    85: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    88: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    91: aconst_null    
                //    92: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    95: goto            62
                //    98: astore_2       
                //    99: aload_1        
                //   100: monitorexit    
                //   101: aload_2        
                //   102: athrow         
                //   103: astore_2       
                //   104: aload_0        
                //   105: getfield        com/google/android/gms/cast/RemoteMediaPlayer$5.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   108: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   111: aconst_null    
                //   112: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   115: aload_2        
                //   116: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     98     103    Any
                //  24     51     65     98     Ljava/io/IOException;
                //  24     51     103    117    Any
                //  51     62     98     103    Any
                //  62     64     98     103    Any
                //  66     84     103    117    Any
                //  84     95     98     103    Any
                //  99     101    98     103    Any
                //  104    117    98     103    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0062:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueJumpToItem(final GoogleApiClient googleApiClient, final int n, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: aload_0        
                //    15: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSQ:I
                //    18: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zza:(Lcom/google/android/gms/cast/RemoteMediaPlayer;I)I
                //    21: iconst_m1      
                //    22: if_icmpne       44
                //    25: aload_0        
                //    26: aload_0        
                //    27: new             Lcom/google/android/gms/common/api/Status;
                //    30: dup            
                //    31: iconst_0       
                //    32: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    35: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$14.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    38: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$14.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    41: aload_1        
                //    42: monitorexit    
                //    43: return         
                //    44: aload_0        
                //    45: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    48: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    51: aload_0        
                //    52: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_0        
                //    59: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    62: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    65: aload_0        
                //    66: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    69: aload_0        
                //    70: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSQ:I
                //    73: aconst_null    
                //    74: iconst_0       
                //    75: aconst_null    
                //    76: aload_0        
                //    77: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSG:Lorg/json/JSONObject;
                //    80: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;I[Lcom/google/android/gms/cast/MediaQueueItem;ILjava/lang/Integer;Lorg/json/JSONObject;)J
                //    83: pop2           
                //    84: aload_0        
                //    85: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    88: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    91: aconst_null    
                //    92: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    95: aload_1        
                //    96: monitorexit    
                //    97: return         
                //    98: astore_2       
                //    99: aload_1        
                //   100: monitorexit    
                //   101: aload_2        
                //   102: athrow         
                //   103: astore_2       
                //   104: aload_0        
                //   105: aload_0        
                //   106: new             Lcom/google/android/gms/common/api/Status;
                //   109: dup            
                //   110: sipush          2100
                //   113: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //   116: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$14.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //   119: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$14.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //   122: aload_0        
                //   123: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   126: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   129: aconst_null    
                //   130: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   133: goto            95
                //   136: astore_2       
                //   137: aload_0        
                //   138: getfield        com/google/android/gms/cast/RemoteMediaPlayer$14.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   141: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   144: aconst_null    
                //   145: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   148: aload_2        
                //   149: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     43     98     103    Any
                //  44     58     98     103    Any
                //  58     84     103    136    Ljava/io/IOException;
                //  58     84     136    150    Any
                //  84     95     98     103    Any
                //  95     97     98     103    Any
                //  99     101    98     103    Any
                //  104    122    136    150    Any
                //  122    133    98     103    Any
                //  137    150    98     103    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0095:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueLoad(final GoogleApiClient googleApiClient, final MediaQueueItem[] array, final int n, final int n2, final JSONObject jsonObject) throws IllegalArgumentException {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSD:[Lcom/google/android/gms/cast/MediaQueueItem;
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSE:I
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSF:I
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSG:Lorg/json/JSONObject;
                //    51: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;[Lcom/google/android/gms/cast/MediaQueueItem;IILorg/json/JSONObject;)J
                //    54: pop2           
                //    55: aload_0        
                //    56: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    59: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    62: aconst_null    
                //    63: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    66: aload_1        
                //    67: monitorexit    
                //    68: return         
                //    69: astore_2       
                //    70: aload_0        
                //    71: aload_0        
                //    72: new             Lcom/google/android/gms/common/api/Status;
                //    75: dup            
                //    76: sipush          2100
                //    79: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    82: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$4.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    85: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$4.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    88: aload_0        
                //    89: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    92: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    95: aconst_null    
                //    96: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    99: goto            66
                //   102: astore_2       
                //   103: aload_1        
                //   104: monitorexit    
                //   105: aload_2        
                //   106: athrow         
                //   107: astore_2       
                //   108: aload_0        
                //   109: getfield        com/google/android/gms/cast/RemoteMediaPlayer$4.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   112: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   115: aconst_null    
                //   116: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   119: aload_2        
                //   120: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     102    107    Any
                //  24     55     69     102    Ljava/io/IOException;
                //  24     55     107    121    Any
                //  55     66     102    107    Any
                //  66     68     102    107    Any
                //  70     88     107    121    Any
                //  88     99     102    107    Any
                //  103    105    102    107    Any
                //  108    121    102    107    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0066:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueMoveItemToNewIndex(final GoogleApiClient googleApiClient, final int n, final int n2, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze zze) {
                int zzSQ = 0;
                int n;
                synchronized (RemoteMediaPlayer.this.zzqt) {
                    n = RemoteMediaPlayer.this.zzaH(n);
                    if (n == -1) {
                        this.setResult((R)((zzb)this).zzn(new Status(0)));
                        return;
                    }
                    if (n2 < 0) {
                        this.setResult((R)((zzb)this).zzn(new Status(2001, String.format(Locale.ROOT, "Invalid request: Invalid newIndex %d.", n2))));
                        return;
                    }
                }
                if (n == n2) {
                    this.setResult((R)((zzb)this).zzn(new Status(0)));
                    // monitorexit(zze)
                    return;
                }
                Label_0245: {
                    if (n2 <= n) {
                        break Label_0245;
                    }
                    n = n2 + 1;
                Label_0242_Outer:
                    while (true) {
                        final MediaQueueItem queueItem = RemoteMediaPlayer.this.getMediaStatus().getQueueItem(n);
                        n = zzSQ;
                        if (queueItem != null) {
                            n = queueItem.getItemId();
                        }
                        RemoteMediaPlayer.this.zzSu.zzb(googleApiClient);
                        while (true) {
                            try {
                                final zzm zzg = RemoteMediaPlayer.this.zzSt;
                                final zzo zzTa = this.zzTa;
                                zzSQ = n;
                                zzg.zza(zzTa, new int[] { zzSQ }, n, jsonObject);
                                RemoteMediaPlayer.this.zzSu.zzb(null);
                                // monitorexit(zze)
                                return;
                                n = n2;
                                continue Label_0242_Outer;
                            }
                            catch (IOException ex) {
                                this.setResult((R)((zzb)this).zzn(new Status(2100)));
                                RemoteMediaPlayer.this.zzSu.zzb(null);
                                continue;
                            }
                            finally {
                                RemoteMediaPlayer.this.zzSu.zzb(null);
                            }
                            break;
                        }
                        break;
                    }
                }
            }
        });
    }
    
    public PendingResult<MediaChannelResult> queueNext(final GoogleApiClient googleApiClient, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: iconst_0       
                //    36: aconst_null    
                //    37: iconst_1       
                //    38: aconst_null    
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSG:Lorg/json/JSONObject;
                //    43: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;I[Lcom/google/android/gms/cast/MediaQueueItem;ILjava/lang/Integer;Lorg/json/JSONObject;)J
                //    46: pop2           
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    51: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    54: aconst_null    
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_1        
                //    59: monitorexit    
                //    60: return         
                //    61: astore_2       
                //    62: aload_0        
                //    63: aload_0        
                //    64: new             Lcom/google/android/gms/common/api/Status;
                //    67: dup            
                //    68: sipush          2100
                //    71: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    74: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$10.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    77: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$10.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    80: aload_0        
                //    81: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    84: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    87: aconst_null    
                //    88: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    91: goto            58
                //    94: astore_2       
                //    95: aload_1        
                //    96: monitorexit    
                //    97: aload_2        
                //    98: athrow         
                //    99: astore_2       
                //   100: aload_0        
                //   101: getfield        com/google/android/gms/cast/RemoteMediaPlayer$10.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   104: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   107: aconst_null    
                //   108: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   111: aload_2        
                //   112: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     94     99     Any
                //  24     47     61     94     Ljava/io/IOException;
                //  24     47     99     113    Any
                //  47     58     94     99     Any
                //  58     60     94     99     Any
                //  62     80     99     113    Any
                //  80     91     94     99     Any
                //  95     97     94     99     Any
                //  100    113    94     99     Any
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queuePrev(final GoogleApiClient googleApiClient, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: iconst_0       
                //    36: aconst_null    
                //    37: iconst_m1      
                //    38: aconst_null    
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSG:Lorg/json/JSONObject;
                //    43: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;I[Lcom/google/android/gms/cast/MediaQueueItem;ILjava/lang/Integer;Lorg/json/JSONObject;)J
                //    46: pop2           
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    51: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    54: aconst_null    
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_1        
                //    59: monitorexit    
                //    60: return         
                //    61: astore_2       
                //    62: aload_0        
                //    63: aload_0        
                //    64: new             Lcom/google/android/gms/common/api/Status;
                //    67: dup            
                //    68: sipush          2100
                //    71: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    74: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$9.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    77: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$9.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    80: aload_0        
                //    81: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    84: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    87: aconst_null    
                //    88: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    91: goto            58
                //    94: astore_2       
                //    95: aload_1        
                //    96: monitorexit    
                //    97: aload_2        
                //    98: athrow         
                //    99: astore_2       
                //   100: aload_0        
                //   101: getfield        com/google/android/gms/cast/RemoteMediaPlayer$9.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   104: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   107: aconst_null    
                //   108: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   111: aload_2        
                //   112: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     94     99     Any
                //  24     47     61     94     Ljava/io/IOException;
                //  24     47     99     113    Any
                //  47     58     94     99     Any
                //  58     60     94     99     Any
                //  62     80     99     113    Any
                //  80     91     94     99     Any
                //  95     97     94     99     Any
                //  100    113    94     99     Any
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueRemoveItem(final GoogleApiClient googleApiClient, final int n, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: aload_0        
                //    15: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSQ:I
                //    18: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zza:(Lcom/google/android/gms/cast/RemoteMediaPlayer;I)I
                //    21: iconst_m1      
                //    22: if_icmpne       44
                //    25: aload_0        
                //    26: aload_0        
                //    27: new             Lcom/google/android/gms/common/api/Status;
                //    30: dup            
                //    31: iconst_0       
                //    32: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    35: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$13.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    38: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$13.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    41: aload_1        
                //    42: monitorexit    
                //    43: return         
                //    44: aload_0        
                //    45: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    48: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    51: aload_0        
                //    52: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_0        
                //    59: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    62: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    65: astore_3       
                //    66: aload_0        
                //    67: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    70: astore          4
                //    72: aload_0        
                //    73: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSQ:I
                //    76: istore_2       
                //    77: aload_0        
                //    78: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSG:Lorg/json/JSONObject;
                //    81: astore          5
                //    83: aload_3        
                //    84: aload           4
                //    86: iconst_1       
                //    87: newarray        I
                //    89: dup            
                //    90: iconst_0       
                //    91: iload_2        
                //    92: iastore        
                //    93: aload           5
                //    95: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;[ILorg/json/JSONObject;)J
                //    98: pop2           
                //    99: aload_0        
                //   100: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   103: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   106: aconst_null    
                //   107: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   110: aload_1        
                //   111: monitorexit    
                //   112: return         
                //   113: astore_3       
                //   114: aload_1        
                //   115: monitorexit    
                //   116: aload_3        
                //   117: athrow         
                //   118: astore_3       
                //   119: aload_0        
                //   120: aload_0        
                //   121: new             Lcom/google/android/gms/common/api/Status;
                //   124: dup            
                //   125: sipush          2100
                //   128: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //   131: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$13.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //   134: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$13.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //   137: aload_0        
                //   138: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   141: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   144: aconst_null    
                //   145: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   148: goto            110
                //   151: astore_3       
                //   152: aload_0        
                //   153: getfield        com/google/android/gms/cast/RemoteMediaPlayer$13.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   156: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   159: aconst_null    
                //   160: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   163: aload_3        
                //   164: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     43     113    118    Any
                //  44     58     113    118    Any
                //  58     99     118    151    Ljava/io/IOException;
                //  58     99     151    165    Any
                //  99     110    113    118    Any
                //  110    112    113    118    Any
                //  114    116    113    118    Any
                //  119    137    151    165    Any
                //  137    148    113    118    Any
                //  152    165    113    118    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0110:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueRemoveItems(final GoogleApiClient googleApiClient, final int[] array, final JSONObject jsonObject) throws IllegalArgumentException {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSK:[I
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSG:Lorg/json/JSONObject;
                //    43: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;[ILorg/json/JSONObject;)J
                //    46: pop2           
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    51: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    54: aconst_null    
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_1        
                //    59: monitorexit    
                //    60: return         
                //    61: astore_2       
                //    62: aload_0        
                //    63: aload_0        
                //    64: new             Lcom/google/android/gms/common/api/Status;
                //    67: dup            
                //    68: sipush          2100
                //    71: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    74: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$7.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    77: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$7.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    80: aload_0        
                //    81: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    84: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    87: aconst_null    
                //    88: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    91: goto            58
                //    94: astore_2       
                //    95: aload_1        
                //    96: monitorexit    
                //    97: aload_2        
                //    98: athrow         
                //    99: astore_2       
                //   100: aload_0        
                //   101: getfield        com/google/android/gms/cast/RemoteMediaPlayer$7.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   104: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   107: aconst_null    
                //   108: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   111: aload_2        
                //   112: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     94     99     Any
                //  24     47     61     94     Ljava/io/IOException;
                //  24     47     99     113    Any
                //  47     58     94     99     Any
                //  58     60     94     99     Any
                //  62     80     99     113    Any
                //  80     91     94     99     Any
                //  95     97     94     99     Any
                //  100    113    94     99     Any
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueReorderItems(final GoogleApiClient googleApiClient, final int[] array, final int n, final JSONObject jsonObject) throws IllegalArgumentException {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSL:[I
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSI:I
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSG:Lorg/json/JSONObject;
                //    47: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;[IILorg/json/JSONObject;)J
                //    50: pop2           
                //    51: aload_0        
                //    52: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    55: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    58: aconst_null    
                //    59: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    62: aload_1        
                //    63: monitorexit    
                //    64: return         
                //    65: astore_2       
                //    66: aload_0        
                //    67: aload_0        
                //    68: new             Lcom/google/android/gms/common/api/Status;
                //    71: dup            
                //    72: sipush          2100
                //    75: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    78: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$8.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    81: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$8.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    84: aload_0        
                //    85: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    88: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    91: aconst_null    
                //    92: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    95: goto            62
                //    98: astore_2       
                //    99: aload_1        
                //   100: monitorexit    
                //   101: aload_2        
                //   102: athrow         
                //   103: astore_2       
                //   104: aload_0        
                //   105: getfield        com/google/android/gms/cast/RemoteMediaPlayer$8.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   108: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   111: aconst_null    
                //   112: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   115: aload_2        
                //   116: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     98     103    Any
                //  24     51     65     98     Ljava/io/IOException;
                //  24     51     103    117    Any
                //  51     62     98     103    Any
                //  62     64     98     103    Any
                //  66     84     103    117    Any
                //  84     95     98     103    Any
                //  99     101    98     103    Any
                //  104    117    98     103    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0062:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueSetRepeatMode(final GoogleApiClient googleApiClient, final int n, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: iconst_0       
                //    36: aconst_null    
                //    37: iconst_0       
                //    38: aload_0        
                //    39: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSF:I
                //    42: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
                //    45: aload_0        
                //    46: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSG:Lorg/json/JSONObject;
                //    49: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;I[Lcom/google/android/gms/cast/MediaQueueItem;ILjava/lang/Integer;Lorg/json/JSONObject;)J
                //    52: pop2           
                //    53: aload_0        
                //    54: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    57: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    60: aconst_null    
                //    61: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    64: aload_1        
                //    65: monitorexit    
                //    66: return         
                //    67: astore_2       
                //    68: aload_0        
                //    69: aload_0        
                //    70: new             Lcom/google/android/gms/common/api/Status;
                //    73: dup            
                //    74: sipush          2100
                //    77: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    80: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$11.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    83: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$11.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    86: aload_0        
                //    87: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    90: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    93: aconst_null    
                //    94: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    97: goto            64
                //   100: astore_2       
                //   101: aload_1        
                //   102: monitorexit    
                //   103: aload_2        
                //   104: athrow         
                //   105: astore_2       
                //   106: aload_0        
                //   107: getfield        com/google/android/gms/cast/RemoteMediaPlayer$11.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   110: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   113: aconst_null    
                //   114: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   117: aload_2        
                //   118: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     100    105    Any
                //  24     53     67     100    Ljava/io/IOException;
                //  24     53     105    119    Any
                //  53     64     100    105    Any
                //  64     66     100    105    Any
                //  68     86     105    119    Any
                //  86     97     100    105    Any
                //  101    103    100    105    Any
                //  106    119    100    105    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0064:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> queueUpdateItems(final GoogleApiClient googleApiClient, final MediaQueueItem[] array, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: iconst_0       
                //    36: aload_0        
                //    37: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSJ:[Lcom/google/android/gms/cast/MediaQueueItem;
                //    40: iconst_0       
                //    41: aconst_null    
                //    42: aload_0        
                //    43: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSG:Lorg/json/JSONObject;
                //    46: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;I[Lcom/google/android/gms/cast/MediaQueueItem;ILjava/lang/Integer;Lorg/json/JSONObject;)J
                //    49: pop2           
                //    50: aload_0        
                //    51: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    54: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    57: aconst_null    
                //    58: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    61: aload_1        
                //    62: monitorexit    
                //    63: return         
                //    64: astore_2       
                //    65: aload_0        
                //    66: aload_0        
                //    67: new             Lcom/google/android/gms/common/api/Status;
                //    70: dup            
                //    71: sipush          2100
                //    74: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    77: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$6.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    80: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$6.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    83: aload_0        
                //    84: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    87: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    90: aconst_null    
                //    91: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    94: goto            61
                //    97: astore_2       
                //    98: aload_1        
                //    99: monitorexit    
                //   100: aload_2        
                //   101: athrow         
                //   102: astore_2       
                //   103: aload_0        
                //   104: getfield        com/google/android/gms/cast/RemoteMediaPlayer$6.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   107: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   110: aconst_null    
                //   111: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   114: aload_2        
                //   115: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     97     102    Any
                //  24     50     64     97     Ljava/io/IOException;
                //  24     50     102    116    Any
                //  50     61     97     102    Any
                //  61     63     97     102    Any
                //  65     83     102    116    Any
                //  83     94     97     102    Any
                //  98     100    97     102    Any
                //  103    116    97     102    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0061:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> requestStatus(final GoogleApiClient googleApiClient) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;)J
                //    38: pop2           
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    43: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    46: aconst_null    
                //    47: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    50: aload_1        
                //    51: monitorexit    
                //    52: return         
                //    53: astore_2       
                //    54: aload_0        
                //    55: aload_0        
                //    56: new             Lcom/google/android/gms/common/api/Status;
                //    59: dup            
                //    60: sipush          2100
                //    63: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    66: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$22.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    69: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$22.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    72: aload_0        
                //    73: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    76: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    79: aconst_null    
                //    80: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    83: goto            50
                //    86: astore_2       
                //    87: aload_1        
                //    88: monitorexit    
                //    89: aload_2        
                //    90: athrow         
                //    91: astore_2       
                //    92: aload_0        
                //    93: getfield        com/google/android/gms/cast/RemoteMediaPlayer$22.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    96: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    99: aconst_null    
                //   100: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   103: aload_2        
                //   104: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     86     91     Any
                //  24     39     53     86     Ljava/io/IOException;
                //  24     39     91     105    Any
                //  39     50     86     91     Any
                //  50     52     86     91     Any
                //  54     72     91     105    Any
                //  72     83     86     91     Any
                //  87     89     86     91     Any
                //  92     105    86     91     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0050:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> seek(final GoogleApiClient googleApiClient, final long n) {
        return this.seek(googleApiClient, n, 0, null);
    }
    
    public PendingResult<MediaChannelResult> seek(final GoogleApiClient googleApiClient, final long n, final int n2) {
        return this.seek(googleApiClient, n, n2, null);
    }
    
    public PendingResult<MediaChannelResult> seek(final GoogleApiClient googleApiClient, final long n, final int n2, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSS:J
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzST:I
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSG:Lorg/json/JSONObject;
                //    47: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;JILorg/json/JSONObject;)J
                //    50: pop2           
                //    51: aload_0        
                //    52: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    55: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    58: aconst_null    
                //    59: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    62: aload_1        
                //    63: monitorexit    
                //    64: return         
                //    65: astore_2       
                //    66: aload_0        
                //    67: aload_0        
                //    68: new             Lcom/google/android/gms/common/api/Status;
                //    71: dup            
                //    72: sipush          2100
                //    75: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    78: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$19.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    81: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$19.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    84: aload_0        
                //    85: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    88: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    91: aconst_null    
                //    92: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    95: goto            62
                //    98: astore_2       
                //    99: aload_1        
                //   100: monitorexit    
                //   101: aload_2        
                //   102: athrow         
                //   103: astore_2       
                //   104: aload_0        
                //   105: getfield        com/google/android/gms/cast/RemoteMediaPlayer$19.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   108: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   111: aconst_null    
                //   112: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   115: aload_2        
                //   116: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     98     103    Any
                //  24     51     65     98     Ljava/io/IOException;
                //  24     51     103    117    Any
                //  51     62     98     103    Any
                //  62     64     98     103    Any
                //  66     84     103    117    Any
                //  84     95     98     103    Any
                //  99     101    98     103    Any
                //  104    117    98     103    Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0062:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> setActiveMediaTracks(final GoogleApiClient googleApiClient, final long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("trackIds cannot be null");
        }
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSB:[J
                //    39: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;[J)J
                //    42: pop2           
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    47: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    50: aconst_null    
                //    51: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    54: aload_1        
                //    55: monitorexit    
                //    56: return         
                //    57: astore_2       
                //    58: aload_0        
                //    59: aload_0        
                //    60: new             Lcom/google/android/gms/common/api/Status;
                //    63: dup            
                //    64: sipush          2100
                //    67: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    70: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$2.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    73: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$2.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    76: aload_0        
                //    77: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    80: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    83: aconst_null    
                //    84: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    87: goto            54
                //    90: astore_2       
                //    91: aload_1        
                //    92: monitorexit    
                //    93: aload_2        
                //    94: athrow         
                //    95: astore_2       
                //    96: aload_0        
                //    97: getfield        com/google/android/gms/cast/RemoteMediaPlayer$2.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   100: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   103: aconst_null    
                //   104: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   107: aload_2        
                //   108: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     90     95     Any
                //  24     43     57     90     Ljava/io/IOException;
                //  24     43     95     109    Any
                //  43     54     90     95     Any
                //  54     56     90     95     Any
                //  58     76     95     109    Any
                //  76     87     90     95     Any
                //  91     93     90     95     Any
                //  96     109    90     95     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0054:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public void setOnMetadataUpdatedListener(final OnMetadataUpdatedListener zzSx) {
        this.zzSx = zzSx;
    }
    
    public void setOnPreloadStatusUpdatedListener(final OnPreloadStatusUpdatedListener zzSv) {
        this.zzSv = zzSv;
    }
    
    public void setOnQueueStatusUpdatedListener(final OnQueueStatusUpdatedListener zzSw) {
        this.zzSw = zzSw;
    }
    
    public void setOnStatusUpdatedListener(final OnStatusUpdatedListener zzSy) {
        this.zzSy = zzSy;
    }
    
    public PendingResult<MediaChannelResult> setStreamMute(final GoogleApiClient googleApiClient, final boolean b) {
        return this.setStreamMute(googleApiClient, b, null);
    }
    
    public PendingResult<MediaChannelResult> setStreamMute(final GoogleApiClient googleApiClient, final boolean b, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSV:Z
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSG:Lorg/json/JSONObject;
                //    43: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;ZLorg/json/JSONObject;)J
                //    46: pop2           
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    51: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    54: aconst_null    
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_1        
                //    59: monitorexit    
                //    60: return         
                //    61: astore_2       
                //    62: aload_0        
                //    63: aload_0        
                //    64: new             Lcom/google/android/gms/common/api/Status;
                //    67: dup            
                //    68: sipush          2100
                //    71: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    74: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$21.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    77: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$21.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    80: aload_0        
                //    81: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    84: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    87: aconst_null    
                //    88: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    91: goto            58
                //    94: astore_2       
                //    95: aload_1        
                //    96: monitorexit    
                //    97: aload_2        
                //    98: athrow         
                //    99: astore_2       
                //   100: aload_0        
                //   101: getfield        com/google/android/gms/cast/RemoteMediaPlayer$21.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   104: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   107: aconst_null    
                //   108: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   111: aload_2        
                //   112: athrow         
                //   113: astore_2       
                //   114: goto            62
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                             
                //  -----  -----  -----  -----  ---------------------------------
                //  10     24     94     99     Any
                //  24     47     61     62     Ljava/lang/IllegalStateException;
                //  24     47     113    117    Ljava/io/IOException;
                //  24     47     99     113    Any
                //  47     58     94     99     Any
                //  58     60     94     99     Any
                //  62     80     99     113    Any
                //  80     91     94     99     Any
                //  95     97     94     99     Any
                //  100    113    94     99     Any
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> setStreamVolume(final GoogleApiClient googleApiClient, final double n) throws IllegalArgumentException {
        return this.setStreamVolume(googleApiClient, n, null);
    }
    
    public PendingResult<MediaChannelResult> setStreamVolume(final GoogleApiClient googleApiClient, final double n, final JSONObject jsonObject) throws IllegalArgumentException {
        if (Double.isInfinite(n) || Double.isNaN(n)) {
            throw new IllegalArgumentException("Volume cannot be " + n);
        }
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSU:D
                //    39: aload_0        
                //    40: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSG:Lorg/json/JSONObject;
                //    43: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;DLorg/json/JSONObject;)J
                //    46: pop2           
                //    47: aload_0        
                //    48: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    51: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    54: aconst_null    
                //    55: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    58: aload_1        
                //    59: monitorexit    
                //    60: return         
                //    61: astore_2       
                //    62: aload_0        
                //    63: aload_0        
                //    64: new             Lcom/google/android/gms/common/api/Status;
                //    67: dup            
                //    68: sipush          2100
                //    71: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    74: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$20.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    77: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$20.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    80: aload_0        
                //    81: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    84: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    87: aconst_null    
                //    88: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    91: goto            58
                //    94: astore_2       
                //    95: aload_1        
                //    96: monitorexit    
                //    97: aload_2        
                //    98: athrow         
                //    99: astore_2       
                //   100: aload_0        
                //   101: getfield        com/google/android/gms/cast/RemoteMediaPlayer$20.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   104: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   107: aconst_null    
                //   108: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   111: aload_2        
                //   112: athrow         
                //   113: astore_2       
                //   114: goto            62
                //   117: astore_2       
                //   118: goto            62
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                                
                //  -----  -----  -----  -----  ------------------------------------
                //  10     24     94     99     Any
                //  24     47     61     62     Ljava/lang/IllegalStateException;
                //  24     47     117    121    Ljava/lang/IllegalArgumentException;
                //  24     47     113    117    Ljava/io/IOException;
                //  24     47     99     113    Any
                //  47     58     94     99     Any
                //  58     60     94     99     Any
                //  62     80     99     113    Any
                //  80     91     94     99     Any
                //  95     97     94     99     Any
                //  100    113    94     99     Any
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> setTextTrackStyle(final GoogleApiClient googleApiClient, final TextTrackStyle textTrackStyle) {
        if (textTrackStyle == null) {
            throw new IllegalArgumentException("trackStyle cannot be null");
        }
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSC:Lcom/google/android/gms/cast/TextTrackStyle;
                //    39: invokevirtual   com/google/android/gms/cast/internal/zzm.zza:(Lcom/google/android/gms/cast/internal/zzo;Lcom/google/android/gms/cast/TextTrackStyle;)J
                //    42: pop2           
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    47: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    50: aconst_null    
                //    51: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    54: aload_1        
                //    55: monitorexit    
                //    56: return         
                //    57: astore_2       
                //    58: aload_0        
                //    59: aload_0        
                //    60: new             Lcom/google/android/gms/common/api/Status;
                //    63: dup            
                //    64: sipush          2100
                //    67: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    70: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$3.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    73: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$3.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    76: aload_0        
                //    77: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    80: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    83: aconst_null    
                //    84: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    87: goto            54
                //    90: astore_2       
                //    91: aload_1        
                //    92: monitorexit    
                //    93: aload_2        
                //    94: athrow         
                //    95: astore_2       
                //    96: aload_0        
                //    97: getfield        com/google/android/gms/cast/RemoteMediaPlayer$3.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   100: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   103: aconst_null    
                //   104: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   107: aload_2        
                //   108: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     90     95     Any
                //  24     43     57     90     Ljava/io/IOException;
                //  24     43     95     109    Any
                //  43     54     90     95     Any
                //  54     56     90     95     Any
                //  58     76     95     109    Any
                //  76     87     90     95     Any
                //  91     93     90     95     Any
                //  96     109    90     95     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0054:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public PendingResult<MediaChannelResult> stop(final GoogleApiClient googleApiClient) {
        return this.stop(googleApiClient, null);
    }
    
    public PendingResult<MediaChannelResult> stop(final GoogleApiClient googleApiClient, final JSONObject jsonObject) {
        return googleApiClient.zzb((PendingResult<MediaChannelResult>)new zzb(googleApiClient) {
            protected void zza(final com.google.android.gms.cast.internal.zze p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //     4: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zze:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    14: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    17: aload_0        
                //    18: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSA:Lcom/google/android/gms/common/api/GoogleApiClient;
                //    21: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    24: aload_0        
                //    25: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    28: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzg:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/internal/zzm;
                //    31: aload_0        
                //    32: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzTa:Lcom/google/android/gms/cast/internal/zzo;
                //    35: aload_0        
                //    36: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSG:Lorg/json/JSONObject;
                //    39: invokevirtual   com/google/android/gms/cast/internal/zzm.zzb:(Lcom/google/android/gms/cast/internal/zzo;Lorg/json/JSONObject;)J
                //    42: pop2           
                //    43: aload_0        
                //    44: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    47: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    50: aconst_null    
                //    51: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    54: aload_1        
                //    55: monitorexit    
                //    56: return         
                //    57: astore_2       
                //    58: aload_0        
                //    59: aload_0        
                //    60: new             Lcom/google/android/gms/common/api/Status;
                //    63: dup            
                //    64: sipush          2100
                //    67: invokespecial   com/google/android/gms/common/api/Status.<init>:(I)V
                //    70: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$17.zzn:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/cast/RemoteMediaPlayer$MediaChannelResult;
                //    73: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$17.setResult:(Lcom/google/android/gms/common/api/Result;)V
                //    76: aload_0        
                //    77: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //    80: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //    83: aconst_null    
                //    84: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //    87: goto            54
                //    90: astore_2       
                //    91: aload_1        
                //    92: monitorexit    
                //    93: aload_2        
                //    94: athrow         
                //    95: astore_2       
                //    96: aload_0        
                //    97: getfield        com/google/android/gms/cast/RemoteMediaPlayer$17.zzSz:Lcom/google/android/gms/cast/RemoteMediaPlayer;
                //   100: invokestatic    com/google/android/gms/cast/RemoteMediaPlayer.zzf:(Lcom/google/android/gms/cast/RemoteMediaPlayer;)Lcom/google/android/gms/cast/RemoteMediaPlayer$zza;
                //   103: aconst_null    
                //   104: invokevirtual   com/google/android/gms/cast/RemoteMediaPlayer$zza.zzb:(Lcom/google/android/gms/common/api/GoogleApiClient;)V
                //   107: aload_2        
                //   108: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     24     90     95     Any
                //  24     43     57     90     Ljava/io/IOException;
                //  24     43     95     109    Any
                //  43     54     90     95     Any
                //  54     56     90     95     Any
                //  58     76     95     109    Any
                //  76     87     90     95     Any
                //  91     93     90     95     Any
                //  96     109    90     95     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IllegalStateException: Expression is linked from several locations: Label_0054:
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        });
    }
    
    public interface MediaChannelResult extends Result
    {
    }
    
    public interface OnMetadataUpdatedListener
    {
        void onMetadataUpdated();
    }
    
    public interface OnPreloadStatusUpdatedListener
    {
        void onPreloadStatusUpdated();
    }
    
    public interface OnQueueStatusUpdatedListener
    {
        void onQueueStatusUpdated();
    }
    
    public interface OnStatusUpdatedListener
    {
        void onStatusUpdated();
    }
    
    private class zza implements zzn
    {
        private GoogleApiClient zzSW;
        private long zzSX;
        
        public zza() {
            this.zzSX = 0L;
        }
        
        @Override
        public void zza(final String s, final String s2, final long n, final String s3) throws IOException {
            if (this.zzSW == null) {
                throw new IOException("No GoogleApiClient available");
            }
            Cast.CastApi.sendMessage(this.zzSW, s, s2).setResultCallback(new RemoteMediaPlayer.zza.zza(n));
        }
        
        public void zzb(final GoogleApiClient zzSW) {
            this.zzSW = zzSW;
        }
        
        @Override
        public long zzlu() {
            return ++this.zzSX;
        }
        
        private final class zza implements ResultCallback<Status>
        {
            private final long zzSY;
            
            zza(final long zzSY) {
                this.zzSY = zzSY;
            }
            
            public void zzm(final Status status) {
                if (!status.isSuccess()) {
                    RemoteMediaPlayer.this.zzSt.zzb(this.zzSY, status.getStatusCode());
                }
            }
        }
    }
    
    private abstract static class zzb extends com.google.android.gms.cast.internal.zzb<MediaChannelResult>
    {
        zzo zzTa;
        
        zzb(final GoogleApiClient googleApiClient) {
            super(googleApiClient);
            this.zzTa = new zzo() {
                @Override
                public void zza(final long n, final int n2, final Object o) {
                    JSONObject jsonObject;
                    if (o instanceof JSONObject) {
                        jsonObject = (JSONObject)o;
                    }
                    else {
                        jsonObject = null;
                    }
                    zzb.this.setResult((R)new RemoteMediaPlayer.zzc(new Status(n2), jsonObject));
                }
                
                @Override
                public void zzy(final long n) {
                    zzb.this.setResult((R)zzb.this.zzn(new Status(2103)));
                }
            };
        }
        
        public MediaChannelResult zzn(final Status status) {
            return new MediaChannelResult() {
                @Override
                public Status getStatus() {
                    return status;
                }
            };
        }
    }
    
    private static final class zzc implements MediaChannelResult
    {
        private final Status zzOt;
        private final JSONObject zzRJ;
        
        zzc(final Status zzOt, final JSONObject zzRJ) {
            this.zzOt = zzOt;
            this.zzRJ = zzRJ;
        }
        
        @Override
        public Status getStatus() {
            return this.zzOt;
        }
    }
}
