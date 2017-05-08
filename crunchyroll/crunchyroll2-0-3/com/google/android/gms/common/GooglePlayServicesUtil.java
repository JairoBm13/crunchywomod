// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import android.os.Build;
import java.util.Iterator;
import android.content.pm.PackageInstaller$SessionInfo;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.os.Bundle;
import com.google.android.gms.common.internal.zzm;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.Notification;
import android.os.Build$VERSION;
import android.app.Notification$Style;
import android.app.Notification$BigTextStyle;
import android.app.Notification$Builder;
import com.google.android.gms.common.internal.zzu;
import com.google.android.gms.R;
import android.content.DialogInterface$OnClickListener;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzf;
import android.app.AlertDialog$Builder;
import android.util.TypedValue;
import android.util.Log;
import com.google.android.gms.internal.zzkz;
import com.google.android.gms.internal.zzlk;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.content.res.Resources;
import android.content.pm.PackageManager$NameNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import android.net.Uri;
import android.net.Uri$Builder;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.DialogInterface$OnCancelListener;
import android.app.Dialog;
import android.app.Activity;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GooglePlayServicesUtil
{
    public static final String GMS_ERROR_DIALOG = "GooglePlayServicesErrorDialog";
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    public static boolean zzVS;
    public static boolean zzVT;
    private static int zzVU;
    private static String zzVV;
    private static Integer zzVW;
    static final AtomicBoolean zzVX;
    private static final Object zzoW;
    
    static {
        GOOGLE_PLAY_SERVICES_VERSION_CODE = zzml();
        GooglePlayServicesUtil.zzVS = false;
        GooglePlayServicesUtil.zzVT = false;
        GooglePlayServicesUtil.zzVU = -1;
        zzoW = new Object();
        GooglePlayServicesUtil.zzVV = null;
        GooglePlayServicesUtil.zzVW = null;
        zzVX = new AtomicBoolean();
    }
    
    @Deprecated
    public static Dialog getErrorDialog(final int n, final Activity activity, final int n2) {
        return getErrorDialog(n, activity, n2, null);
    }
    
    @Deprecated
    public static Dialog getErrorDialog(final int n, final Activity activity, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return zza(n, activity, null, n2, dialogInterface$OnCancelListener);
    }
    
    @Deprecated
    public static PendingIntent getErrorPendingIntent(final int n, final Context context, final int n2) {
        final Intent zzaT = zzaT(n);
        if (zzaT == null) {
            return null;
        }
        return PendingIntent.getActivity(context, n2, zzaT, 268435456);
    }
    
    @Deprecated
    public static String getErrorString(final int n) {
        return ConnectionResult.getStatusString(n);
    }
    
    @Deprecated
    public static String getOpenSourceSoftwareLicenseInfo(final Context context) {
        Object o = new Uri$Builder().scheme("android.resource").authority("com.google.android.gms").appendPath("raw").appendPath("oss_notice").build();
        try {
            final InputStream openInputStream = context.getContentResolver().openInputStream((Uri)o);
            try {
                return (String)(o = new Scanner(openInputStream).useDelimiter("\\A").next());
            }
            catch (NoSuchElementException ex) {}
            finally {
                if (openInputStream != null) {
                    openInputStream.close();
                }
            }
        }
        catch (Exception ex2) {
            o = null;
        }
        return (String)o;
    }
    
    public static Context getRemoteContext(Context packageContext) {
        try {
            packageContext = packageContext.createPackageContext("com.google.android.gms", 3);
            return packageContext;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    public static Resources getRemoteResource(final Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        }
        catch (PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    @Deprecated
    public static int isGooglePlayServicesAvailable(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: getstatic       com/google/android/gms/common/internal/zzd.zzZR:Z
        //     3: ifeq            8
        //     6: iconst_0       
        //     7: ireturn        
        //     8: aload_0        
        //     9: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    12: astore_3       
        //    13: aload_0        
        //    14: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //    17: getstatic       com/google/android/gms/R$string.common_google_play_services_unknown_issue:I
        //    20: invokevirtual   android/content/res/Resources.getString:(I)Ljava/lang/String;
        //    23: pop            
        //    24: ldc             "com.google.android.gms"
        //    26: aload_0        
        //    27: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //    30: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    33: ifne            40
        //    36: aload_0        
        //    37: invokestatic    com/google/android/gms/common/GooglePlayServicesUtil.zzaa:(Landroid/content/Context;)V
        //    40: aload_3        
        //    41: ldc             "com.google.android.gms"
        //    43: bipush          64
        //    45: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //    48: astore_2       
        //    49: invokestatic    com/google/android/gms/common/zzd.zzmn:()Lcom/google/android/gms/common/zzd;
        //    52: astore          4
        //    54: aload_2        
        //    55: getfield        android/content/pm/PackageInfo.versionCode:I
        //    58: invokestatic    com/google/android/gms/internal/zzkz.zzbP:(I)Z
        //    61: ifne            71
        //    64: aload_0        
        //    65: invokestatic    com/google/android/gms/internal/zzkz.zzai:(Landroid/content/Context;)Z
        //    68: ifeq            118
        //    71: aload           4
        //    73: aload_2        
        //    74: getstatic       com/google/android/gms/common/zzc$zzbk.zzVR:[Lcom/google/android/gms/common/zzc$zza;
        //    77: invokevirtual   com/google/android/gms/common/zzd.zza:(Landroid/content/pm/PackageInfo;[Lcom/google/android/gms/common/zzc$zza;)Lcom/google/android/gms/common/zzc$zza;
        //    80: ifnonnull       237
        //    83: ldc             "GooglePlayServicesUtil"
        //    85: ldc             "Google Play services signature invalid."
        //    87: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //    90: pop            
        //    91: bipush          9
        //    93: ireturn        
        //    94: astore_2       
        //    95: ldc             "GooglePlayServicesUtil"
        //    97: ldc             "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included."
        //    99: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   102: pop            
        //   103: goto            24
        //   106: astore_0       
        //   107: ldc             "GooglePlayServicesUtil"
        //   109: ldc_w           "Google Play services is missing."
        //   112: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   115: pop            
        //   116: iconst_1       
        //   117: ireturn        
        //   118: aload           4
        //   120: aload_3        
        //   121: ldc             "com.android.vending"
        //   123: bipush          64
        //   125: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //   128: getstatic       com/google/android/gms/common/zzc$zzbk.zzVR:[Lcom/google/android/gms/common/zzc$zza;
        //   131: invokevirtual   com/google/android/gms/common/zzd.zza:(Landroid/content/pm/PackageInfo;[Lcom/google/android/gms/common/zzc$zza;)Lcom/google/android/gms/common/zzc$zza;
        //   134: astore          5
        //   136: aload           5
        //   138: ifnonnull       196
        //   141: ldc             "GooglePlayServicesUtil"
        //   143: ldc_w           "Google Play Store signature invalid."
        //   146: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   149: pop            
        //   150: bipush          9
        //   152: ireturn        
        //   153: astore          5
        //   155: aload_0        
        //   156: ldc             "com.android.vending"
        //   158: invokestatic    com/google/android/gms/common/GooglePlayServicesUtil.zzh:(Landroid/content/Context;Ljava/lang/String;)Z
        //   161: ifeq            225
        //   164: ldc             "GooglePlayServicesUtil"
        //   166: ldc_w           "Google Play Store is updating."
        //   169: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   172: pop            
        //   173: aload           4
        //   175: aload_2        
        //   176: getstatic       com/google/android/gms/common/zzc$zzbk.zzVR:[Lcom/google/android/gms/common/zzc$zza;
        //   179: invokevirtual   com/google/android/gms/common/zzd.zza:(Landroid/content/pm/PackageInfo;[Lcom/google/android/gms/common/zzc$zza;)Lcom/google/android/gms/common/zzc$zza;
        //   182: ifnonnull       237
        //   185: ldc             "GooglePlayServicesUtil"
        //   187: ldc             "Google Play services signature invalid."
        //   189: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   192: pop            
        //   193: bipush          9
        //   195: ireturn        
        //   196: aload           4
        //   198: aload_2        
        //   199: iconst_1       
        //   200: anewarray       Lcom/google/android/gms/common/zzc$zza;
        //   203: dup            
        //   204: iconst_0       
        //   205: aload           5
        //   207: aastore        
        //   208: invokevirtual   com/google/android/gms/common/zzd.zza:(Landroid/content/pm/PackageInfo;[Lcom/google/android/gms/common/zzc$zza;)Lcom/google/android/gms/common/zzc$zza;
        //   211: ifnonnull       237
        //   214: ldc             "GooglePlayServicesUtil"
        //   216: ldc             "Google Play services signature invalid."
        //   218: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   221: pop            
        //   222: bipush          9
        //   224: ireturn        
        //   225: ldc             "GooglePlayServicesUtil"
        //   227: ldc_w           "Google Play Store is neither installed nor updating."
        //   230: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   233: pop            
        //   234: bipush          9
        //   236: ireturn        
        //   237: getstatic       com/google/android/gms/common/GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE:I
        //   240: invokestatic    com/google/android/gms/internal/zzkz.zzbN:(I)I
        //   243: istore_1       
        //   244: aload_2        
        //   245: getfield        android/content/pm/PackageInfo.versionCode:I
        //   248: invokestatic    com/google/android/gms/internal/zzkz.zzbN:(I)I
        //   251: iload_1        
        //   252: if_icmpge       298
        //   255: ldc             "GooglePlayServicesUtil"
        //   257: new             Ljava/lang/StringBuilder;
        //   260: dup            
        //   261: invokespecial   java/lang/StringBuilder.<init>:()V
        //   264: ldc_w           "Google Play services out of date.  Requires "
        //   267: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   270: getstatic       com/google/android/gms/common/GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE:I
        //   273: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   276: ldc_w           " but found "
        //   279: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   282: aload_2        
        //   283: getfield        android/content/pm/PackageInfo.versionCode:I
        //   286: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   289: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   292: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   295: pop            
        //   296: iconst_2       
        //   297: ireturn        
        //   298: aload_2        
        //   299: getfield        android/content/pm/PackageInfo.applicationInfo:Landroid/content/pm/ApplicationInfo;
        //   302: astore_2       
        //   303: aload_2        
        //   304: astore_0       
        //   305: aload_2        
        //   306: ifnonnull       317
        //   309: aload_3        
        //   310: ldc             "com.google.android.gms"
        //   312: iconst_0       
        //   313: invokevirtual   android/content/pm/PackageManager.getApplicationInfo:(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;
        //   316: astore_0       
        //   317: aload_0        
        //   318: getfield        android/content/pm/ApplicationInfo.enabled:Z
        //   321: ifne            342
        //   324: iconst_3       
        //   325: ireturn        
        //   326: astore_0       
        //   327: ldc             "GooglePlayServicesUtil"
        //   329: ldc_w           "Google Play services missing when getting application info."
        //   332: invokestatic    android/util/Log.wtf:(Ljava/lang/String;Ljava/lang/String;)I
        //   335: pop            
        //   336: aload_0        
        //   337: invokevirtual   android/content/pm/PackageManager$NameNotFoundException.printStackTrace:()V
        //   340: iconst_1       
        //   341: ireturn        
        //   342: iconst_0       
        //   343: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  13     24     94     106    Ljava/lang/Throwable;
        //  40     49     106    118    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  118    136    153    196    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  141    150    153    196    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  196    222    153    196    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  309    317    326    342    Landroid/content/pm/PackageManager$NameNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0040:
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
    
    @Deprecated
    public static boolean isUserRecoverableError(final int n) {
        switch (n) {
            default: {
                return false;
            }
            case 1:
            case 2:
            case 3:
            case 9: {
                return true;
            }
        }
    }
    
    @Deprecated
    public static boolean showErrorDialogFragment(final int n, final Activity activity, final int n2) {
        return showErrorDialogFragment(n, activity, n2, null);
    }
    
    @Deprecated
    public static boolean showErrorDialogFragment(final int n, final Activity activity, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return showErrorDialogFragment(n, activity, null, n2, dialogInterface$OnCancelListener);
    }
    
    public static boolean showErrorDialogFragment(final int n, Activity activity, Fragment zza, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        int n3 = 0;
        zza = (Fragment)zza(n, activity, zza, n2, dialogInterface$OnCancelListener);
        if (zza == null) {
            return false;
        }
        while (true) {
            try {
                n3 = ((activity instanceof FragmentActivity) ? 1 : 0);
                if (n3 != 0) {
                    activity = (Activity)((FragmentActivity)activity).getSupportFragmentManager();
                    SupportErrorDialogFragment.newInstance((Dialog)zza, dialogInterface$OnCancelListener).show((FragmentManager)activity, "GooglePlayServicesErrorDialog");
                }
                else {
                    if (!zzlk.zzoR()) {
                        throw new RuntimeException("This Activity does not support Fragments.");
                    }
                    activity = (Activity)activity.getFragmentManager();
                    ErrorDialogFragment.newInstance((Dialog)zza, dialogInterface$OnCancelListener).show((android.app.FragmentManager)activity, "GooglePlayServicesErrorDialog");
                }
                return true;
            }
            catch (NoClassDefFoundError noClassDefFoundError) {
                continue;
            }
            break;
        }
    }
    
    @Deprecated
    public static void showErrorNotification(final int n, final Context context) {
        int n2 = n;
        if (zzkz.zzai(context) && (n2 = n) == 2) {
            n2 = 42;
        }
        if (zze(context, n2) || zzf(context, n2)) {
            zzab(context);
            return;
        }
        zza(n2, context);
    }
    
    @Deprecated
    public static void zzY(final Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        final int googlePlayServicesAvailable = isGooglePlayServicesAvailable(context);
        if (googlePlayServicesAvailable == 0) {
            return;
        }
        final Intent zzaT = zzaT(googlePlayServicesAvailable);
        Log.e("GooglePlayServicesUtil", "GooglePlayServices not available due to error " + googlePlayServicesAvailable);
        if (zzaT == null) {
            throw new GooglePlayServicesNotAvailableException(googlePlayServicesAvailable);
        }
        throw new GooglePlayServicesRepairableException(googlePlayServicesAvailable, "Google Play Services not available", zzaT);
    }
    
    private static Dialog zza(final int n, final Activity activity, final Fragment fragment, final int n2, final DialogInterface$OnCancelListener onCancelListener) {
        final AlertDialog$Builder alertDialog$Builder = null;
        if (n == 0) {
            return null;
        }
        int n3 = n;
        if (zzkz.zzai((Context)activity) && (n3 = n) == 2) {
            n3 = 42;
        }
        AlertDialog$Builder alertDialog$Builder2 = alertDialog$Builder;
        if (zzlk.zzoU()) {
            final TypedValue typedValue = new TypedValue();
            activity.getTheme().resolveAttribute(16843529, typedValue, true);
            alertDialog$Builder2 = alertDialog$Builder;
            if ("Theme.Dialog.Alert".equals(activity.getResources().getResourceEntryName(typedValue.resourceId))) {
                alertDialog$Builder2 = new AlertDialog$Builder((Context)activity, 5);
            }
        }
        AlertDialog$Builder alertDialog$Builder3;
        if ((alertDialog$Builder3 = alertDialog$Builder2) == null) {
            alertDialog$Builder3 = new AlertDialog$Builder((Context)activity);
        }
        alertDialog$Builder3.setMessage((CharSequence)zzf.zzb((Context)activity, n3, zzad((Context)activity)));
        if (onCancelListener != null) {
            alertDialog$Builder3.setOnCancelListener(onCancelListener);
        }
        final Intent zzaT = zzaT(n3);
        zzg zzg;
        if (fragment == null) {
            zzg = new zzg(activity, zzaT, n2);
        }
        else {
            zzg = new zzg(fragment, zzaT, n2);
        }
        final String zzh = zzf.zzh((Context)activity, n3);
        if (zzh != null) {
            alertDialog$Builder3.setPositiveButton((CharSequence)zzh, (DialogInterface$OnClickListener)zzg);
        }
        final String zzg2 = zzf.zzg((Context)activity, n3);
        if (zzg2 != null) {
            alertDialog$Builder3.setTitle((CharSequence)zzg2);
        }
        return (Dialog)alertDialog$Builder3.create();
    }
    
    private static void zza(final int n, final Context context) {
        zza(n, context, null);
    }
    
    private static void zza(int n, final Context context, final String s) {
        final Resources resources = context.getResources();
        final String zzad = zzad(context);
        String contentTitle;
        if ((contentTitle = zzf.zzi(context, n)) == null) {
            contentTitle = resources.getString(R.string.common_google_play_services_notification_ticker);
        }
        final String zzc = zzf.zzc(context, n, zzad);
        final PendingIntent errorPendingIntent = getErrorPendingIntent(n, context, 0);
        Notification notification;
        if (zzkz.zzai(context)) {
            zzu.zzU(zzlk.zzoV());
            notification = new Notification$Builder(context).setSmallIcon(R.drawable.common_ic_googleplayservices).setPriority(2).setAutoCancel(true).setStyle((Notification$Style)new Notification$BigTextStyle().bigText((CharSequence)(contentTitle + " " + zzc))).addAction(R.drawable.common_full_open_on_phone, (CharSequence)resources.getString(R.string.common_open_on_phone), errorPendingIntent).build();
        }
        else {
            final String string = resources.getString(R.string.common_google_play_services_notification_ticker);
            if (zzlk.zzoR()) {
                final Notification$Builder setAutoCancel = new Notification$Builder(context).setSmallIcon(17301642).setContentTitle((CharSequence)contentTitle).setContentText((CharSequence)zzc).setContentIntent(errorPendingIntent).setTicker((CharSequence)string).setAutoCancel(true);
                if (zzlk.zzoY()) {
                    setAutoCancel.setLocalOnly(true);
                }
                if (zzlk.zzoV()) {
                    setAutoCancel.setStyle((Notification$Style)new Notification$BigTextStyle().bigText((CharSequence)zzc));
                    notification = setAutoCancel.build();
                }
                else {
                    notification = setAutoCancel.getNotification();
                }
                if (Build$VERSION.SDK_INT == 19) {
                    notification.extras.putBoolean("android.support.localOnly", true);
                }
            }
            else {
                final Notification notification2 = new Notification(17301642, (CharSequence)string, System.currentTimeMillis());
                notification2.flags |= 0x10;
                notification2.setLatestEventInfo(context, (CharSequence)contentTitle, (CharSequence)zzc, errorPendingIntent);
                notification = notification2;
            }
        }
        if (zzaU(n)) {
            GooglePlayServicesUtil.zzVX.set(false);
            n = 10436;
        }
        else {
            n = 39789;
        }
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (s != null) {
            notificationManager.notify(s, n, notification);
            return;
        }
        notificationManager.notify(n, notification);
    }
    
    public static boolean zza(final Context context, int n, final String s) {
        final boolean b = false;
        Label_0030: {
            if (!zzlk.zzoX()) {
                break Label_0030;
            }
            final AppOpsManager appOpsManager = (AppOpsManager)context.getSystemService("appops");
            try {
                appOpsManager.checkPackage(n, s);
                boolean b2 = true;
                Label_0028: {
                    return b2;
                }
                final String[] packagesForUid = context.getPackageManager().getPackagesForUid(n);
                b2 = b;
                // iftrue(Label_0028:, s == null)
                // iftrue(Label_0028:, n >= packagesForUid.length)
                // iftrue(Label_0076:, !s.equals((Object)packagesForUid[n]))
                while (true) {
                    Block_4: {
                        break Block_4;
                        n = 0;
                        while (true) {
                            b2 = b;
                            return true;
                            Label_0076:
                            ++n;
                            continue;
                        }
                    }
                    b2 = b;
                    continue;
                }
            }
            // iftrue(Label_0028:, packagesForUid == null)
            catch (SecurityException ex) {
                return false;
            }
        }
    }
    
    @Deprecated
    public static Intent zzaT(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1:
            case 2: {
                return zzm.zzcg("com.google.android.gms");
            }
            case 42: {
                return zzm.zznX();
            }
            case 3: {
                return zzm.zzce("com.google.android.gms");
            }
        }
    }
    
    private static boolean zzaU(final int n) {
        switch (n) {
            default: {
                return false;
            }
            case 1:
            case 2:
            case 3:
            case 18:
            case 42: {
                return true;
            }
        }
    }
    
    private static void zzaa(final Context context) {
        while (true) {
            final Context context2;
        Label_0163:
            while (true) {
                synchronized (GooglePlayServicesUtil.zzoW) {
                    if (GooglePlayServicesUtil.zzVV == null) {
                        GooglePlayServicesUtil.zzVV = context.getPackageName();
                        try {
                            final Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                            if (metaData != null) {
                                GooglePlayServicesUtil.zzVW = metaData.getInt("com.google.android.gms.version");
                            }
                            else {
                                GooglePlayServicesUtil.zzVW = null;
                            }
                            final Integer zzVW = GooglePlayServicesUtil.zzVW;
                            // monitorexit(GooglePlayServicesUtil.zzoW)
                            if (zzVW == null) {
                                throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
                            }
                            break Label_0163;
                        }
                        catch (PackageManager$NameNotFoundException ex) {
                            Log.wtf("GooglePlayServicesUtil", "This should never happen.", (Throwable)ex);
                            continue;
                        }
                        continue;
                    }
                }
                if (!GooglePlayServicesUtil.zzVV.equals(context2.getPackageName())) {
                    throw new IllegalArgumentException("isGooglePlayServicesAvailable should only be called with Context from your application's package. A previous call used package '" + GooglePlayServicesUtil.zzVV + "' and this call used package '" + context2.getPackageName() + "'.");
                }
                continue;
            }
            if ((int)context2 != GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE) {
                throw new IllegalStateException("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected " + GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE + " but" + " found " + context2 + ".  You must have the" + " following declaration within the <application> element: " + "    <meta-data android:name=\"" + "com.google.android.gms.version" + "\" android:value=\"@integer/google_play_services_version\" />");
            }
        }
    }
    
    private static void zzab(final Context context) {
        final zza zza = new zza(context);
        zza.sendMessageDelayed(zza.obtainMessage(1), 120000L);
    }
    
    @Deprecated
    public static void zzac(final Context context) {
        if (GooglePlayServicesUtil.zzVX.getAndSet(true)) {
            return;
        }
        try {
            ((NotificationManager)context.getSystemService("notification")).cancel(10436);
        }
        catch (SecurityException ex) {}
    }
    
    public static String zzad(final Context context) {
        String s;
        Object packageManager = s = context.getApplicationInfo().name;
        if (!TextUtils.isEmpty((CharSequence)packageManager)) {
            return s;
        }
        s = context.getPackageName();
        packageManager = context.getApplicationContext().getPackageManager();
        while (true) {
            try {
                final ApplicationInfo applicationInfo = ((PackageManager)packageManager).getApplicationInfo(context.getPackageName(), 0);
                if (applicationInfo != null) {
                    s = ((PackageManager)packageManager).getApplicationLabel(applicationInfo).toString();
                }
                return s;
            }
            catch (PackageManager$NameNotFoundException ex) {
                final ApplicationInfo applicationInfo = null;
                continue;
            }
            break;
        }
    }
    
    public static boolean zzae(final Context context) {
        final PackageManager packageManager = context.getPackageManager();
        return zzlk.zzoZ() && packageManager.hasSystemFeature("com.google.sidewinder");
    }
    
    public static boolean zzb(final PackageManager packageManager) {
        synchronized (GooglePlayServicesUtil.zzoW) {
        Label_0050:
            while (true) {
                if (GooglePlayServicesUtil.zzVU != -1) {
                    break Label_0050;
                }
                while (true) {
                    try {
                        if (zzd.zzmn().zza(packageManager.getPackageInfo("com.google.android.gms", 64), zzc.zzVK[1]) != null) {
                            GooglePlayServicesUtil.zzVU = 1;
                        }
                        else {
                            GooglePlayServicesUtil.zzVU = 0;
                        }
                        // monitorexit(GooglePlayServicesUtil.zzoW)
                        if (GooglePlayServicesUtil.zzVU != 0) {
                            return true;
                        }
                        break;
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        GooglePlayServicesUtil.zzVU = 0;
                        continue Label_0050;
                    }
                    continue Label_0050;
                }
                break;
            }
        }
        return false;
    }
    
    @Deprecated
    public static boolean zzb(final PackageManager packageManager, final String s) {
        return zzd.zzmn().zzb(packageManager, s);
    }
    
    public static boolean zzc(final PackageManager packageManager) {
        return zzb(packageManager) || !zzmm();
    }
    
    public static boolean zzd(final Context context, final int n) {
        return zza(context, n, "com.google.android.gms") && zzb(context.getPackageManager(), "com.google.android.gms");
    }
    
    @Deprecated
    public static boolean zze(final Context context, final int n) {
        return n == 18 || (n == 1 && zzh(context, "com.google.android.gms"));
    }
    
    @Deprecated
    public static boolean zzf(final Context context, final int n) {
        return n == 9 && zzh(context, "com.android.vending");
    }
    
    public static boolean zzh(final Context context, final String s) {
        if (zzlk.zzoZ()) {
            final Iterator<PackageInstaller$SessionInfo> iterator = context.getPackageManager().getPackageInstaller().getAllSessions().iterator();
            while (iterator.hasNext()) {
                if (s.equals(iterator.next().getAppPackageName())) {
                    return true;
                }
            }
        }
        else {
            final PackageManager packageManager = context.getPackageManager();
            try {
                if (packageManager.getApplicationInfo(s, 8192).enabled) {
                    return true;
                }
            }
            catch (PackageManager$NameNotFoundException ex) {}
        }
        return false;
    }
    
    private static int zzml() {
        return 7571000;
    }
    
    public static boolean zzmm() {
        if (GooglePlayServicesUtil.zzVS) {
            return GooglePlayServicesUtil.zzVT;
        }
        return "user".equals(Build.TYPE);
    }
    
    private static class zza extends Handler
    {
        private final Context zzqw;
        
        zza(final Context context) {
            Looper looper;
            if (Looper.myLooper() == null) {
                looper = Looper.getMainLooper();
            }
            else {
                looper = Looper.myLooper();
            }
            super(looper);
            this.zzqw = context.getApplicationContext();
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    Log.w("GooglePlayServicesUtil", "Don't know how to handle this message: " + message.what);
                    break;
                }
                case 1: {
                    final int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.zzqw);
                    if (GooglePlayServicesUtil.isUserRecoverableError(googlePlayServicesAvailable)) {
                        zza(googlePlayServicesAvailable, this.zzqw);
                        return;
                    }
                    break;
                }
            }
        }
    }
}
