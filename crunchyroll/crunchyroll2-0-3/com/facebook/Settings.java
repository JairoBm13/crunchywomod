// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import android.content.SharedPreferences$Editor;
import com.facebook.internal.Validate;
import android.os.Handler;
import android.os.Looper;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.model.GraphObject;
import com.facebook.internal.AttributionIdentifiers;
import java.net.HttpURLConnection;
import com.facebook.internal.Utility;
import android.content.pm.ApplicationInfo;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import android.database.Cursor;
import android.util.Log;
import android.content.ContentResolver;
import java.lang.reflect.Field;
import android.os.AsyncTask;
import android.content.pm.Signature;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.security.NoSuchAlgorithmException;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Base64;
import java.security.MessageDigest;
import android.content.Context;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Collection;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import android.net.Uri;

public final class Settings
{
    private static final String ANALYTICS_EVENT = "event";
    public static final String APPLICATION_ID_PROPERTY = "com.facebook.sdk.ApplicationId";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final Uri ATTRIBUTION_ID_CONTENT_URI;
    private static final String ATTRIBUTION_PREFERENCES = "com.facebook.sdk.attributionTracking";
    public static final String CLIENT_TOKEN_PROPERTY = "com.facebook.sdk.ClientToken";
    private static final int DEFAULT_CORE_POOL_SIZE = 5;
    private static final int DEFAULT_KEEP_ALIVE = 1;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 128;
    private static final ThreadFactory DEFAULT_THREAD_FACTORY;
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE;
    private static final String FACEBOOK_COM = "facebook.com";
    private static final Object LOCK;
    private static final String MOBILE_INSTALL_EVENT = "MOBILE_APP_INSTALL";
    private static final String PUBLISH_ACTIVITY_PATH = "%s/activities";
    private static final String TAG;
    private static volatile String appClientToken;
    private static volatile String appVersion;
    private static volatile String applicationId;
    private static volatile boolean defaultsLoaded;
    private static volatile Executor executor;
    private static volatile String facebookDomain;
    private static volatile boolean isDebugEnabled;
    private static final HashSet<LoggingBehavior> loggingBehaviors;
    private static AtomicLong onProgressThreshold;
    private static volatile boolean platformCompatibilityEnabled;
    private static Boolean sdkInitialized;
    
