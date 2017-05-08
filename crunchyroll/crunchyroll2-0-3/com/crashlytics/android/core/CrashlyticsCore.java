// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.concurrent.CountDownLatch;
import android.annotation.SuppressLint;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import com.crashlytics.android.core.internal.models.SessionEventData;
import java.util.Collections;
import java.util.Map;
import io.fabric.sdk.android.services.settings.SettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import android.content.pm.PackageInfo;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.common.Crash;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.services.common.CommonUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.content.Context;
import java.util.concurrent.Future;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.Callable;
import io.fabric.sdk.android.services.concurrency.Task;
import io.fabric.sdk.android.services.concurrency.Priority;
import io.fabric.sdk.android.services.concurrency.PriorityCallable;
import io.fabric.sdk.android.services.settings.PromptSettingsData;
import android.app.Activity;
import java.util.concurrent.ExecutorService;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import java.io.File;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.concurrent.ConcurrentHashMap;
import com.crashlytics.android.core.internal.CrashEventDataProvider;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.Kit;

@DependsOn({ CrashEventDataProvider.class })
public class CrashlyticsCore extends Kit<Void>
{
    private final ConcurrentHashMap<String, String> attributes;
    private String buildId;
    private float delay;
    private boolean disabled;
    private CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private CrashEventDataProvider externalCrashEventDataProvider;
    private CrashlyticsUncaughtExceptionHandler handler;
    private HttpRequestFactory httpRequestFactory;
    private File initializationMarkerFile;
    private String installerPackageName;
    private CrashlyticsListener listener;
    private String packageName;
    private final PinningInfoProvider pinningInfo;
    private File sdkDir;
    private final long startTime;
    private String userEmail;
    private String userId;
    private String userName;
    private String versionCode;
    private String versionName;
    
    public CrashlyticsCore() {
        this(1.0f, null, null, false);
    }
    
    CrashlyticsCore(final float n, final CrashlyticsListener crashlyticsListener, final PinningInfoProvider pinningInfoProvider, final boolean b) {
        this(n, crashlyticsListener, pinningInfoProvider, b, ExecutorUtils.buildSingleThreadExecutorService("Crashlytics Exception Handler"));
    }
    
    CrashlyticsCore(final float delay, final CrashlyticsListener listener, final PinningInfoProvider pinningInfo, final boolean disabled, final ExecutorService executorService) {
        this.userId = null;
        this.userEmail = null;
        this.userName = null;
        this.attributes = new ConcurrentHashMap<String, String>();
        this.startTime = System.currentTimeMillis();
        this.delay = delay;
        this.listener = listener;
        this.pinningInfo = pinningInfo;
        this.disabled = disabled;
        this.executorServiceWrapper = new CrashlyticsExecutorServiceWrapper(executorService);
    }
    
    private int dipsToPixels(final float n, final int n2) {
        return (int)(n2 * n);
    }
    
