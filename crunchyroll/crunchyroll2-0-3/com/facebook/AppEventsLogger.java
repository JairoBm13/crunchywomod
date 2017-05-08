// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Currency;
import java.math.BigDecimal;
import android.content.Intent;
import android.content.ComponentName;
import bolts.AppLinks;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONArray;
import com.facebook.internal.AttributionIdentifiers;
import java.util.UUID;
import android.os.Bundle;
import com.facebook.model.GraphObject;
import com.facebook.internal.Logger;
import java.util.ArrayList;
import java.util.Set;
import android.util.Log;
import android.app.Activity;
import java.util.List;
import java.util.Iterator;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import android.content.Context;

public class AppEventsLogger
{
    public static final String ACTION_APP_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_FLUSHED";
    public static final String APP_EVENTS_EXTRA_FLUSH_RESULT = "com.facebook.sdk.APP_EVENTS_FLUSH_RESULT";
    public static final String APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED";
    static final String APP_EVENT_PREFERENCES = "com.facebook.sdk.appEventPreferences";
    private static final int APP_SUPPORTS_ATTRIBUTION_ID_RECHECK_PERIOD_IN_SECONDS = 86400;
    private static final int FLUSH_APP_SESSION_INFO_IN_SECONDS = 30;
    private static final int FLUSH_PERIOD_IN_SECONDS = 15;
    private static final int NUM_LOG_EVENTS_TO_TRY_TO_FLUSH_AFTER = 100;
    private static final String SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT = "_fbSourceApplicationHasBeenSet";
    private static final String TAG;
    private static String anonymousAppDeviceGUID;
    private static Context applicationContext;
    private static ScheduledThreadPoolExecutor backgroundExecutor;
    private static FlushBehavior flushBehavior;
    private static boolean isOpenedByApplink;
    private static boolean requestInFlight;
    private static String sourceApplication;
    private static Map<AccessTokenAppIdPair, SessionEventsState> stateMap;
    private static Object staticLock;
    private final AccessTokenAppIdPair accessTokenAppId;
    private final Context context;
    
    static {
        TAG = AppEventsLogger.class.getCanonicalName();
        AppEventsLogger.stateMap = new ConcurrentHashMap<AccessTokenAppIdPair, SessionEventsState>();
        AppEventsLogger.flushBehavior = FlushBehavior.AUTO;
        AppEventsLogger.staticLock = new Object();
    }
    
    private AppEventsLogger(final Context context, String staticLock, final Session session) {
        Validate.notNull(context, "context");
        this.context = context;
        Session activeSession = session;
        if (session == null) {
            activeSession = Session.getActiveSession();
        }
        Label_0086: {
            if (activeSession == null || (staticLock != null && !staticLock.equals(activeSession.getApplicationId()))) {
                break Label_0086;
            }
            this.accessTokenAppId = new AccessTokenAppIdPair(activeSession);
        Label_0097_Outer:
            while (true) {
                staticLock = (String)AppEventsLogger.staticLock;
                synchronized (staticLock) {
                    if (AppEventsLogger.applicationContext == null) {
                        AppEventsLogger.applicationContext = context.getApplicationContext();
                    }
                    initializeTimersIfNeeded();
                    return;
                    while (true) {
                        this.accessTokenAppId = new AccessTokenAppIdPair(null, staticLock);
                        continue Label_0097_Outer;
                        Utility.getMetadataApplicationId(context);
                        continue;
                    }
                }
                // iftrue(Label_0097:, s = staticLock != null)
            }
        }
    }
    
    private static int accumulatePersistedEvents() {
        final PersistedEvents andClearStore = PersistedEvents.readAndClearStore(AppEventsLogger.applicationContext);
        int n = 0;
        for (final AccessTokenAppIdPair accessTokenAppIdPair : andClearStore.keySet()) {
            final SessionEventsState sessionEventsState = getSessionEventsState(AppEventsLogger.applicationContext, accessTokenAppIdPair);
            final List<AppEvent> events = andClearStore.getEvents(accessTokenAppIdPair);
            sessionEventsState.accumulatePersistedEvents(events);
            n += events.size();
        }
        return n;
    }
    
    public static void activateApp(final Context context) {
        Settings.sdkInitialize(context);
        activateApp(context, Utility.getMetadataApplicationId(context));
    }
    