    static {
        TAG = Settings.class.getCanonicalName();
        loggingBehaviors = new HashSet<LoggingBehavior>(Arrays.asList(LoggingBehavior.DEVELOPER_ERRORS));
        Settings.defaultsLoaded = false;
        Settings.facebookDomain = "facebook.com";
        Settings.onProgressThreshold = new AtomicLong(65536L);
        Settings.isDebugEnabled = false;
        LOCK = new Object();
        ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
        DEFAULT_WORK_QUEUE = new LinkedBlockingQueue<Runnable>(10);
        DEFAULT_THREAD_FACTORY = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            
            @Override
            public Thread newThread(final Runnable runnable) {
                return new Thread(runnable, "FacebookSdk #" + this.counter.incrementAndGet());
            }
        };
        Settings.sdkInitialized = false;
    }
    
    public static final void addLoggingBehavior(final LoggingBehavior loggingBehavior) {
        synchronized (Settings.loggingBehaviors) {
            Settings.loggingBehaviors.add(loggingBehavior);
        }
    }
    
    public static final void clearLoggingBehaviors() {
        synchronized (Settings.loggingBehaviors) {
            Settings.loggingBehaviors.clear();
        }
    }
    
    public static String getAppVersion() {
        return Settings.appVersion;
    }
    
    public static String getApplicationId() {
        return Settings.applicationId;
    }
    
    public static String getApplicationSignature(final Context context) {
        if (context != null) {
            final PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                final String packageName = context.getPackageName();
                PackageInfo packageInfo;
                try {
                    packageInfo = packageManager.getPackageInfo(packageName, 64);
                    final Signature[] signatures = packageInfo.signatures;
                    if (signatures != null && signatures.length != 0) {
                        final String s = "SHA-1";
                        final MessageDigest messageDigest = MessageDigest.getInstance(s);
                        final MessageDigest messageDigest3;
                        final MessageDigest messageDigest2 = messageDigest3 = messageDigest;
                        final PackageInfo packageInfo2 = packageInfo;
                        final Signature[] array = packageInfo2.signatures;
                        final int n = 0;
                        final Signature signature = array[n];
                        final byte[] array2 = signature.toByteArray();
                        messageDigest3.update(array2);
                        final MessageDigest messageDigest4 = messageDigest2;
                        final byte[] array3 = messageDigest4.digest();
                        final int n2 = 9;
                        return Base64.encodeToString(array3, n2);
                    }
                    return null;
                }
                catch (PackageManager$NameNotFoundException ex) {
                    return null;
                }
                try {
                    final String s = "SHA-1";
                    final MessageDigest messageDigest = MessageDigest.getInstance(s);
                    final MessageDigest messageDigest3;
                    final MessageDigest messageDigest2 = messageDigest3 = messageDigest;
                    final PackageInfo packageInfo2 = packageInfo;
                    final Signature[] array = packageInfo2.signatures;
                    final int n = 0;
                    final Signature signature = array[n];
                    final byte[] array2 = signature.toByteArray();
                    messageDigest3.update(array2);
                    final MessageDigest messageDigest4 = messageDigest2;
                    final byte[] array3 = messageDigest4.digest();
                    final int n2 = 9;
                    return Base64.encodeToString(array3, n2);
                }
                catch (NoSuchAlgorithmException ex2) {
                    return null;
                }
            }
        }
        return null;
    }
    
    private static Executor getAsyncTaskExecutor() {
        Object o3 = null;
        Label_0026: {
            Field field;
            try {
                final Field field2;
                field = (field2 = AsyncTask.class.getField("THREAD_POOL_EXECUTOR"));
                final Object o = null;
                final Object o2 = field2.get(o);
                final Object o4;
                o3 = (o4 = o2);
                if (o4 == null) {
                    return null;
                }
                break Label_0026;
            }
            catch (NoSuchFieldException ex) {
                return null;
            }
            try {
                final Field field2 = field;
                final Object o = null;
                final Object o2 = field2.get(o);
                final Object o4;
                o3 = (o4 = o2);
                if (o4 == null) {
                    return null;
                }
            }
            catch (IllegalAccessException ex2) {
                return null;
            }
        }
        if (!(o3 instanceof Executor)) {
            return null;
        }
        return (Executor)o3;
    }
    
    public static String getAttributionId(final ContentResolver contentResolver) {
        final String s = null;
        String string = null;
        Cursor cursor = null;
        try {
            final Cursor query = contentResolver.query(Settings.ATTRIBUTION_ID_CONTENT_URI, new String[] { "aid" }, (String)null, (String[])null, (String)null);
            if (query != null) {
                cursor = query;
                string = (String)query;
                if (query.moveToFirst()) {
                    cursor = query;
                    string = (String)query;
                    cursor = (Cursor)(string = query.getString(query.getColumnIndex("aid")));
                    return (String)cursor;
                }
            }
            string = s;
            if (query != null) {
                query.close();
                string = s;
            }
            return string;
        }
        catch (Exception ex) {
            string = (String)cursor;
            Log.d(Settings.TAG, "Caught unexpected exception in getAttributionId(): " + ex.toString());
            string = s;
            return null;
        }
        finally {
            if (string != null) {
                ((Cursor)string).close();
            }
        }
    }
    
    public static String getClientToken() {
        return Settings.appClientToken;
    }
    
    public static Executor getExecutor() {
        synchronized (Settings.LOCK) {
            if (Settings.executor == null) {
                Executor asyncTaskExecutor;
                if ((asyncTaskExecutor = getAsyncTaskExecutor()) == null) {
                    asyncTaskExecutor = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, Settings.DEFAULT_WORK_QUEUE, Settings.DEFAULT_THREAD_FACTORY);
                }
                Settings.executor = asyncTaskExecutor;
            }
            return Settings.executor;
        }
    }
    
    public static String getFacebookDomain() {
        return Settings.facebookDomain;
    }
    
    public static boolean getLimitEventAndDataUsage(final Context context) {
        return context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).getBoolean("limitEventUsage", false);
    }
    
    public static final Set<LoggingBehavior> getLoggingBehaviors() {
        synchronized (Settings.loggingBehaviors) {
            return Collections.unmodifiableSet((Set<? extends LoggingBehavior>)new HashSet<LoggingBehavior>(Settings.loggingBehaviors));
        }
    }
    
    public static long getOnProgressThreshold() {
        return Settings.onProgressThreshold.get();
    }
    
    public static boolean getPlatformCompatibilityEnabled() {
        return Settings.platformCompatibilityEnabled;
    }
    
    public static String getSdkVersion() {
        return "3.23.0";
    }
    
    public static final boolean isDebugEnabled() {
        return Settings.isDebugEnabled;
    }
    
    public static final boolean isLoggingBehaviorEnabled(final LoggingBehavior loggingBehavior) {
        while (true) {
            synchronized (Settings.loggingBehaviors) {
                if (isDebugEnabled() && Settings.loggingBehaviors.contains(loggingBehavior)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    @Deprecated
    public static final boolean isLoggingEnabled() {
        return isDebugEnabled();
    }
    
    public static void loadDefaultsFromMetadata(final Context context) {
        Settings.defaultsLoaded = true;
        if (context != null) {
            try {
                final ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    if (Settings.applicationId == null) {
                        Settings.applicationId = applicationInfo.metaData.getString("com.facebook.sdk.ApplicationId");
                    }
                    if (Settings.appClientToken == null) {
                        Settings.appClientToken = applicationInfo.metaData.getString("com.facebook.sdk.ClientToken");
                    }
                }
            }
            catch (PackageManager$NameNotFoundException ex) {}
        }
    }
    
    static void loadDefaultsFromMetadataIfNeeded(final Context context) {
        if (!Settings.defaultsLoaded) {
            loadDefaultsFromMetadata(context);
        }
    }
    
    static Response publishInstallAndWaitForResponse(final Context context, String edit) {
        Label_0046: {
            if (context != null) {
                if (edit != null) {
                    break Label_0046;
                }
            }
            try {
                throw new IllegalArgumentException("Both context and applicationId must be non-null");
            }
            catch (Exception ex) {
                Utility.logd("Facebook-publish", ex);
                return new Response(null, null, new FacebookRequestError(null, ex));
            }
        }
        final AttributionIdentifiers attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.facebook.sdk.attributionTracking", 0);
        final String string = edit + "ping";
        final String string2 = edit + "json";
        final long long1 = sharedPreferences.getLong(string, 0L);
        final String string3 = sharedPreferences.getString(string2, (String)null);
        final GraphObject create = GraphObject.Factory.create();
        create.setProperty("event", "MOBILE_APP_INSTALL");
        Utility.setAppEventAttributionParameters(create, attributionIdentifiers, AppEventsLogger.getAnonymousAppDeviceGUID(context), getLimitEventAndDataUsage(context));
        create.setProperty("application_package_name", context.getPackageName());
        final Request postRequest = Request.newPostRequest(null, String.format("%s/activities", edit), create, null);
        Label_0274: {
            if (long1 == 0L) {
                break Label_0274;
            }
            Object create2;
            edit = (String)(create2 = null);
            while (true) {
                if (string3 == null) {
                    break Label_0223;
                }
                try {
                    create2 = GraphObject.Factory.create(new JSONObject(string3));
                    if (create2 == null) {
                        return Response.createResponsesFromString("true", null, new RequestBatch(new Request[] { postRequest }), true).get(0);
                    }
                    return new Response(null, null, null, (GraphObject)create2, true);
                    while (true) {
                        ((SharedPreferences$Editor)edit).apply();
                        return;
                        executeAndWait = postRequest.executeAndWait();
                        edit = (String)sharedPreferences.edit();
                        ((SharedPreferences$Editor)edit).putLong(string, System.currentTimeMillis());
                        ((SharedPreferences$Editor)edit).putString(string2, executeAndWait.getGraphObject().getInnerJSONObject().toString());
                        continue;
                    }
                }
                // iftrue(Label_0340:, executeAndWait.getGraphObject() == null || executeAndWait.getGraphObject().getInnerJSONObject() == null)
                catch (JSONException ex2) {
                    create2 = edit;
                    continue;
                }
                break;
            }
        }
    }
    
    static void publishInstallAsync(Context applicationContext, final String s, final Request.Callback callback) {
        applicationContext = applicationContext.getApplicationContext();
        getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final Response publishInstallAndWaitForResponse = Settings.publishInstallAndWaitForResponse(applicationContext, s);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            callback.onCompleted(publishInstallAndWaitForResponse);
                        }
                    });
                }
            }
        });
    }
    
    public static final void removeLoggingBehavior(final LoggingBehavior loggingBehavior) {
        synchronized (Settings.loggingBehaviors) {
            Settings.loggingBehaviors.remove(loggingBehavior);
        }
    }
    
    public static void sdkInitialize(final Context context) {
        synchronized (Settings.class) {
            if (!Settings.sdkInitialized) {
                loadDefaultsFromMetadataIfNeeded(context);
                Utility.loadAppSettingsAsync(context, getApplicationId());
                BoltsMeasurementEventListener.getInstance(context.getApplicationContext());
                Settings.sdkInitialized = true;
            }
        }
    }
    
    public static void setAppVersion(final String appVersion) {
        Settings.appVersion = appVersion;
    }
    
    public static void setApplicationId(final String applicationId) {
        Settings.applicationId = applicationId;
    }
    
    public static void setClientToken(final String appClientToken) {
        Settings.appClientToken = appClientToken;
    }
    
    public static void setExecutor(final Executor executor) {
        Validate.notNull(executor, "executor");
        synchronized (Settings.LOCK) {
            Settings.executor = executor;
        }
    }
    
    public static void setFacebookDomain(final String facebookDomain) {
        Log.w(Settings.TAG, "WARNING: Calling setFacebookDomain from non-DEBUG code.");
        Settings.facebookDomain = facebookDomain;
    }
    
    public static final void setIsDebugEnabled(final boolean isDebugEnabled) {
        Settings.isDebugEnabled = isDebugEnabled;
    }
    
    @Deprecated
    public static final void setIsLoggingEnabled(final boolean isDebugEnabled) {
        setIsDebugEnabled(isDebugEnabled);
    }
    
    public static void setLimitEventAndDataUsage(final Context context, final boolean b) {
        context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).edit().putBoolean("limitEventUsage", b).apply();
    }
    
    public static void setOnProgressThreshold(final long n) {
        Settings.onProgressThreshold.set(n);
    }
    
    public static void setPlatformCompatibilityEnabled(final boolean platformCompatibilityEnabled) {
        Settings.platformCompatibilityEnabled = platformCompatibilityEnabled;
    }
}
