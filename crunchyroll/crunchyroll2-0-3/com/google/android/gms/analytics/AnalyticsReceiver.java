// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics;

import android.content.Intent;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.common.internal.zzu;
import android.content.Context;
import android.os.PowerManager$WakeLock;
import android.content.BroadcastReceiver;

public final class AnalyticsReceiver extends BroadcastReceiver
{
    static PowerManager$WakeLock zzIc;
    static Boolean zzId;
    static Object zzoW;
    
    static {
        AnalyticsReceiver.zzoW = new Object();
    }
    
    public static boolean zzT(final Context context) {
        zzu.zzu(context);
        if (AnalyticsReceiver.zzId != null) {
            return AnalyticsReceiver.zzId;
        }
        final boolean zza = zzam.zza(context, AnalyticsReceiver.class, false);
        AnalyticsReceiver.zzId = zza;
        return zza;
    }
    
    public void onReceive(final Context p0, final Intent p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokestatic    com/google/android/gms/analytics/internal/zzf.zzV:(Landroid/content/Context;)Lcom/google/android/gms/analytics/internal/zzf;
        //     4: astore          5
        //     6: aload           5
        //     8: invokevirtual   com/google/android/gms/analytics/internal/zzf.zzhQ:()Lcom/google/android/gms/analytics/internal/zzaf;
        //    11: astore          4
        //    13: aload_2        
        //    14: invokevirtual   android/content/Intent.getAction:()Ljava/lang/String;
        //    17: astore_2       
        //    18: aload           5
        //    20: invokevirtual   com/google/android/gms/analytics/internal/zzf.zzhR:()Lcom/google/android/gms/analytics/internal/zzr;
        //    23: invokevirtual   com/google/android/gms/analytics/internal/zzr.zziW:()Z
        //    26: ifeq            91
        //    29: aload           4
        //    31: ldc             "Device AnalyticsReceiver got"
        //    33: aload_2        
        //    34: invokevirtual   com/google/android/gms/analytics/internal/zzaf.zza:(Ljava/lang/String;Ljava/lang/Object;)V
        //    37: ldc             "com.google.android.gms.analytics.ANALYTICS_DISPATCH"
        //    39: aload_2        
        //    40: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    43: ifeq            90
        //    46: aload_1        
        //    47: invokestatic    com/google/android/gms/analytics/AnalyticsService.zzU:(Landroid/content/Context;)Z
        //    50: istore_3       
        //    51: new             Landroid/content/Intent;
        //    54: dup            
        //    55: aload_1        
        //    56: ldc             Lcom/google/android/gms/analytics/AnalyticsService;.class
        //    58: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //    61: astore          5
        //    63: aload           5
        //    65: ldc             "com.google.android.gms.analytics.ANALYTICS_DISPATCH"
        //    67: invokevirtual   android/content/Intent.setAction:(Ljava/lang/String;)Landroid/content/Intent;
        //    70: pop            
        //    71: getstatic       com/google/android/gms/analytics/AnalyticsReceiver.zzoW:Ljava/lang/Object;
        //    74: astore_2       
        //    75: aload_2        
        //    76: monitorenter   
        //    77: aload_1        
        //    78: aload           5
        //    80: invokevirtual   android/content/Context.startService:(Landroid/content/Intent;)Landroid/content/ComponentName;
        //    83: pop            
        //    84: iload_3        
        //    85: ifne            102
        //    88: aload_2        
        //    89: monitorexit    
        //    90: return         
        //    91: aload           4
        //    93: ldc             "Local AnalyticsReceiver got"
        //    95: aload_2        
        //    96: invokevirtual   com/google/android/gms/analytics/internal/zzaf.zza:(Ljava/lang/String;Ljava/lang/Object;)V
        //    99: goto            37
        //   102: aload_1        
        //   103: ldc             "power"
        //   105: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   108: checkcast       Landroid/os/PowerManager;
        //   111: astore_1       
        //   112: getstatic       com/google/android/gms/analytics/AnalyticsReceiver.zzIc:Landroid/os/PowerManager$WakeLock;
        //   115: ifnonnull       135
        //   118: aload_1        
        //   119: iconst_1       
        //   120: ldc             "Analytics WakeLock"
        //   122: invokevirtual   android/os/PowerManager.newWakeLock:(ILjava/lang/String;)Landroid/os/PowerManager$WakeLock;
        //   125: putstatic       com/google/android/gms/analytics/AnalyticsReceiver.zzIc:Landroid/os/PowerManager$WakeLock;
        //   128: getstatic       com/google/android/gms/analytics/AnalyticsReceiver.zzIc:Landroid/os/PowerManager$WakeLock;
        //   131: iconst_0       
        //   132: invokevirtual   android/os/PowerManager$WakeLock.setReferenceCounted:(Z)V
        //   135: getstatic       com/google/android/gms/analytics/AnalyticsReceiver.zzIc:Landroid/os/PowerManager$WakeLock;
        //   138: ldc2_w          1000
        //   141: invokevirtual   android/os/PowerManager$WakeLock.acquire:(J)V
        //   144: aload_2        
        //   145: monitorexit    
        //   146: return         
        //   147: astore_1       
        //   148: aload_2        
        //   149: monitorexit    
        //   150: aload_1        
        //   151: athrow         
        //   152: astore_1       
        //   153: aload           4
        //   155: ldc             "Analytics service at risk of not starting. For more reliable analytics, add the WAKE_LOCK permission to your manifest. See http://goo.gl/8Rd3yj for instructions."
        //   157: invokevirtual   com/google/android/gms/analytics/internal/zzaf.zzaW:(Ljava/lang/String;)V
        //   160: goto            144
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                         
        //  -----  -----  -----  -----  -----------------------------
        //  77     84     147    152    Any
        //  88     90     147    152    Any
        //  102    135    152    163    Ljava/lang/SecurityException;
        //  102    135    147    152    Any
        //  135    144    152    163    Ljava/lang/SecurityException;
        //  135    144    147    152    Any
        //  144    146    147    152    Any
        //  148    150    147    152    Any
        //  153    160    147    152    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0102:
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
}