    private void finishInitSynchronously() {
        final PriorityCallable<Void> priorityCallable = new PriorityCallable<Void>() {
            @Override
            public Void call() throws Exception {
                return CrashlyticsCore.this.doInBackground();
            }
            
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        final Iterator<Task> iterator = this.getDependencies().iterator();
        while (iterator.hasNext()) {
            priorityCallable.addDependency((Task)iterator.next());
        }
        final Future<Object> submit = this.getFabric().getExecutorService().submit((Callable<Object>)priorityCallable);
        Fabric.getLogger().d("CrashlyticsCore", "Crashlytics detected incomplete initialization on previous app launch. Will initialize synchronously.");
        try {
            submit.get(4L, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            Fabric.getLogger().e("CrashlyticsCore", "Crashlytics was interrupted during initialization.", ex);
        }
        catch (ExecutionException ex2) {
            Fabric.getLogger().e("CrashlyticsCore", "Problem encountered during Crashlytics initialization.", ex2);
        }
        catch (TimeoutException ex3) {
            Fabric.getLogger().e("CrashlyticsCore", "Crashlytics timed out during initialization.", ex3);
        }
    }
    
    public static CrashlyticsCore getInstance() {
        return Fabric.getKit(CrashlyticsCore.class);
    }
    
    private boolean getSendDecisionFromUser(final Activity activity, final PromptSettingsData promptSettingsData) {
        final DialogStringResolver dialogStringResolver = new DialogStringResolver((Context)activity, promptSettingsData);
        final OptInLatch optInLatch = new OptInLatch();
        activity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
                final DialogInterface$OnClickListener dialogInterface$OnClickListener = (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        optInLatch.setOptIn(true);
                        dialogInterface.dismiss();
                    }
                };
                final float density = activity.getResources().getDisplayMetrics().density;
                final int access$300 = CrashlyticsCore.this.dipsToPixels(density, 5);
                final TextView textView = new TextView((Context)activity);
                textView.setAutoLinkMask(15);
                textView.setText((CharSequence)dialogStringResolver.getMessage());
                textView.setTextAppearance((Context)activity, 16973892);
                textView.setPadding(access$300, access$300, access$300, access$300);
                textView.setFocusable(false);
                final ScrollView view = new ScrollView((Context)activity);
                view.setPadding(CrashlyticsCore.this.dipsToPixels(density, 14), CrashlyticsCore.this.dipsToPixels(density, 2), CrashlyticsCore.this.dipsToPixels(density, 10), CrashlyticsCore.this.dipsToPixels(density, 12));
                view.addView((View)textView);
                alertDialog$Builder.setView((View)view).setTitle((CharSequence)dialogStringResolver.getTitle()).setCancelable(false).setNeutralButton((CharSequence)dialogStringResolver.getSendButtonTitle(), (DialogInterface$OnClickListener)dialogInterface$OnClickListener);
                if (promptSettingsData.showCancelButton) {
                    alertDialog$Builder.setNegativeButton((CharSequence)dialogStringResolver.getCancelButtonTitle(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            optInLatch.setOptIn(false);
                            dialogInterface.dismiss();
                        }
                    });
                }
                if (promptSettingsData.showAlwaysSendButton) {
                    alertDialog$Builder.setPositiveButton((CharSequence)dialogStringResolver.getAlwaysSendButtonTitle(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            CrashlyticsCore.this.setShouldSendUserReportsWithoutPrompting(true);
                            optInLatch.setOptIn(true);
                            dialogInterface.dismiss();
                        }
                    });
                }
                alertDialog$Builder.show();
            }
        });
        Fabric.getLogger().d("CrashlyticsCore", "Waiting for user opt-in.");
        optInLatch.await();
        return optInLatch.getOptIn();
    }
    
    private boolean isRequiringBuildId(final Context context) {
        return CommonUtils.getBooleanResourceValue(context, "com.crashlytics.RequireBuildId", true);
    }
    
    static void recordFatalExceptionEvent(final String s) {
        final Answers answers = Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new Crash.FatalException(s));
        }
    }
    
    private static String sanitizeAttribute(String s) {
        String s2 = s;
        if (s != null) {
            s = (s2 = s.trim());
            if (s.length() > 1024) {
                s2 = s.substring(0, 1024);
            }
        }
        return s2;
    }
    
    private void setAndValidateKitProperties(final Context context, final String s) {
        Label_0173: {
            if (this.pinningInfo == null) {
                break Label_0173;
            }
            CrashlyticsPinningInfoProvider pinningInfoProvider = new CrashlyticsPinningInfoProvider(this.pinningInfo);
            while (true) {
                (this.httpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger())).setPinningInfoProvider(pinningInfoProvider);
                while (true) {
                    try {
                        this.packageName = context.getPackageName();
                        this.installerPackageName = this.getIdManager().getInstallerPackageName();
                        Fabric.getLogger().d("CrashlyticsCore", "Installer package name is: " + this.installerPackageName);
                        final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(this.packageName, 0);
                        this.versionCode = Integer.toString(packageInfo.versionCode);
                        String versionName;
                        if (packageInfo.versionName == null) {
                            versionName = "0.0";
                        }
                        else {
                            versionName = packageInfo.versionName;
                        }
                        this.versionName = versionName;
                        this.buildId = CommonUtils.resolveBuildId(context);
                        this.getIdManager().getBluetoothMacAddress();
                        this.getBuildIdValidator(this.buildId, this.isRequiringBuildId(context)).validate(s, this.packageName);
                        return;
                        pinningInfoProvider = null;
                    }
                    catch (Exception ex) {
                        Fabric.getLogger().e("CrashlyticsCore", "Error setting up app properties", ex);
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    boolean canSendWithUserApproval() {
        return Settings.getInstance().withSettings((Settings.SettingsAccess<Boolean>)new Settings.SettingsAccess<Boolean>() {
            public Boolean usingSettings(final SettingsData settingsData) {
                final boolean b = true;
                final Activity currentActivity = CrashlyticsCore.this.getFabric().getCurrentActivity();
                boolean access$100 = b;
                if (currentActivity != null) {
                    access$100 = b;
                    if (!currentActivity.isFinishing()) {
                        access$100 = b;
                        if (CrashlyticsCore.this.shouldPromptUserBeforeSendingCrashReports()) {
                            access$100 = CrashlyticsCore.this.getSendDecisionFromUser(currentActivity, settingsData.promptData);
                        }
                    }
                }
                return access$100;
            }
        }, true);
    }
    
    boolean didPreviousInitializationComplete() {
        return this.executorServiceWrapper.executeSyncLoggingException((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return CrashlyticsCore.this.initializationMarkerFile.exists();
            }
        });
    }
    
    @Override
    protected Void doInBackground() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.markInitializationStarted:()V
        //     4: aload_0        
        //     5: getfield        com/crashlytics/android/core/CrashlyticsCore.handler:Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler;
        //     8: invokevirtual   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.cleanInvalidTempFiles:()V
        //    11: iconst_1       
        //    12: istore_3       
        //    13: iconst_1       
        //    14: istore_2       
        //    15: iload_3        
        //    16: istore_1       
        //    17: invokestatic    io/fabric/sdk/android/services/settings/Settings.getInstance:()Lio/fabric/sdk/android/services/settings/Settings;
        //    20: invokevirtual   io/fabric/sdk/android/services/settings/Settings.awaitSettingsData:()Lio/fabric/sdk/android/services/settings/SettingsData;
        //    23: astore          4
        //    25: aload           4
        //    27: ifnonnull       51
        //    30: iload_3        
        //    31: istore_1       
        //    32: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //    35: ldc             "CrashlyticsCore"
        //    37: ldc_w           "Received null settings, skipping initialization!"
        //    40: invokeinterface io/fabric/sdk/android/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //    45: aload_0        
        //    46: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.markInitializationComplete:()V
        //    49: aconst_null    
        //    50: areturn        
        //    51: iload_3        
        //    52: istore_1       
        //    53: aload           4
        //    55: getfield        io/fabric/sdk/android/services/settings/SettingsData.featuresData:Lio/fabric/sdk/android/services/settings/FeaturesSettingsData;
        //    58: getfield        io/fabric/sdk/android/services/settings/FeaturesSettingsData.collectReports:Z
        //    61: ifeq            111
        //    64: iconst_0       
        //    65: istore_3       
        //    66: iconst_0       
        //    67: istore_2       
        //    68: iload_3        
        //    69: istore_1       
        //    70: aload_0        
        //    71: getfield        com/crashlytics/android/core/CrashlyticsCore.handler:Lcom/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler;
        //    74: invokevirtual   com/crashlytics/android/core/CrashlyticsUncaughtExceptionHandler.finalizeSessions:()Z
        //    77: pop            
        //    78: iload_3        
        //    79: istore_1       
        //    80: aload_0        
        //    81: aload           4
        //    83: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.getCreateReportSpiCall:(Lio/fabric/sdk/android/services/settings/SettingsData;)Lcom/crashlytics/android/core/CreateReportSpiCall;
        //    86: astore          4
        //    88: aload           4
        //    90: ifnull          134
        //    93: iload_3        
        //    94: istore_1       
        //    95: new             Lcom/crashlytics/android/core/ReportUploader;
        //    98: dup            
        //    99: aload           4
        //   101: invokespecial   com/crashlytics/android/core/ReportUploader.<init>:(Lcom/crashlytics/android/core/CreateReportSpiCall;)V
        //   104: aload_0        
        //   105: getfield        com/crashlytics/android/core/CrashlyticsCore.delay:F
        //   108: invokevirtual   com/crashlytics/android/core/ReportUploader.uploadReports:(F)V
        //   111: iload_2        
        //   112: ifeq            128
        //   115: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   118: ldc             "CrashlyticsCore"
        //   120: ldc_w           "Crash reporting disabled."
        //   123: invokeinterface io/fabric/sdk/android/Logger.d:(Ljava/lang/String;Ljava/lang/String;)V
        //   128: aload_0        
        //   129: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.markInitializationComplete:()V
        //   132: aconst_null    
        //   133: areturn        
        //   134: iload_3        
        //   135: istore_1       
        //   136: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   139: ldc             "CrashlyticsCore"
        //   141: ldc_w           "Unable to create a call to upload reports."
        //   144: invokeinterface io/fabric/sdk/android/Logger.w:(Ljava/lang/String;Ljava/lang/String;)V
        //   149: goto            111
        //   152: astore          4
        //   154: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   157: ldc             "CrashlyticsCore"
        //   159: ldc_w           "Error dealing with settings"
        //   162: aload           4
        //   164: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   169: iload_1        
        //   170: istore_2       
        //   171: goto            111
        //   174: astore          4
        //   176: invokestatic    io/fabric/sdk/android/Fabric.getLogger:()Lio/fabric/sdk/android/Logger;
        //   179: ldc             "CrashlyticsCore"
        //   181: ldc             "Problem encountered during Crashlytics initialization."
        //   183: aload           4
        //   185: invokeinterface io/fabric/sdk/android/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   190: aload_0        
        //   191: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.markInitializationComplete:()V
        //   194: aconst_null    
        //   195: areturn        
        //   196: astore          4
        //   198: aload_0        
        //   199: invokevirtual   com/crashlytics/android/core/CrashlyticsCore.markInitializationComplete:()V
        //   202: aload           4
        //   204: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  17     25     152    174    Ljava/lang/Exception;
        //  17     25     196    205    Any
        //  32     45     152    174    Ljava/lang/Exception;
        //  32     45     196    205    Any
        //  53     64     152    174    Ljava/lang/Exception;
        //  53     64     196    205    Any
        //  70     78     152    174    Ljava/lang/Exception;
        //  70     78     196    205    Any
        //  80     88     152    174    Ljava/lang/Exception;
        //  80     88     196    205    Any
        //  95     111    152    174    Ljava/lang/Exception;
        //  95     111    196    205    Any
        //  115    128    174    196    Ljava/lang/Exception;
        //  115    128    196    205    Any
        //  136    149    152    174    Ljava/lang/Exception;
        //  136    149    196    205    Any
        //  154    169    174    196    Ljava/lang/Exception;
        //  154    169    196    205    Any
        //  176    190    196    205    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0128:
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
    
    Map<String, String> getAttributes() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.attributes);
    }
    
    BuildIdValidator getBuildIdValidator(final String s, final boolean b) {
        return new BuildIdValidator(s, b);
    }
    
    CreateReportSpiCall getCreateReportSpiCall(final SettingsData settingsData) {
        if (settingsData != null) {
            return new DefaultCreateReportSpiCall(this, this.getOverridenSpiEndpoint(), settingsData.appData.reportsUrl, this.httpRequestFactory);
        }
        return null;
    }
    
    SessionEventData getExternalCrashEventData() {
        SessionEventData crashEventData = null;
        if (this.externalCrashEventDataProvider != null) {
            crashEventData = this.externalCrashEventDataProvider.getCrashEventData();
        }
        return crashEventData;
    }
    
    CrashlyticsUncaughtExceptionHandler getHandler() {
        return this.handler;
    }
    
    @Override
    public String getIdentifier() {
        return "com.crashlytics.sdk.android.crashlytics-core";
    }
    
    String getInstallerPackageName() {
        return this.installerPackageName;
    }
    
    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(this.getContext(), "com.crashlytics.ApiEndpoint");
    }
    
    String getPackageName() {
        return this.packageName;
    }
    
    File getSdkDirectory() {
        if (this.sdkDir == null) {
            this.sdkDir = new FileStoreImpl(this).getFilesDir();
        }
        return this.sdkDir;
    }
    
    SessionSettingsData getSessionSettingsData() {
        final SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
        if (awaitSettingsData == null) {
            return null;
        }
        return awaitSettingsData.sessionData;
    }
    
    String getUserEmail() {
        if (this.getIdManager().canCollectUserIds()) {
            return this.userEmail;
        }
        return null;
    }
    
    String getUserIdentifier() {
        if (this.getIdManager().canCollectUserIds()) {
            return this.userId;
        }
        return null;
    }
    
    String getUserName() {
        if (this.getIdManager().canCollectUserIds()) {
            return this.userName;
        }
        return null;
    }
    
    @Override
    public String getVersion() {
        return "2.3.5.79";
    }
    
    String getVersionCode() {
        return this.versionCode;
    }
    
    String getVersionName() {
        return this.versionName;
    }
    
    void markInitializationComplete() {
        this.executorServiceWrapper.executeAsync((Callable<Object>)new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    final boolean delete = CrashlyticsCore.this.initializationMarkerFile.delete();
                    Fabric.getLogger().d("CrashlyticsCore", "Initialization marker file removed: " + delete);
                    return delete;
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("CrashlyticsCore", "Problem encountered deleting Crashlytics initialization marker.", ex);
                    return false;
                }
            }
        });
    }
    
    void markInitializationStarted() {
        this.executorServiceWrapper.executeSyncLoggingException((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                CrashlyticsCore.this.initializationMarkerFile.createNewFile();
                Fabric.getLogger().d("CrashlyticsCore", "Initialization marker file created.");
                return null;
            }
        });
    }
    
    @Override
    protected boolean onPreExecute() {
        return this.onPreExecute(super.getContext());
    }
    
    boolean onPreExecute(final Context context) {
        if (this.disabled) {
            return false;
        }
        final String value = new ApiKey().getValue(context);
        if (value == null) {
            return false;
        }
        Fabric.getLogger().i("CrashlyticsCore", "Initializing Crashlytics " + this.getVersion());
        this.initializationMarkerFile = new File(this.getSdkDirectory(), "initialization_marker");
        final boolean b = false;
        try {
            this.setAndValidateKitProperties(context, value);
            boolean didPreviousInitializationComplete = b;
            try {
                final SessionDataWriter sessionDataWriter = new SessionDataWriter(this.getContext(), this.buildId, this.getPackageName());
                didPreviousInitializationComplete = b;
                Fabric.getLogger().d("CrashlyticsCore", "Installing exception handler...");
                didPreviousInitializationComplete = b;
                this.handler = new CrashlyticsUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this.listener, this.executorServiceWrapper, this.getIdManager(), sessionDataWriter, this);
                didPreviousInitializationComplete = b;
                final boolean b2 = didPreviousInitializationComplete = this.didPreviousInitializationComplete();
                this.handler.openSession();
                didPreviousInitializationComplete = b2;
                Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)this.handler);
                didPreviousInitializationComplete = b2;
                Fabric.getLogger().d("CrashlyticsCore", "Successfully installed exception handler.");
                didPreviousInitializationComplete = b2;
                if (didPreviousInitializationComplete && CommonUtils.canTryConnection(context)) {
                    this.finishInitSynchronously();
                    return false;
                }
                goto Label_0247;
            }
            catch (Exception ex) {
                Fabric.getLogger().e("CrashlyticsCore", "There was a problem installing the exception handler.", ex);
            }
        }
        catch (CrashlyticsMissingDependencyException ex2) {
            throw new UnmetDependencyException(ex2);
        }
        catch (Exception ex3) {
            Fabric.getLogger().e("CrashlyticsCore", "Crashlytics was not started due to an exception during initialization", ex3);
            return false;
        }
    }
    
    @SuppressLint({ "CommitPrefEdits" })
    void setShouldSendUserReportsWithoutPrompting(final boolean b) {
        final PreferenceStoreImpl preferenceStoreImpl = new PreferenceStoreImpl(this);
        preferenceStoreImpl.save(preferenceStoreImpl.edit().putBoolean("always_send_reports_opt_in", b));
    }
    
    public void setString(String sanitizeAttribute, final String s) {
        if (this.disabled) {
            return;
        }
        if (sanitizeAttribute == null) {
            if (this.getContext() != null && CommonUtils.isAppDebuggable(this.getContext())) {
                throw new IllegalArgumentException("Custom attribute key must not be null.");
            }
            Fabric.getLogger().e("CrashlyticsCore", "Attempting to set custom attribute with null key, ignoring.", null);
        }
        else {
            final String sanitizeAttribute2 = sanitizeAttribute(sanitizeAttribute);
            if (this.attributes.size() < 64 || this.attributes.containsKey(sanitizeAttribute2)) {
                if (s == null) {
                    sanitizeAttribute = "";
                }
                else {
                    sanitizeAttribute = sanitizeAttribute(s);
                }
                this.attributes.put(sanitizeAttribute2, sanitizeAttribute);
                this.handler.cacheKeyData(this.attributes);
                return;
            }
            Fabric.getLogger().d("CrashlyticsCore", "Exceeded maximum number of custom attributes (64)");
        }
    }
    
    boolean shouldPromptUserBeforeSendingCrashReports() {
        return Settings.getInstance().withSettings((Settings.SettingsAccess<Boolean>)new Settings.SettingsAccess<Boolean>() {
            public Boolean usingSettings(final SettingsData settingsData) {
                boolean b = false;
                if (settingsData.featuresData.promptEnabled) {
                    if (!CrashlyticsCore.this.shouldSendReportsWithoutPrompting()) {
                        b = true;
                    }
                    return b;
                }
                return false;
            }
        }, false);
    }
    
    boolean shouldSendReportsWithoutPrompting() {
        return new PreferenceStoreImpl(this).get().getBoolean("always_send_reports_opt_in", false);
    }
    
    private class OptInLatch
    {
        private final CountDownLatch latch;
        private boolean send;
        
        private OptInLatch() {
            this.send = false;
            this.latch = new CountDownLatch(1);
        }
        
        void await() {
            try {
                this.latch.await();
            }
            catch (InterruptedException ex) {}
        }
        
        boolean getOptIn() {
            return this.send;
        }
        
        void setOptIn(final boolean send) {
            this.send = send;
            this.latch.countDown();
        }
    }
}