    public static void activateApp(final Context context, String sourceApplication) {
        if (context == null || sourceApplication == null) {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        }
        if (context instanceof Activity) {
            setSourceApplication((Activity)context);
        }
        else {
            resetSourceApplication();
            Log.d(AppEventsLogger.class.getName(), "To set source application the context of activateApp must be an instance of Activity");
        }
        Settings.publishInstallAsync(context, sourceApplication, null);
        final AppEventsLogger appEventsLogger = new AppEventsLogger(context, sourceApplication, null);
        final long currentTimeMillis = System.currentTimeMillis();
        sourceApplication = getSourceApplication();
        AppEventsLogger.backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appEventsLogger.logAppSessionResumeEvent(currentTimeMillis, sourceApplication);
            }
        });
    }
    
    private static FlushStatistics buildAndExecuteRequests(final FlushReason flushReason, final Set<AccessTokenAppIdPair> set) {
        final FlushStatistics flushStatistics = new FlushStatistics();
        final boolean limitEventAndDataUsage = Settings.getLimitEventAndDataUsage(AppEventsLogger.applicationContext);
        final ArrayList<Request> list = new ArrayList<Request>();
        for (final AccessTokenAppIdPair accessTokenAppIdPair : set) {
            final SessionEventsState sessionEventsState = getSessionEventsState(accessTokenAppIdPair);
            if (sessionEventsState != null) {
                final Request buildRequestForSession = buildRequestForSession(accessTokenAppIdPair, sessionEventsState, limitEventAndDataUsage, flushStatistics);
                if (buildRequestForSession == null) {
                    continue;
                }
                list.add(buildRequestForSession);
            }
        }
        FlushStatistics flushStatistics2;
        if (list.size() > 0) {
            Logger.log(LoggingBehavior.APP_EVENTS, AppEventsLogger.TAG, "Flushing %d events due to %s.", flushStatistics.numEvents, flushReason.toString());
            final Iterator<Object> iterator2 = list.iterator();
            while (true) {
                flushStatistics2 = flushStatistics;
                if (!iterator2.hasNext()) {
                    break;
                }
                iterator2.next().executeAndWait();
            }
        }
        else {
            flushStatistics2 = null;
        }
        return flushStatistics2;
    }
    
    private static Request buildRequestForSession(final AccessTokenAppIdPair accessTokenAppIdPair, final SessionEventsState sessionEventsState, final boolean b, final FlushStatistics flushStatistics) {
        final String applicationId = accessTokenAppIdPair.getApplicationId();
        final Utility.FetchedAppSettings queryAppSettings = Utility.queryAppSettings(applicationId, false);
        final Request postRequest = Request.newPostRequest(null, String.format("%s/activities", applicationId), null, null);
        Bundle parameters;
        if ((parameters = postRequest.getParameters()) == null) {
            parameters = new Bundle();
        }
        parameters.putString("access_token", accessTokenAppIdPair.getAccessToken());
        postRequest.setParameters(parameters);
        if (queryAppSettings == null) {
            return null;
        }
        final int populateRequest = sessionEventsState.populateRequest(postRequest, queryAppSettings.supportsImplicitLogging(), b);
        if (populateRequest == 0) {
            return null;
        }
        flushStatistics.numEvents += populateRequest;
        postRequest.setCallback((Request.Callback)new Request.Callback() {
            @Override
            public void onCompleted(final Response response) {
                handleResponse(accessTokenAppIdPair, postRequest, response, sessionEventsState, flushStatistics);
            }
        });
        return postRequest;
    }
    
    public static void deactivateApp(final Context context) {
        deactivateApp(context, Utility.getMetadataApplicationId(context));
    }
    
    public static void deactivateApp(final Context context, final String s) {
        if (context == null || s == null) {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        }
        resetSourceApplication();
        AppEventsLogger.backgroundExecutor.execute(new Runnable() {
            final /* synthetic */ long val$eventTime = System.currentTimeMillis();
            final /* synthetic */ AppEventsLogger val$logger = new AppEventsLogger(context, s, null);
            
            @Override
            public void run() {
                this.val$logger.logAppSessionSuspendEvent(this.val$eventTime);
            }
        });
    }
    
    static void eagerFlush() {
        if (getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY) {
            flush(FlushReason.EAGER_FLUSHING_EVENT);
        }
    }
    
    private static void flush(final FlushReason flushReason) {
        Settings.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                flushAndWait(flushReason);
            }
        });
    }
    
    private static void flushAndWait(final FlushReason p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: getstatic       com/facebook/AppEventsLogger.staticLock:Ljava/lang/Object;
        //     3: astore_1       
        //     4: aload_1        
        //     5: monitorenter   
        //     6: getstatic       com/facebook/AppEventsLogger.requestInFlight:Z
        //     9: ifeq            15
        //    12: aload_1        
        //    13: monitorexit    
        //    14: return         
        //    15: iconst_1       
        //    16: putstatic       com/facebook/AppEventsLogger.requestInFlight:Z
        //    19: new             Ljava/util/HashSet;
        //    22: dup            
        //    23: getstatic       com/facebook/AppEventsLogger.stateMap:Ljava/util/Map;
        //    26: invokeinterface java/util/Map.keySet:()Ljava/util/Set;
        //    31: invokespecial   java/util/HashSet.<init>:(Ljava/util/Collection;)V
        //    34: astore_2       
        //    35: aload_1        
        //    36: monitorexit    
        //    37: invokestatic    com/facebook/AppEventsLogger.accumulatePersistedEvents:()I
        //    40: pop            
        //    41: aconst_null    
        //    42: astore_1       
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokestatic    com/facebook/AppEventsLogger.buildAndExecuteRequests:(Lcom/facebook/AppEventsLogger$FlushReason;Ljava/util/Set;)Lcom/facebook/AppEventsLogger$FlushStatistics;
        //    48: astore_0       
        //    49: getstatic       com/facebook/AppEventsLogger.staticLock:Ljava/lang/Object;
        //    52: astore_1       
        //    53: aload_1        
        //    54: monitorenter   
        //    55: iconst_0       
        //    56: putstatic       com/facebook/AppEventsLogger.requestInFlight:Z
        //    59: aload_1        
        //    60: monitorexit    
        //    61: aload_0        
        //    62: ifnull          135
        //    65: new             Landroid/content/Intent;
        //    68: dup            
        //    69: ldc             "com.facebook.sdk.APP_EVENTS_FLUSHED"
        //    71: invokespecial   android/content/Intent.<init>:(Ljava/lang/String;)V
        //    74: astore_1       
        //    75: aload_1        
        //    76: ldc             "com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED"
        //    78: aload_0        
        //    79: getfield        com/facebook/AppEventsLogger$FlushStatistics.numEvents:I
        //    82: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //    85: pop            
        //    86: aload_1        
        //    87: ldc             "com.facebook.sdk.APP_EVENTS_FLUSH_RESULT"
        //    89: aload_0        
        //    90: getfield        com/facebook/AppEventsLogger$FlushStatistics.result:Lcom/facebook/AppEventsLogger$FlushResult;
        //    93: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;Ljava/io/Serializable;)Landroid/content/Intent;
        //    96: pop            
        //    97: getstatic       com/facebook/AppEventsLogger.applicationContext:Landroid/content/Context;
        //   100: invokestatic    android/support/v4/content/LocalBroadcastManager.getInstance:(Landroid/content/Context;)Landroid/support/v4/content/LocalBroadcastManager;
        //   103: aload_1        
        //   104: invokevirtual   android/support/v4/content/LocalBroadcastManager.sendBroadcast:(Landroid/content/Intent;)Z
        //   107: pop            
        //   108: return         
        //   109: astore_0       
        //   110: aload_1        
        //   111: monitorexit    
        //   112: aload_0        
        //   113: athrow         
        //   114: astore_0       
        //   115: getstatic       com/facebook/AppEventsLogger.TAG:Ljava/lang/String;
        //   118: ldc_w           "Caught unexpected exception while flushing: "
        //   121: aload_0        
        //   122: invokestatic    com/facebook/internal/Utility.logd:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   125: aload_1        
        //   126: astore_0       
        //   127: goto            49
        //   130: astore_0       
        //   131: aload_1        
        //   132: monitorexit    
        //   133: aload_0        
        //   134: athrow         
        //   135: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      14     109    114    Any
        //  15     37     109    114    Any
        //  43     49     114    130    Ljava/lang/Exception;
        //  55     61     130    135    Any
        //  110    112    109    114    Any
        //  131    133    130    135    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 79, Size: 79
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
    
    private static void flushIfNecessary() {
        synchronized (AppEventsLogger.staticLock) {
            if (getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY && getAccumulatedEventCount() > 100) {
                flush(FlushReason.EVENT_THRESHOLD);
            }
        }
    }
    
    private static int getAccumulatedEventCount() {
        final Object staticLock = AppEventsLogger.staticLock;
        // monitorenter(staticLock)
        int n = 0;
        try {
            final Iterator<SessionEventsState> iterator = AppEventsLogger.stateMap.values().iterator();
            while (iterator.hasNext()) {
                n += iterator.next().getAccumulatedEventCount();
            }
            return n;
        }
        finally {
        }
        // monitorexit(staticLock)
    }
    
    static String getAnonymousAppDeviceGUID(final Context context) {
        Label_0101: {
            if (AppEventsLogger.anonymousAppDeviceGUID != null) {
                break Label_0101;
            }
            synchronized (AppEventsLogger.staticLock) {
                if (AppEventsLogger.anonymousAppDeviceGUID == null) {
                    AppEventsLogger.anonymousAppDeviceGUID = context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).getString("anonymousAppDeviceGUID", (String)null);
                    if (AppEventsLogger.anonymousAppDeviceGUID == null) {
                        AppEventsLogger.anonymousAppDeviceGUID = "XZ" + UUID.randomUUID().toString();
                        context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).edit().putString("anonymousAppDeviceGUID", AppEventsLogger.anonymousAppDeviceGUID).apply();
                    }
                }
                return AppEventsLogger.anonymousAppDeviceGUID;
            }
        }
    }
    
    public static FlushBehavior getFlushBehavior() {
        synchronized (AppEventsLogger.staticLock) {
            return AppEventsLogger.flushBehavior;
        }
    }
    
    @Deprecated
    public static boolean getLimitEventUsage(final Context context) {
        return Settings.getLimitEventAndDataUsage(context);
    }
    
    private static SessionEventsState getSessionEventsState(final Context context, final AccessTokenAppIdPair accessTokenAppIdPair) {
        final SessionEventsState sessionEventsState = AppEventsLogger.stateMap.get(accessTokenAppIdPair);
        AttributionIdentifiers attributionIdentifiers = null;
        if (sessionEventsState == null) {
            attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        }
        final Object staticLock = AppEventsLogger.staticLock;
        // monitorenter(staticLock)
        while (true) {
            try {
                Label_0082: {
                    SessionEventsState sessionEventsState2;
                    if ((sessionEventsState2 = AppEventsLogger.stateMap.get(accessTokenAppIdPair)) != null) {
                        break Label_0082;
                    }
                    sessionEventsState2 = new SessionEventsState(attributionIdentifiers, context.getPackageName(), getAnonymousAppDeviceGUID(context));
                    try {
                        AppEventsLogger.stateMap.put(accessTokenAppIdPair, sessionEventsState2);
                        // monitorexit(staticLock)
                        return sessionEventsState2;
                        // monitorexit(staticLock)
                        throw;
                    }
                    finally {}
                }
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    private static SessionEventsState getSessionEventsState(final AccessTokenAppIdPair accessTokenAppIdPair) {
        synchronized (AppEventsLogger.staticLock) {
            return AppEventsLogger.stateMap.get(accessTokenAppIdPair);
        }
    }
    
    static String getSourceApplication() {
        String s = "Unclassified";
        if (AppEventsLogger.isOpenedByApplink) {
            s = "Applink";
        }
        String string = s;
        if (AppEventsLogger.sourceApplication != null) {
            string = s + "(" + AppEventsLogger.sourceApplication + ")";
        }
        return string;
    }
    
    private static void handleResponse(final AccessTokenAppIdPair accessTokenAppIdPair, final Request request, final Response response, final SessionEventsState sessionEventsState, final FlushStatistics flushStatistics) {
        final FacebookRequestError error = response.getError();
        String format = "Success";
        FlushResult result = FlushResult.SUCCESS;
        while (true) {
            while (true) {
                Label_0040: {
                    if (error == null) {
                        break Label_0040;
                    }
                    if (error.getErrorCode() == -1) {
                        format = "Failed: No Connectivity";
                        result = FlushResult.NO_CONNECTIVITY;
                        break Label_0040;
                    }
                    Label_0162: {
                        break Label_0162;
                    Label_0113_Outer:
                        while (true) {
                            final String s = (String)request.getTag();
                            while (true) {
                            Label_0205:
                                while (true) {
                                    try {
                                        final String string = new JSONArray(s).toString(2);
                                        Logger.log(LoggingBehavior.APP_EVENTS, AppEventsLogger.TAG, "Flush completed\nParams: %s\n  Result: %s\n  Events JSON: %s", request.getGraphObject().toString(), format, string);
                                        if (error != null) {
                                            final boolean b = true;
                                            sessionEventsState.clearInFlightAndStats(b);
                                            if (result == FlushResult.NO_CONNECTIVITY) {
                                                PersistedEvents.persistEvents(AppEventsLogger.applicationContext, accessTokenAppIdPair, sessionEventsState);
                                            }
                                            if (result != FlushResult.SUCCESS && flushStatistics.result != FlushResult.NO_CONNECTIVITY) {
                                                flushStatistics.result = result;
                                            }
                                            return;
                                        }
                                        break Label_0205;
                                        format = String.format("Failed:\n  Response: %s\n  Error %s", response.toString(), error.toString());
                                        result = FlushResult.SERVER_ERROR;
                                        break;
                                    }
                                    catch (JSONException ex) {
                                        final String string = "<Can't encode events for debug logging>";
                                        continue Label_0113_Outer;
                                    }
                                    break;
                                }
                                final boolean b = false;
                                continue;
                            }
                        }
                    }
                }
                if (Settings.isLoggingBehaviorEnabled(LoggingBehavior.APP_EVENTS)) {
                    continue;
                }
                break;
            }
            continue;
        }
    }
    
    private static void initializeTimersIfNeeded() {
        Object staticLock = AppEventsLogger.staticLock;
        synchronized (staticLock) {
            if (AppEventsLogger.backgroundExecutor != null) {
                return;
            }
            AppEventsLogger.backgroundExecutor = new ScheduledThreadPoolExecutor(1);
            // monitorexit(staticLock)
            staticLock = new Runnable() {
                @Override
                public void run() {
                    if (AppEventsLogger.getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY) {
                        flushAndWait(FlushReason.TIMER);
                    }
                }
            };
            AppEventsLogger.backgroundExecutor.scheduleAtFixedRate((Runnable)staticLock, 0L, 15L, TimeUnit.SECONDS);
            staticLock = new Runnable() {
                @Override
                public void run() {
                    final HashSet<String> set = new HashSet<String>();
                    synchronized (AppEventsLogger.staticLock) {
                        final Iterator<AccessTokenAppIdPair> iterator = AppEventsLogger.stateMap.keySet().iterator();
                        while (iterator.hasNext()) {
                            set.add(iterator.next().getApplicationId());
                        }
                    }
                    // monitorexit(o)
                    final Set<String> set2;
                    final Iterator<String> iterator2 = set2.iterator();
                    while (iterator2.hasNext()) {
                        Utility.queryAppSettings(iterator2.next(), true);
                    }
                }
            };
            AppEventsLogger.backgroundExecutor.scheduleAtFixedRate((Runnable)staticLock, 0L, 86400L, TimeUnit.SECONDS);
        }
    }
    
    private void logAppSessionResumeEvent(final long n, final String s) {
        PersistedAppSessionInfo.onResume(AppEventsLogger.applicationContext, this.accessTokenAppId, this, n, s);
    }
    
    private void logAppSessionSuspendEvent(final long n) {
        PersistedAppSessionInfo.onSuspend(AppEventsLogger.applicationContext, this.accessTokenAppId, this, n);
    }
    
    private static void logEvent(final Context context, final AppEvent appEvent, final AccessTokenAppIdPair accessTokenAppIdPair) {
        Settings.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                getSessionEventsState(context, accessTokenAppIdPair).addEvent(appEvent);
                flushIfNecessary();
            }
        });
    }
    
    private void logEvent(final String s, final Double n, final Bundle bundle, final boolean b) {
        logEvent(this.context, new AppEvent(this.context, s, n, bundle, b), this.accessTokenAppId);
    }
    
    public static AppEventsLogger newLogger(final Context context) {
        return new AppEventsLogger(context, null, null);
    }
    
    public static AppEventsLogger newLogger(final Context context, final Session session) {
        return new AppEventsLogger(context, null, session);
    }
    
    public static AppEventsLogger newLogger(final Context context, final String s) {
        return new AppEventsLogger(context, s, null);
    }
    
    public static AppEventsLogger newLogger(final Context context, final String s, final Session session) {
        return new AppEventsLogger(context, s, session);
    }
    
    private static void notifyDeveloperError(final String s) {
        Logger.log(LoggingBehavior.DEVELOPER_ERRORS, "AppEvents", s);
    }
    
    public static void onContextStop() {
        PersistedEvents.persistEvents(AppEventsLogger.applicationContext, AppEventsLogger.stateMap);
    }
    
    static void resetSourceApplication() {
        AppEventsLogger.sourceApplication = null;
        AppEventsLogger.isOpenedByApplink = false;
    }
    
    public static void setFlushBehavior(final FlushBehavior flushBehavior) {
        synchronized (AppEventsLogger.staticLock) {
            AppEventsLogger.flushBehavior = flushBehavior;
        }
    }
    
    @Deprecated
    public static void setLimitEventUsage(final Context context, final boolean b) {
        Settings.setLimitEventAndDataUsage(context, b);
    }
    
    private static void setSourceApplication(final Activity activity) {
        final ComponentName callingActivity = activity.getCallingActivity();
        if (callingActivity != null) {
            final String packageName = callingActivity.getPackageName();
            if (packageName.equals(activity.getPackageName())) {
                resetSourceApplication();
                return;
            }
            AppEventsLogger.sourceApplication = packageName;
        }
        final Intent intent = activity.getIntent();
        if (intent == null || intent.getBooleanExtra("_fbSourceApplicationHasBeenSet", false)) {
            resetSourceApplication();
            return;
        }
        final Bundle appLinkData = AppLinks.getAppLinkData(intent);
        if (appLinkData == null) {
            resetSourceApplication();
            return;
        }
        AppEventsLogger.isOpenedByApplink = true;
        final Bundle bundle = appLinkData.getBundle("referer_app_link");
        if (bundle == null) {
            AppEventsLogger.sourceApplication = null;
            return;
        }
        AppEventsLogger.sourceApplication = bundle.getString("package");
        intent.putExtra("_fbSourceApplicationHasBeenSet", true);
    }
    
    static void setSourceApplication(final String sourceApplication, final boolean isOpenedByApplink) {
        AppEventsLogger.sourceApplication = sourceApplication;
        AppEventsLogger.isOpenedByApplink = isOpenedByApplink;
    }
    
    public void flush() {
        flush(FlushReason.EXPLICIT);
    }
    
    public String getApplicationId() {
        return this.accessTokenAppId.getApplicationId();
    }
    
    boolean isValidForSession(final Session session) {
        return this.accessTokenAppId.equals(new AccessTokenAppIdPair(session));
    }
    
    public void logEvent(final String s) {
        this.logEvent(s, null);
    }
    
    public void logEvent(final String s, final double n) {
        this.logEvent(s, n, null);
    }
    
    public void logEvent(final String s, final double n, final Bundle bundle) {
        this.logEvent(s, n, bundle, false);
    }
    
    public void logEvent(final String s, final Bundle bundle) {
        this.logEvent(s, null, bundle, false);
    }
    
    public void logPurchase(final BigDecimal bigDecimal, final Currency currency) {
        this.logPurchase(bigDecimal, currency, null);
    }
    
    public void logPurchase(final BigDecimal bigDecimal, final Currency currency, final Bundle bundle) {
        if (bigDecimal == null) {
            notifyDeveloperError("purchaseAmount cannot be null");
            return;
        }
        if (currency == null) {
            notifyDeveloperError("currency cannot be null");
            return;
        }
        Bundle bundle2;
        if ((bundle2 = bundle) == null) {
            bundle2 = new Bundle();
        }
        bundle2.putString("fb_currency", currency.getCurrencyCode());
        this.logEvent("fb_mobile_purchase", bigDecimal.doubleValue(), bundle2);
        eagerFlush();
    }
    
    public void logSdkEvent(final String s, final Double n, final Bundle bundle) {
        this.logEvent(s, n, bundle, true);
    }
    
    private static class AccessTokenAppIdPair implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final String accessToken;
        private final String applicationId;
        
        AccessTokenAppIdPair(final Session session) {
            this(session.getAccessToken(), session.getApplicationId());
        }
        
        AccessTokenAppIdPair(final String s, final String applicationId) {
            String accessToken = s;
            if (Utility.isNullOrEmpty(s)) {
                accessToken = null;
            }
            this.accessToken = accessToken;
            this.applicationId = applicationId;
        }
        
        private Object writeReplace() {
            return new SerializationProxyV1(this.accessToken, this.applicationId);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof AccessTokenAppIdPair) {
                final AccessTokenAppIdPair accessTokenAppIdPair = (AccessTokenAppIdPair)o;
                if (Utility.areObjectsEqual(accessTokenAppIdPair.accessToken, this.accessToken) && Utility.areObjectsEqual(accessTokenAppIdPair.applicationId, this.applicationId)) {
                    return true;
                }
            }
            return false;
        }
        
        String getAccessToken() {
            return this.accessToken;
        }
        
        String getApplicationId() {
            return this.applicationId;
        }
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            int hashCode2;
            if (this.accessToken == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.accessToken.hashCode();
            }
            if (this.applicationId != null) {
                hashCode = this.applicationId.hashCode();
            }
            return hashCode2 ^ hashCode;
        }
        
        private static class SerializationProxyV1 implements Serializable
        {
            private static final long serialVersionUID = -2488473066578201069L;
            private final String accessToken;
            private final String appId;
            
            private SerializationProxyV1(final String accessToken, final String appId) {
                this.accessToken = accessToken;
                this.appId = appId;
            }
            
            private Object readResolve() {
                return new AccessTokenAppIdPair(this.accessToken, this.appId);
            }
        }
    }
    
    static class AppEvent implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private static final HashSet<String> validatedIdentifiers;
        private boolean isImplicit;
        private JSONObject jsonObject;
        private String name;
        
        static {
            validatedIdentifiers = new HashSet<String>();
        }
        
        public AppEvent(final Context context, String name, final Double n, final Bundle bundle, final boolean isImplicit) {
            try {
                this.validateIdentifier(name);
                this.name = name;
                this.isImplicit = isImplicit;
                (this.jsonObject = new JSONObject()).put("_eventName", (Object)name);
                this.jsonObject.put("_logTime", System.currentTimeMillis() / 1000L);
                this.jsonObject.put("_ui", (Object)Utility.getActivityName(context));
                if (n != null) {
                    this.jsonObject.put("_valueToSum", (double)n);
                }
                if (this.isImplicit) {
                    this.jsonObject.put("_implicitlyLogged", (Object)"1");
                }
                final String appVersion = Settings.getAppVersion();
                if (appVersion != null) {
                    this.jsonObject.put("_appVersion", (Object)appVersion);
                }
                if (bundle != null) {
                    final Iterator<String> iterator = bundle.keySet().iterator();
                    if (iterator.hasNext()) {
                        name = iterator.next();
                        this.validateIdentifier(name);
                        final Object value = bundle.get(name);
                        if (!(value instanceof String) && !(value instanceof Number)) {
                            throw new FacebookException(String.format("Parameter value '%s' for key '%s' should be a string or a numeric type.", value, name));
                        }
                        goto Label_0243;
                    }
                }
            }
            catch (JSONException ex) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "JSON encoding for app event failed: '%s'", ex.toString());
                this.jsonObject = null;
            }
            catch (FacebookException ex2) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Invalid app event name or parameter:", ex2.toString());
                this.jsonObject = null;
                return;
            }
            if (!this.isImplicit) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Created app event '%s'", this.jsonObject.toString());
                return;
            }
            goto Label_0242;
        }
        
        private AppEvent(final String s, final boolean isImplicit) throws JSONException {
            this.jsonObject = new JSONObject(s);
            this.isImplicit = isImplicit;
        }
        
        private void validateIdentifier(final String p0) throws FacebookException {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_1        
            //     1: ifnull          20
            //     4: aload_1        
            //     5: invokevirtual   java/lang/String.length:()I
            //     8: ifeq            20
            //    11: aload_1        
            //    12: invokevirtual   java/lang/String.length:()I
            //    15: bipush          40
            //    17: if_icmple       58
            //    20: aload_1        
            //    21: astore_3       
            //    22: aload_1        
            //    23: ifnonnull       29
            //    26: ldc             "<None Provided>"
            //    28: astore_3       
            //    29: new             Lcom/facebook/FacebookException;
            //    32: dup            
            //    33: ldc             "Identifier '%s' must be less than %d characters"
            //    35: iconst_2       
            //    36: anewarray       Ljava/lang/Object;
            //    39: dup            
            //    40: iconst_0       
            //    41: aload_3        
            //    42: aastore        
            //    43: dup            
            //    44: iconst_1       
            //    45: bipush          40
            //    47: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //    50: aastore        
            //    51: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
            //    54: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/String;)V
            //    57: athrow         
            //    58: getstatic       com/facebook/AppEventsLogger$AppEvent.validatedIdentifiers:Ljava/util/HashSet;
            //    61: astore_3       
            //    62: aload_3        
            //    63: monitorenter   
            //    64: getstatic       com/facebook/AppEventsLogger$AppEvent.validatedIdentifiers:Ljava/util/HashSet;
            //    67: aload_1        
            //    68: invokevirtual   java/util/HashSet.contains:(Ljava/lang/Object;)Z
            //    71: istore_2       
            //    72: aload_3        
            //    73: monitorexit    
            //    74: iload_2        
            //    75: ifne            103
            //    78: aload_1        
            //    79: ldc             "^[0-9a-zA-Z_]+[0-9a-zA-Z _-]*$"
            //    81: invokevirtual   java/lang/String.matches:(Ljava/lang/String;)Z
            //    84: ifeq            114
            //    87: getstatic       com/facebook/AppEventsLogger$AppEvent.validatedIdentifiers:Ljava/util/HashSet;
            //    90: astore_3       
            //    91: aload_3        
            //    92: monitorenter   
            //    93: getstatic       com/facebook/AppEventsLogger$AppEvent.validatedIdentifiers:Ljava/util/HashSet;
            //    96: aload_1        
            //    97: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
            //   100: pop            
            //   101: aload_3        
            //   102: monitorexit    
            //   103: return         
            //   104: astore_1       
            //   105: aload_3        
            //   106: monitorexit    
            //   107: aload_1        
            //   108: athrow         
            //   109: astore_1       
            //   110: aload_3        
            //   111: monitorexit    
            //   112: aload_1        
            //   113: athrow         
            //   114: new             Lcom/facebook/FacebookException;
            //   117: dup            
            //   118: ldc             "Skipping event named '%s' due to illegal name - must be under 40 chars and alphanumeric, _, - or space, and not start with a space or hyphen."
            //   120: iconst_1       
            //   121: anewarray       Ljava/lang/Object;
            //   124: dup            
            //   125: iconst_0       
            //   126: aload_1        
            //   127: aastore        
            //   128: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
            //   131: invokespecial   com/facebook/FacebookException.<init>:(Ljava/lang/String;)V
            //   134: athrow         
            //    Exceptions:
            //  throws com.facebook.FacebookException
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  64     74     104    109    Any
            //  93     103    109    114    Any
            //  105    107    104    109    Any
            //  110    112    109    114    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0103:
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
        
        private Object writeReplace() {
            return new SerializationProxyV1(this.jsonObject.toString(), this.isImplicit);
        }
        
        public boolean getIsImplicit() {
            return this.isImplicit;
        }
        
        public JSONObject getJSONObject() {
            return this.jsonObject;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return String.format("\"%s\", implicit: %b, json: %s", this.jsonObject.optString("_eventName"), this.isImplicit, this.jsonObject.toString());
        }
        
        private static class SerializationProxyV1 implements Serializable
        {
            private static final long serialVersionUID = -2488473066578201069L;
            private final boolean isImplicit;
            private final String jsonString;
            
            private SerializationProxyV1(final String jsonString, final boolean isImplicit) {
                this.jsonString = jsonString;
                this.isImplicit = isImplicit;
            }
            
            private Object readResolve() throws JSONException {
                return new AppEvent(this.jsonString, this.isImplicit);
            }
        }
    }
    
    public enum FlushBehavior
    {
        AUTO, 
        EXPLICIT_ONLY;
    }
    
    private enum FlushReason
    {
        EAGER_FLUSHING_EVENT, 
        EVENT_THRESHOLD, 
        EXPLICIT, 
        PERSISTED_EVENTS, 
        SESSION_CHANGE, 
        TIMER;
    }
    
    private enum FlushResult
    {
        NO_CONNECTIVITY, 
        SERVER_ERROR, 
        SUCCESS, 
        UNKNOWN_ERROR;
    }
    
    private static class FlushStatistics
    {
        public int numEvents;
        public FlushResult result;
        
        private FlushStatistics() {
            this.numEvents = 0;
            this.result = FlushResult.SUCCESS;
        }
    }
    
    static class PersistedAppSessionInfo
    {
        private static final String PERSISTED_SESSION_INFO_FILENAME = "AppEventsLogger.persistedsessioninfo";
        private static final Runnable appSessionInfoFlushRunnable;
        private static Map<AccessTokenAppIdPair, FacebookTimeSpentData> appSessionInfoMap;
        private static boolean hasChanges;
        private static boolean isLoaded;
        private static final Object staticLock;
        
        static {
            staticLock = new Object();
            PersistedAppSessionInfo.hasChanges = false;
            PersistedAppSessionInfo.isLoaded = false;
            appSessionInfoFlushRunnable = new Runnable() {
                @Override
                public void run() {
                    PersistedAppSessionInfo.saveAppSessionInformation(AppEventsLogger.applicationContext);
                }
            };
        }
        
        private static FacebookTimeSpentData getTimeSpentData(final Context context, final AccessTokenAppIdPair accessTokenAppIdPair) {
            restoreAppSessionInformation(context);
            FacebookTimeSpentData facebookTimeSpentData;
            if ((facebookTimeSpentData = PersistedAppSessionInfo.appSessionInfoMap.get(accessTokenAppIdPair)) == null) {
                facebookTimeSpentData = new FacebookTimeSpentData();
                PersistedAppSessionInfo.appSessionInfoMap.put(accessTokenAppIdPair, facebookTimeSpentData);
            }
            return facebookTimeSpentData;
        }
        
        static void onResume(final Context context, final AccessTokenAppIdPair accessTokenAppIdPair, final AppEventsLogger appEventsLogger, final long n, final String s) {
            synchronized (PersistedAppSessionInfo.staticLock) {
                getTimeSpentData(context, accessTokenAppIdPair).onResume(appEventsLogger, n, s);
                onTimeSpentDataUpdate();
            }
        }
        
        static void onSuspend(final Context context, final AccessTokenAppIdPair accessTokenAppIdPair, final AppEventsLogger appEventsLogger, final long n) {
            synchronized (PersistedAppSessionInfo.staticLock) {
                getTimeSpentData(context, accessTokenAppIdPair).onSuspend(appEventsLogger, n);
                onTimeSpentDataUpdate();
            }
        }
        
        private static void onTimeSpentDataUpdate() {
            if (!PersistedAppSessionInfo.hasChanges) {
                PersistedAppSessionInfo.hasChanges = true;
                AppEventsLogger.backgroundExecutor.schedule(PersistedAppSessionInfo.appSessionInfoFlushRunnable, 30L, TimeUnit.SECONDS);
            }
        }
        
        private static void restoreAppSessionInformation(final Context p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore          5
            //     3: aconst_null    
            //     4: astore_3       
            //     5: aconst_null    
            //     6: astore          4
            //     8: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.staticLock:Ljava/lang/Object;
            //    11: astore          6
            //    13: aload           6
            //    15: monitorenter   
            //    16: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.isLoaded:Z
            //    19: istore_1       
            //    20: iload_1        
            //    21: ifne            93
            //    24: new             Ljava/io/ObjectInputStream;
            //    27: dup            
            //    28: aload_0        
            //    29: ldc             "AppEventsLogger.persistedsessioninfo"
            //    31: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
            //    34: invokespecial   java/io/ObjectInputStream.<init>:(Ljava/io/InputStream;)V
            //    37: astore_2       
            //    38: aload_2        
            //    39: invokevirtual   java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
            //    42: checkcast       Ljava/util/HashMap;
            //    45: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //    48: getstatic       com/facebook/LoggingBehavior.APP_EVENTS:Lcom/facebook/LoggingBehavior;
            //    51: ldc             "AppEvents"
            //    53: ldc             "App session info loaded"
            //    55: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;)V
            //    58: aload_2        
            //    59: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    62: aload_0        
            //    63: ldc             "AppEventsLogger.persistedsessioninfo"
            //    65: invokevirtual   android/content/Context.deleteFile:(Ljava/lang/String;)Z
            //    68: pop            
            //    69: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //    72: ifnonnull       85
            //    75: new             Ljava/util/HashMap;
            //    78: dup            
            //    79: invokespecial   java/util/HashMap.<init>:()V
            //    82: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //    85: iconst_1       
            //    86: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.isLoaded:Z
            //    89: iconst_0       
            //    90: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.hasChanges:Z
            //    93: aload           6
            //    95: monitorexit    
            //    96: return         
            //    97: aload_2        
            //    98: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   101: aload_0        
            //   102: ldc             "AppEventsLogger.persistedsessioninfo"
            //   104: invokevirtual   android/content/Context.deleteFile:(Ljava/lang/String;)Z
            //   107: pop            
            //   108: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //   111: ifnonnull       124
            //   114: new             Ljava/util/HashMap;
            //   117: dup            
            //   118: invokespecial   java/util/HashMap.<init>:()V
            //   121: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //   124: iconst_1       
            //   125: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.isLoaded:Z
            //   128: iconst_0       
            //   129: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.hasChanges:Z
            //   132: goto            93
            //   135: aload           6
            //   137: monitorexit    
            //   138: aload_0        
            //   139: athrow         
            //   140: astore          4
            //   142: aload           5
            //   144: astore_2       
            //   145: aload_2        
            //   146: astore_3       
            //   147: invokestatic    com/facebook/AppEventsLogger.access$1300:()Ljava/lang/String;
            //   150: new             Ljava/lang/StringBuilder;
            //   153: dup            
            //   154: invokespecial   java/lang/StringBuilder.<init>:()V
            //   157: ldc             "Got unexpected exception: "
            //   159: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   162: aload           4
            //   164: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
            //   167: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   170: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   173: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //   176: pop            
            //   177: aload_2        
            //   178: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   181: aload_0        
            //   182: ldc             "AppEventsLogger.persistedsessioninfo"
            //   184: invokevirtual   android/content/Context.deleteFile:(Ljava/lang/String;)Z
            //   187: pop            
            //   188: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //   191: ifnonnull       204
            //   194: new             Ljava/util/HashMap;
            //   197: dup            
            //   198: invokespecial   java/util/HashMap.<init>:()V
            //   201: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //   204: iconst_1       
            //   205: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.isLoaded:Z
            //   208: iconst_0       
            //   209: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.hasChanges:Z
            //   212: goto            93
            //   215: aload_3        
            //   216: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   219: aload_0        
            //   220: ldc             "AppEventsLogger.persistedsessioninfo"
            //   222: invokevirtual   android/content/Context.deleteFile:(Ljava/lang/String;)Z
            //   225: pop            
            //   226: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //   229: ifnonnull       242
            //   232: new             Ljava/util/HashMap;
            //   235: dup            
            //   236: invokespecial   java/util/HashMap.<init>:()V
            //   239: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //   242: iconst_1       
            //   243: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.isLoaded:Z
            //   246: iconst_0       
            //   247: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.hasChanges:Z
            //   250: aload_2        
            //   251: athrow         
            //   252: astore_0       
            //   253: goto            135
            //   256: astore          4
            //   258: aload_2        
            //   259: astore_3       
            //   260: aload           4
            //   262: astore_2       
            //   263: goto            215
            //   266: astore          4
            //   268: goto            145
            //   271: astore_3       
            //   272: goto            97
            //   275: astore_2       
            //   276: aload           4
            //   278: astore_2       
            //   279: goto            97
            //   282: astore_0       
            //   283: goto            135
            //   286: astore_2       
            //   287: goto            215
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                           
            //  -----  -----  -----  -----  -------------------------------
            //  16     20     282    286    Any
            //  24     38     275    282    Ljava/io/FileNotFoundException;
            //  24     38     140    145    Ljava/lang/Exception;
            //  24     38     286    290    Any
            //  38     58     271    275    Ljava/io/FileNotFoundException;
            //  38     58     266    271    Ljava/lang/Exception;
            //  38     58     256    266    Any
            //  58     85     252    256    Any
            //  85     93     252    256    Any
            //  93     96     282    286    Any
            //  97     124    282    286    Any
            //  124    132    282    286    Any
            //  135    138    282    286    Any
            //  147    177    286    290    Any
            //  177    204    282    286    Any
            //  204    212    282    286    Any
            //  215    242    282    286    Any
            //  242    252    282    286    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0085:
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
        
        static void saveAppSessionInformation(final Context p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore_2       
            //     2: aconst_null    
            //     3: astore_3       
            //     4: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.staticLock:Ljava/lang/Object;
            //     7: astore          4
            //     9: aload           4
            //    11: monitorenter   
            //    12: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.hasChanges:Z
            //    15: istore_1       
            //    16: iload_1        
            //    17: ifeq            67
            //    20: new             Ljava/io/ObjectOutputStream;
            //    23: dup            
            //    24: new             Ljava/io/BufferedOutputStream;
            //    27: dup            
            //    28: aload_0        
            //    29: ldc             "AppEventsLogger.persistedsessioninfo"
            //    31: iconst_0       
            //    32: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    35: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
            //    38: invokespecial   java/io/ObjectOutputStream.<init>:(Ljava/io/OutputStream;)V
            //    41: astore_0       
            //    42: aload_0        
            //    43: getstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.appSessionInfoMap:Ljava/util/Map;
            //    46: invokevirtual   java/io/ObjectOutputStream.writeObject:(Ljava/lang/Object;)V
            //    49: iconst_0       
            //    50: putstatic       com/facebook/AppEventsLogger$PersistedAppSessionInfo.hasChanges:Z
            //    53: getstatic       com/facebook/LoggingBehavior.APP_EVENTS:Lcom/facebook/LoggingBehavior;
            //    56: ldc             "AppEvents"
            //    58: ldc             "App session info saved"
            //    60: invokestatic    com/facebook/internal/Logger.log:(Lcom/facebook/LoggingBehavior;Ljava/lang/String;Ljava/lang/String;)V
            //    63: aload_0        
            //    64: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    67: aload           4
            //    69: monitorexit    
            //    70: return         
            //    71: astore_2       
            //    72: aload_3        
            //    73: astore_0       
            //    74: aload_2        
            //    75: astore_3       
            //    76: aload_0        
            //    77: astore_2       
            //    78: invokestatic    com/facebook/AppEventsLogger.access$1300:()Ljava/lang/String;
            //    81: new             Ljava/lang/StringBuilder;
            //    84: dup            
            //    85: invokespecial   java/lang/StringBuilder.<init>:()V
            //    88: ldc             "Got unexpected exception: "
            //    90: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    93: aload_3        
            //    94: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
            //    97: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   100: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   103: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //   106: pop            
            //   107: aload_0        
            //   108: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   111: goto            67
            //   114: aload           4
            //   116: monitorexit    
            //   117: aload_0        
            //   118: athrow         
            //   119: astore_0       
            //   120: aload_2        
            //   121: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   124: aload_0        
            //   125: athrow         
            //   126: astore_0       
            //   127: goto            114
            //   130: astore_3       
            //   131: aload_0        
            //   132: astore_2       
            //   133: aload_3        
            //   134: astore_0       
            //   135: goto            120
            //   138: astore_3       
            //   139: goto            76
            //   142: astore_0       
            //   143: goto            114
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  12     16     142    146    Any
            //  20     42     71     76     Ljava/lang/Exception;
            //  20     42     119    120    Any
            //  42     63     138    142    Ljava/lang/Exception;
            //  42     63     130    138    Any
            //  63     67     126    130    Any
            //  67     70     142    146    Any
            //  78     107    119    120    Any
            //  107    111    142    146    Any
            //  114    117    142    146    Any
            //  120    126    142    146    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0067:
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
    }
    
    static class PersistedEvents
    {
        static final String PERSISTED_EVENTS_FILENAME = "AppEventsLogger.persistedevents";
        private static Object staticLock;
        private Context context;
        private HashMap<AccessTokenAppIdPair, List<AppEvent>> persistedEvents;
        
        static {
            PersistedEvents.staticLock = new Object();
        }
        
        private PersistedEvents(final Context context) {
            this.persistedEvents = new HashMap<AccessTokenAppIdPair, List<AppEvent>>();
            this.context = context;
        }
        
        public static void persistEvents(final Context context, final AccessTokenAppIdPair accessTokenAppIdPair, final SessionEventsState sessionEventsState) {
            final HashMap<AccessTokenAppIdPair, SessionEventsState> hashMap = new HashMap<AccessTokenAppIdPair, SessionEventsState>();
            hashMap.put(accessTokenAppIdPair, sessionEventsState);
            persistEvents(context, hashMap);
        }
        
        public static void persistEvents(final Context context, final Map<AccessTokenAppIdPair, SessionEventsState> map) {
            synchronized (PersistedEvents.staticLock) {
                final PersistedEvents andClearStore = readAndClearStore(context);
                for (final Map.Entry<AccessTokenAppIdPair, SessionEventsState> entry : map.entrySet()) {
                    final List<AppEvent> eventsToPersist = entry.getValue().getEventsToPersist();
                    if (eventsToPersist.size() != 0) {
                        andClearStore.addEvents(entry.getKey(), eventsToPersist);
                    }
                }
            }
            final PersistedEvents persistedEvents;
            persistedEvents.write();
        }
        // monitorexit(o)
        
        public static PersistedEvents readAndClearStore(final Context context) {
            synchronized (PersistedEvents.staticLock) {
                final PersistedEvents persistedEvents = new PersistedEvents(context);
                persistedEvents.readAndClearStore();
                return persistedEvents;
            }
        }
        
        private void readAndClearStore() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore          4
            //     3: aconst_null    
            //     4: astore_2       
            //     5: aconst_null    
            //     6: astore_3       
            //     7: new             Ljava/io/ObjectInputStream;
            //    10: dup            
            //    11: new             Ljava/io/BufferedInputStream;
            //    14: dup            
            //    15: aload_0        
            //    16: getfield        com/facebook/AppEventsLogger$PersistedEvents.context:Landroid/content/Context;
            //    19: ldc             "AppEventsLogger.persistedevents"
            //    21: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
            //    24: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
            //    27: invokespecial   java/io/ObjectInputStream.<init>:(Ljava/io/InputStream;)V
            //    30: astore_1       
            //    31: aload_1        
            //    32: invokevirtual   java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
            //    35: checkcast       Ljava/util/HashMap;
            //    38: astore_2       
            //    39: aload_0        
            //    40: getfield        com/facebook/AppEventsLogger$PersistedEvents.context:Landroid/content/Context;
            //    43: ldc             "AppEventsLogger.persistedevents"
            //    45: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
            //    48: invokevirtual   java/io/File.delete:()Z
            //    51: pop            
            //    52: aload_0        
            //    53: aload_2        
            //    54: putfield        com/facebook/AppEventsLogger$PersistedEvents.persistedEvents:Ljava/util/HashMap;
            //    57: aload_1        
            //    58: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    61: return         
            //    62: astore_1       
            //    63: aload_3        
            //    64: astore_1       
            //    65: aload_1        
            //    66: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    69: return         
            //    70: astore_3       
            //    71: aload           4
            //    73: astore_1       
            //    74: aload_1        
            //    75: astore_2       
            //    76: invokestatic    com/facebook/AppEventsLogger.access$1300:()Ljava/lang/String;
            //    79: new             Ljava/lang/StringBuilder;
            //    82: dup            
            //    83: invokespecial   java/lang/StringBuilder.<init>:()V
            //    86: ldc             "Got unexpected exception: "
            //    88: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    91: aload_3        
            //    92: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
            //    95: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    98: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   101: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //   104: pop            
            //   105: aload_1        
            //   106: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   109: return         
            //   110: astore_1       
            //   111: aload_2        
            //   112: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //   115: aload_1        
            //   116: athrow         
            //   117: astore_3       
            //   118: aload_1        
            //   119: astore_2       
            //   120: aload_3        
            //   121: astore_1       
            //   122: goto            111
            //   125: astore_3       
            //   126: goto            74
            //   129: astore_2       
            //   130: goto            65
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                           
            //  -----  -----  -----  -----  -------------------------------
            //  7      31     62     65     Ljava/io/FileNotFoundException;
            //  7      31     70     74     Ljava/lang/Exception;
            //  7      31     110    111    Any
            //  31     57     129    133    Ljava/io/FileNotFoundException;
            //  31     57     125    129    Ljava/lang/Exception;
            //  31     57     117    125    Any
            //  76     105    110    111    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0074:
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
        
        private void write() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aconst_null    
            //     1: astore_1       
            //     2: aconst_null    
            //     3: astore_3       
            //     4: new             Ljava/io/ObjectOutputStream;
            //     7: dup            
            //     8: new             Ljava/io/BufferedOutputStream;
            //    11: dup            
            //    12: aload_0        
            //    13: getfield        com/facebook/AppEventsLogger$PersistedEvents.context:Landroid/content/Context;
            //    16: ldc             "AppEventsLogger.persistedevents"
            //    18: iconst_0       
            //    19: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    22: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
            //    25: invokespecial   java/io/ObjectOutputStream.<init>:(Ljava/io/OutputStream;)V
            //    28: astore_2       
            //    29: aload_2        
            //    30: aload_0        
            //    31: getfield        com/facebook/AppEventsLogger$PersistedEvents.persistedEvents:Ljava/util/HashMap;
            //    34: invokevirtual   java/io/ObjectOutputStream.writeObject:(Ljava/lang/Object;)V
            //    37: aload_2        
            //    38: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    41: return         
            //    42: astore_1       
            //    43: aload_3        
            //    44: astore_2       
            //    45: aload_1        
            //    46: astore_3       
            //    47: aload_2        
            //    48: astore_1       
            //    49: invokestatic    com/facebook/AppEventsLogger.access$1300:()Ljava/lang/String;
            //    52: new             Ljava/lang/StringBuilder;
            //    55: dup            
            //    56: invokespecial   java/lang/StringBuilder.<init>:()V
            //    59: ldc             "Got unexpected exception: "
            //    61: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    64: aload_3        
            //    65: invokevirtual   java/lang/Exception.toString:()Ljava/lang/String;
            //    68: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    71: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    74: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //    77: pop            
            //    78: aload_2        
            //    79: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    82: return         
            //    83: astore_2       
            //    84: aload_1        
            //    85: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
            //    88: aload_2        
            //    89: athrow         
            //    90: astore_3       
            //    91: aload_2        
            //    92: astore_1       
            //    93: aload_3        
            //    94: astore_2       
            //    95: goto            84
            //    98: astore_3       
            //    99: goto            47
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  4      29     42     47     Ljava/lang/Exception;
            //  4      29     83     84     Any
            //  29     37     98     102    Ljava/lang/Exception;
            //  29     37     90     98     Any
            //  49     78     83     84     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0047:
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
        
        public void addEvents(final AccessTokenAppIdPair accessTokenAppIdPair, final List<AppEvent> list) {
            if (!this.persistedEvents.containsKey(accessTokenAppIdPair)) {
                this.persistedEvents.put(accessTokenAppIdPair, new ArrayList<AppEvent>());
            }
            this.persistedEvents.get(accessTokenAppIdPair).addAll(list);
        }
        
        public List<AppEvent> getEvents(final AccessTokenAppIdPair accessTokenAppIdPair) {
            return this.persistedEvents.get(accessTokenAppIdPair);
        }
        
        public Set<AccessTokenAppIdPair> keySet() {
            return this.persistedEvents.keySet();
        }
    }
    
    static class SessionEventsState
    {
        public static final String ENCODED_EVENTS_KEY = "encoded_events";
        public static final String EVENT_COUNT_KEY = "event_count";
        public static final String NUM_SKIPPED_KEY = "num_skipped";
        private final int MAX_ACCUMULATED_LOG_EVENTS;
        private List<AppEvent> accumulatedEvents;
        private String anonymousAppDeviceGUID;
        private AttributionIdentifiers attributionIdentifiers;
        private List<AppEvent> inFlightEvents;
        private int numSkippedEventsDueToFullBuffer;
        private String packageName;
        
        public SessionEventsState(final AttributionIdentifiers attributionIdentifiers, final String packageName, final String anonymousAppDeviceGUID) {
            this.accumulatedEvents = new ArrayList<AppEvent>();
            this.inFlightEvents = new ArrayList<AppEvent>();
            this.MAX_ACCUMULATED_LOG_EVENTS = 1000;
            this.attributionIdentifiers = attributionIdentifiers;
            this.packageName = packageName;
            this.anonymousAppDeviceGUID = anonymousAppDeviceGUID;
        }
        
        private byte[] getStringAsByteArray(final String s) {
            try {
                return s.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException ex) {
                Utility.logd("Encoding exception: ", ex);
                return null;
            }
        }
        
        private void populateRequest(final Request request, final int n, JSONArray string, final boolean b) {
            Object o = GraphObject.Factory.create();
            ((GraphObject)o).setProperty("event", "CUSTOM_APP_EVENTS");
            if (this.numSkippedEventsDueToFullBuffer > 0) {
                ((GraphObject)o).setProperty("num_skipped_events", n);
            }
            Utility.setAppEventAttributionParameters((GraphObject)o, this.attributionIdentifiers, this.anonymousAppDeviceGUID, b);
            while (true) {
                try {
                    Utility.setAppEventExtendedDeviceInfoParameters((GraphObject)o, AppEventsLogger.applicationContext);
                    ((GraphObject)o).setProperty("application_package_name", this.packageName);
                    request.setGraphObject((GraphObject)o);
                    if ((o = request.getParameters()) == null) {
                        o = new Bundle();
                    }
                    string = (JSONArray)string.toString();
                    if (string != null) {
                        ((Bundle)o).putByteArray("custom_events_file", this.getStringAsByteArray((String)string));
                        request.setTag(string);
                    }
                    request.setParameters((Bundle)o);
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
        
        public void accumulatePersistedEvents(final List<AppEvent> list) {
            synchronized (this) {
                this.accumulatedEvents.addAll(list);
            }
        }
        
        public void addEvent(final AppEvent appEvent) {
            synchronized (this) {
                if (this.accumulatedEvents.size() + this.inFlightEvents.size() >= 1000) {
                    ++this.numSkippedEventsDueToFullBuffer;
                }
                else {
                    this.accumulatedEvents.add(appEvent);
                }
            }
        }
        
        public void clearInFlightAndStats(final boolean b) {
            // monitorenter(this)
            Label_0020: {
                if (!b) {
                    break Label_0020;
                }
                try {
                    this.accumulatedEvents.addAll(this.inFlightEvents);
                    this.inFlightEvents.clear();
                    this.numSkippedEventsDueToFullBuffer = 0;
                }
                finally {
                }
                // monitorexit(this)
            }
        }
        
        public int getAccumulatedEventCount() {
            synchronized (this) {
                return this.accumulatedEvents.size();
            }
        }
        
        public List<AppEvent> getEventsToPersist() {
            synchronized (this) {
                final List<AppEvent> accumulatedEvents = this.accumulatedEvents;
                this.accumulatedEvents = new ArrayList<AppEvent>();
                return accumulatedEvents;
            }
        }
        
        public int populateRequest(final Request request, final boolean b, final boolean b2) {
            final int numSkippedEventsDueToFullBuffer;
            final JSONArray jsonArray;
            synchronized (this) {
                numSkippedEventsDueToFullBuffer = this.numSkippedEventsDueToFullBuffer;
                this.inFlightEvents.addAll(this.accumulatedEvents);
                this.accumulatedEvents.clear();
                jsonArray = new JSONArray();
                for (final AppEvent appEvent : this.inFlightEvents) {
                    if (b || !appEvent.getIsImplicit()) {
                        jsonArray.put((Object)appEvent.getJSONObject());
                    }
                }
            }
            if (jsonArray.length() == 0) {
                // monitorexit(this)
                return 0;
            }
            // monitorexit(this)
            final Request request2;
            this.populateRequest(request2, numSkippedEventsDueToFullBuffer, jsonArray, b2);
            return jsonArray.length();
        }
    }
}
