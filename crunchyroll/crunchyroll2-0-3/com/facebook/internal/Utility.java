// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import java.util.Collections;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import java.io.InputStream;
import android.os.Parcelable;
import android.util.Log;
import java.util.HashSet;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import com.facebook.FacebookException;
import org.json.JSONTokener;
import java.lang.reflect.Method;
import com.facebook.Settings;
import com.facebook.Session;
import com.facebook.Request;
import android.text.TextUtils;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import java.io.IOException;
import java.io.Closeable;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.content.Context;
import java.util.Iterator;
import android.net.Uri$Builder;
import android.net.Uri;
import android.os.Bundle;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.concurrent.ConcurrentHashMap;
import com.facebook.model.GraphObject;
import android.os.AsyncTask;
import java.util.Map;

public final class Utility
{
    private static final String APPLICATION_FIELDS = "fields";
    private static final String APP_SETTINGS_PREFS_KEY_FORMAT = "com.facebook.internal.APP_SETTINGS.%s";
    private static final String APP_SETTINGS_PREFS_STORE = "com.facebook.internal.preferences.APP_SETTINGS";
    private static final String APP_SETTING_DIALOG_CONFIGS = "android_dialog_configs";
    private static final String[] APP_SETTING_FIELDS;
    private static final String APP_SETTING_NUX_CONTENT = "gdpv4_nux_content";
    private static final String APP_SETTING_NUX_ENABLED = "gdpv4_nux_enabled";
    private static final String APP_SETTING_SUPPORTS_IMPLICIT_SDK_LOGGING = "supports_implicit_sdk_logging";
    public static final int DEFAULT_STREAM_BUFFER_SIZE = 8192;
    private static final String DIALOG_CONFIG_DIALOG_NAME_FEATURE_NAME_SEPARATOR = "\\|";
    private static final String DIALOG_CONFIG_NAME_KEY = "name";
    private static final String DIALOG_CONFIG_URL_KEY = "url";
    private static final String DIALOG_CONFIG_VERSIONS_KEY = "versions";
    private static final String EXTRA_APP_EVENTS_INFO_FORMAT_VERSION = "a1";
    private static final String HASH_ALGORITHM_MD5 = "MD5";
    private static final String HASH_ALGORITHM_SHA1 = "SHA-1";
    static final String LOG_TAG = "FacebookSDK";
    private static final String URL_SCHEME = "https";
    private static final String UTF8 = "UTF-8";
    private static Map<String, FetchedAppSettings> fetchedAppSettings;
    private static AsyncTask<Void, Void, GraphObject> initialAppSettingsLoadTask;
    
    static {
        APP_SETTING_FIELDS = new String[] { "supports_implicit_sdk_logging", "gdpv4_nux_content", "gdpv4_nux_enabled", "android_dialog_configs" };
        Utility.fetchedAppSettings = new ConcurrentHashMap<String, FetchedAppSettings>();
    }
    
    public static <T> boolean areObjectsEqual(final T t, final T t2) {
        if (t == null) {
            return t2 == null;
        }
        return t.equals(t2);
    }
    
    public static <T> ArrayList<T> arrayList(final T... array) {
        final ArrayList<T> list = new ArrayList<T>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(array[i]);
        }
        return list;
    }
    
    public static <T> List<T> asListNoNulls(final T... array) {
        final ArrayList<T> list = new ArrayList<T>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final T t = array[i];
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }
    
    public static Uri buildUri(final String s, String s2, final Bundle bundle) {
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme("https");
        uri$Builder.authority(s);
        uri$Builder.path(s2);
        final Iterator<String> iterator = bundle.keySet().iterator();
        while (iterator.hasNext()) {
            s2 = iterator.next();
            final Object value = bundle.get(s2);
            if (value instanceof String) {
                uri$Builder.appendQueryParameter(s2, (String)value);
            }
        }
        return uri$Builder.build();
    }
    
    public static void clearCaches(final Context context) {
        ImageDownloader.clearCache(context);
    }
    
    private static void clearCookiesForDomain(final Context context, final String s) {
        CookieSyncManager.createInstance(context).sync();
        final CookieManager instance = CookieManager.getInstance();
        final String cookie = instance.getCookie(s);
        if (cookie == null) {
            return;
        }
        final String[] split = cookie.split(";");
        for (int length = split.length, i = 0; i < length; ++i) {
            final String[] split2 = split[i].split("=");
            if (split2.length > 0) {
                instance.setCookie(s, split2[0].trim() + "=;expires=Sat, 1 Jan 2000 00:00:01 UTC;");
            }
        }
        instance.removeExpiredCookie();
    }
    
    public static void clearFacebookCookies(final Context context) {
        clearCookiesForDomain(context, "facebook.com");
        clearCookiesForDomain(context, ".facebook.com");
        clearCookiesForDomain(context, "https://facebook.com");
        clearCookiesForDomain(context, "https://.facebook.com");
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
    
    public static String coerceValueIfNullOrEmpty(final String s, final String s2) {
        if (isNullOrEmpty(s)) {
            return s2;
        }
        return s;
    }
    
    static Map<String, Object> convertJSONObjectToHashMap(final JSONObject jsonObject) {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        final JSONArray names = jsonObject.names();
        int n = 0;
    Label_0067_Outer:
        while (true) {
            if (n >= names.length()) {
                return hashMap;
            }
            while (true) {
                try {
                    final String string = names.getString(n);
                    Object o2;
                    final Object o = o2 = jsonObject.get(string);
                    if (o instanceof JSONObject) {
                        o2 = convertJSONObjectToHashMap((JSONObject)o);
                    }
                    hashMap.put(string, o2);
                    ++n;
                    continue Label_0067_Outer;
                }
                catch (JSONException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static void deleteDirectory(final File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                deleteDirectory(listFiles[i]);
            }
        }
        file.delete();
    }
    
    public static void disconnectQuietly(final URLConnection urlConnection) {
        if (urlConnection instanceof HttpURLConnection) {
            ((HttpURLConnection)urlConnection).disconnect();
        }
    }
    
    public static String getActivityName(final Context context) {
        if (context == null) {
            return "null";
        }
        if (context == context.getApplicationContext()) {
            return "unknown";
        }
        return context.getClass().getSimpleName();
    }
    
    private static GraphObject getAppSettingsQueryResponse(final String s) {
        final Bundle parameters = new Bundle();
        parameters.putString("fields", TextUtils.join((CharSequence)",", (Object[])Utility.APP_SETTING_FIELDS));
        final Request graphPathRequest = Request.newGraphPathRequest(null, s, null);
        graphPathRequest.setSkipClientToken(true);
        graphPathRequest.setParameters(parameters);
        return graphPathRequest.executeAndWait().getGraphObject();
    }
    
    public static DialogFeatureConfig getDialogFeatureConfig(final String s, final String s2, final String s3) {
        if (!isNullOrEmpty(s2) && !isNullOrEmpty(s3)) {
            final FetchedAppSettings fetchedAppSettings = Utility.fetchedAppSettings.get(s);
            if (fetchedAppSettings != null) {
                final Map<String, DialogFeatureConfig> map = fetchedAppSettings.getDialogConfigurations().get(s2);
                if (map != null) {
                    return (DialogFeatureConfig)map.get(s3);
                }
            }
        }
        return null;
    }
    
    public static String getMetadataApplicationId(final Context context) {
        Validate.notNull(context, "context");
        Settings.loadDefaultsFromMetadata(context);
        return Settings.getApplicationId();
    }
    
    public static Method getMethodQuietly(final Class<?> clazz, final String s, final Class<?>... array) {
        try {
            return clazz.getMethod(s, array);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    public static Method getMethodQuietly(final String s, final String s2, final Class<?>... array) {
        try {
            return getMethodQuietly(Class.forName(s), s2, array);
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static Object getStringPropertyAsJSON(final JSONObject jsonObject, final String s, final String s2) throws JSONException {
        Object o2;
        final Object o = o2 = jsonObject.opt(s);
        if (o != null) {
            o2 = o;
            if (o instanceof String) {
                o2 = new JSONTokener((String)o).nextValue();
            }
        }
        if (o2 == null || o2 instanceof JSONObject || o2 instanceof JSONArray) {
            return o2;
        }
        if (s2 != null) {
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.putOpt(s2, o2);
            return jsonObject2;
        }
        throw new FacebookException("Got an unexpected non-JSON object.");
    }
    
    private static String hashBytes(final MessageDigest messageDigest, final byte[] array) {
        messageDigest.update(array);
        final byte[] digest = messageDigest.digest();
        final StringBuilder sb = new StringBuilder();
        for (int length = digest.length, i = 0; i < length; ++i) {
            final byte b = digest[i];
            sb.append(Integer.toHexString(b >> 4 & 0xF));
            sb.append(Integer.toHexString(b >> 0 & 0xF));
        }
        return sb.toString();
    }
    
    private static String hashWithAlgorithm(final String s, final String s2) {
        return hashWithAlgorithm(s, s2.getBytes());
    }
    
    private static String hashWithAlgorithm(final String s, final byte[] array) {
        try {
            return hashBytes(MessageDigest.getInstance(s), array);
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    
    public static int[] intersectRanges(final int[] array, final int[] array2) {
        if (array == null) {
            return array2;
        }
        if (array2 == null) {
            return array;
        }
        final int[] array3 = new int[array.length + array2.length];
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4;
        while (true) {
            n4 = n;
            if (n2 >= array.length) {
                break;
            }
            n4 = n;
            if (n3 >= array2.length) {
                break;
            }
            int n5 = Integer.MIN_VALUE;
            final int n6 = Integer.MAX_VALUE;
            final int n7 = array[n2];
            int n8 = Integer.MAX_VALUE;
            final int n9 = array2[n3];
            int n10 = Integer.MAX_VALUE;
            if (n2 < array.length - 1) {
                n8 = array[n2 + 1];
            }
            if (n3 < array2.length - 1) {
                n10 = array2[n3 + 1];
            }
            int n11;
            int n12;
            if (n7 < n9) {
                if (n8 > n9) {
                    n5 = n9;
                    if (n8 > n10) {
                        n8 = n10;
                        n11 = n3 + 2;
                        n12 = n2;
                    }
                    else {
                        n12 = n2 + 2;
                        n11 = n3;
                    }
                }
                else {
                    n12 = n2 + 2;
                    n11 = n3;
                    n8 = n6;
                }
            }
            else if (n10 > n7) {
                n5 = n7;
                if (n10 > n8) {
                    n12 = n2 + 2;
                    n11 = n3;
                }
                else {
                    n8 = n10;
                    n11 = n3 + 2;
                    n12 = n2;
                }
            }
            else {
                n11 = n3 + 2;
                n12 = n2;
                n8 = n6;
            }
            n2 = n12;
            n3 = n11;
            if (n5 == Integer.MIN_VALUE) {
                continue;
            }
            final int n13 = n + 1;
            array3[n] = n5;
            if (n8 == Integer.MAX_VALUE) {
                n4 = n13;
                break;
            }
            n = n13 + 1;
            array3[n13] = n8;
            n2 = n12;
            n3 = n11;
        }
        return Arrays.copyOf(array3, n4);
    }
    
    public static Object invokeMethodQuietly(Object invoke, final Method method, final Object... array) {
        try {
            invoke = method.invoke(invoke, array);
            return invoke;
        }
        catch (IllegalAccessException ex) {
            return null;
        }
        catch (InvocationTargetException ex2) {
            return null;
        }
    }
    
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static <T> boolean isNullOrEmpty(final Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }
    
    public static <T> boolean isSubset(final Collection<T> collection, final Collection<T> collection2) {
        boolean b = false;
        if (collection2 == null || collection2.size() == 0) {
            if (collection == null || collection.size() == 0) {
                b = true;
            }
            return b;
        }
        final HashSet set = new HashSet((Collection<? extends E>)collection2);
        final Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!set.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public static void loadAppSettingsAsync(Context context, final String s) {
        if (!isNullOrEmpty(s) && !Utility.fetchedAppSettings.containsKey(s) && Utility.initialAppSettingsLoadTask == null) {
            final String format = String.format("com.facebook.internal.APP_SETTINGS.%s", s);
            (Utility.initialAppSettingsLoadTask = new AsyncTask<Void, Void, GraphObject>() {
                protected GraphObject doInBackground(final Void... array) {
                    return getAppSettingsQueryResponse(s);
                }
                
                protected void onPostExecute(final GraphObject graphObject) {
                    if (graphObject != null) {
                        final JSONObject innerJSONObject = graphObject.getInnerJSONObject();
                        parseAppSettingsFromJSON(s, innerJSONObject);
                        context.getSharedPreferences("com.facebook.internal.preferences.APP_SETTINGS", 0).edit().putString(format, innerJSONObject.toString()).apply();
                    }
                    Utility.initialAppSettingsLoadTask = null;
                }
            }).execute((Object[])null);
            final String string = context.getSharedPreferences("com.facebook.internal.preferences.APP_SETTINGS", 0).getString(format, (String)null);
            if (!isNullOrEmpty(string)) {
                context = null;
                while (true) {
                    try {
                        context = (Context)new JSONObject(string);
                        if (context != null) {
                            parseAppSettingsFromJSON(s, (JSONObject)context);
                        }
                    }
                    catch (JSONException ex) {
                        logd("FacebookSDK", (Exception)ex);
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public static void logd(final String s, final Exception ex) {
        if (Settings.isDebugEnabled() && s != null && ex != null) {
            Log.d(s, ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
    
    public static void logd(final String s, final String s2) {
        if (Settings.isDebugEnabled() && s != null && s2 != null) {
            Log.d(s, s2);
        }
    }
    
    public static void logd(final String s, final String s2, final Throwable t) {
        if (Settings.isDebugEnabled() && !isNullOrEmpty(s)) {
            Log.d(s, s2, t);
        }
    }
    
    static String md5hash(final String s) {
        return hashWithAlgorithm("MD5", s);
    }
    
    private static FetchedAppSettings parseAppSettingsFromJSON(final String s, final JSONObject jsonObject) {
        final FetchedAppSettings fetchedAppSettings = new FetchedAppSettings(jsonObject.optBoolean("supports_implicit_sdk_logging", false), jsonObject.optString("gdpv4_nux_content", ""), jsonObject.optBoolean("gdpv4_nux_enabled", false), (Map)parseDialogConfigurations(jsonObject.optJSONObject("android_dialog_configs")));
        Utility.fetchedAppSettings.put(s, fetchedAppSettings);
        return fetchedAppSettings;
    }
    
    private static Map<String, Map<String, DialogFeatureConfig>> parseDialogConfigurations(final JSONObject jsonObject) {
        final HashMap<Object, Map<String, DialogFeatureConfig>> hashMap = new HashMap<Object, Map<String, DialogFeatureConfig>>();
        if (jsonObject != null) {
            final JSONArray optJSONArray = jsonObject.optJSONArray("data");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); ++i) {
                    final DialogFeatureConfig access$400 = parseDialogConfig(optJSONArray.optJSONObject(i));
                    if (access$400 != null) {
                        final String dialogName = access$400.getDialogName();
                        Map<String, DialogFeatureConfig> map;
                        if ((map = hashMap.get(dialogName)) == null) {
                            map = new HashMap<String, DialogFeatureConfig>();
                            hashMap.put(dialogName, map);
                        }
                        map.put(access$400.getFeatureName(), access$400);
                    }
                }
            }
        }
        return (Map<String, Map<String, DialogFeatureConfig>>)hashMap;
    }
    
    public static Bundle parseUrlQueryString(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Landroid/os/Bundle;
        //     3: dup            
        //     4: invokespecial   android/os/Bundle.<init>:()V
        //     7: astore_3       
        //     8: aload_0        
        //     9: invokestatic    com/facebook/internal/Utility.isNullOrEmpty:(Ljava/lang/String;)Z
        //    12: ifne            113
        //    15: aload_0        
        //    16: ldc_w           "&"
        //    19: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    22: astore_0       
        //    23: aload_0        
        //    24: arraylength    
        //    25: istore_2       
        //    26: iconst_0       
        //    27: istore_1       
        //    28: iload_1        
        //    29: iload_2        
        //    30: if_icmpge       113
        //    33: aload_0        
        //    34: iload_1        
        //    35: aaload         
        //    36: ldc             "="
        //    38: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    41: astore          4
        //    43: aload           4
        //    45: arraylength    
        //    46: iconst_2       
        //    47: if_icmpne       75
        //    50: aload_3        
        //    51: aload           4
        //    53: iconst_0       
        //    54: aaload         
        //    55: ldc             "UTF-8"
        //    57: invokestatic    java/net/URLDecoder.decode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    60: aload           4
        //    62: iconst_1       
        //    63: aaload         
        //    64: ldc             "UTF-8"
        //    66: invokestatic    java/net/URLDecoder.decode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    69: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //    72: goto            115
        //    75: aload           4
        //    77: arraylength    
        //    78: iconst_1       
        //    79: if_icmpne       115
        //    82: aload_3        
        //    83: aload           4
        //    85: iconst_0       
        //    86: aaload         
        //    87: ldc             "UTF-8"
        //    89: invokestatic    java/net/URLDecoder.decode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    92: ldc_w           ""
        //    95: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //    98: goto            115
        //   101: astore          4
        //   103: ldc             "FacebookSDK"
        //   105: aload           4
        //   107: invokestatic    com/facebook/internal/Utility.logd:(Ljava/lang/String;Ljava/lang/Exception;)V
        //   110: goto            115
        //   113: aload_3        
        //   114: areturn        
        //   115: iload_1        
        //   116: iconst_1       
        //   117: iadd           
        //   118: istore_1       
        //   119: goto            28
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                  
        //  -----  -----  -----  -----  --------------------------------------
        //  43     72     101    113    Ljava/io/UnsupportedEncodingException;
        //  75     98     101    113    Ljava/io/UnsupportedEncodingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    public static void putObjectInBundle(final Bundle bundle, final String s, final Object o) {
        if (o instanceof String) {
            bundle.putString(s, (String)o);
            return;
        }
        if (o instanceof Parcelable) {
            bundle.putParcelable(s, (Parcelable)o);
            return;
        }
        if (o instanceof byte[]) {
            bundle.putByteArray(s, (byte[])o);
            return;
        }
        throw new FacebookException("attempted to add unsupported type to Bundle");
    }
    
    public static FetchedAppSettings queryAppSettings(final String s, final boolean b) {
        if (!b && Utility.fetchedAppSettings.containsKey(s)) {
            return Utility.fetchedAppSettings.get(s);
        }
        final GraphObject appSettingsQueryResponse = getAppSettingsQueryResponse(s);
        if (appSettingsQueryResponse == null) {
            return null;
        }
        return parseAppSettingsFromJSON(s, appSettingsQueryResponse.getInnerJSONObject());
    }
    
    public static String readStreamToString(final InputStream p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aconst_null    
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore_3       
        //     4: new             Ljava/io/BufferedInputStream;
        //     7: dup            
        //     8: aload_0        
        //     9: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //    12: astore_0       
        //    13: new             Ljava/io/InputStreamReader;
        //    16: dup            
        //    17: aload_0        
        //    18: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    21: astore_2       
        //    22: new             Ljava/lang/StringBuilder;
        //    25: dup            
        //    26: invokespecial   java/lang/StringBuilder.<init>:()V
        //    29: astore_3       
        //    30: sipush          2048
        //    33: newarray        C
        //    35: astore          4
        //    37: aload_2        
        //    38: aload           4
        //    40: invokevirtual   java/io/InputStreamReader.read:([C)I
        //    43: istore_1       
        //    44: iload_1        
        //    45: iconst_m1      
        //    46: if_icmpeq       78
        //    49: aload_3        
        //    50: aload           4
        //    52: iconst_0       
        //    53: iload_1        
        //    54: invokevirtual   java/lang/StringBuilder.append:([CII)Ljava/lang/StringBuilder;
        //    57: pop            
        //    58: goto            37
        //    61: astore          4
        //    63: aload_2        
        //    64: astore_3       
        //    65: aload           4
        //    67: astore_2       
        //    68: aload_0        
        //    69: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //    72: aload_3        
        //    73: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //    76: aload_2        
        //    77: athrow         
        //    78: aload_3        
        //    79: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    82: astore_3       
        //    83: aload_0        
        //    84: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //    87: aload_2        
        //    88: invokestatic    com/facebook/internal/Utility.closeQuietly:(Ljava/io/Closeable;)V
        //    91: aload_3        
        //    92: areturn        
        //    93: astore          4
        //    95: aload_2        
        //    96: astore_0       
        //    97: aload           4
        //    99: astore_2       
        //   100: goto            68
        //   103: astore_2       
        //   104: goto            68
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  4      13     93     103    Any
        //  13     22     103    107    Any
        //  22     37     61     68     Any
        //  37     44     61     68     Any
        //  49     58     61     68     Any
        //  78     83     61     68     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0037:
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
    
    public static boolean safeGetBooleanFromResponse(final GraphObject graphObject, final String s) {
        Object o = false;
        if (graphObject != null) {
            o = graphObject.getProperty(s);
        }
        Object value;
        if (!(o instanceof Boolean)) {
            value = false;
        }
        else {
            value = o;
        }
        return (boolean)value;
    }
    
    public static String safeGetStringFromResponse(final GraphObject graphObject, final String s) {
        Object property = "";
        if (graphObject != null) {
            property = graphObject.getProperty(s);
        }
        Object o;
        if (!(property instanceof String)) {
            o = "";
        }
        else {
            o = property;
        }
        return (String)o;
    }
    
    public static void setAppEventAttributionParameters(final GraphObject graphObject, final AttributionIdentifiers attributionIdentifiers, final String s, final boolean b) {
        final boolean b2 = true;
        if (attributionIdentifiers != null && attributionIdentifiers.getAttributionId() != null) {
            graphObject.setProperty("attribution", attributionIdentifiers.getAttributionId());
        }
        if (attributionIdentifiers != null && attributionIdentifiers.getAndroidAdvertiserId() != null) {
            graphObject.setProperty("advertiser_id", attributionIdentifiers.getAndroidAdvertiserId());
            graphObject.setProperty("advertiser_tracking_enabled", !attributionIdentifiers.isTrackingLimited());
        }
        graphObject.setProperty("anon_id", s);
        graphObject.setProperty("application_tracking_enabled", !b && b2);
    }
    
    public static void setAppEventExtendedDeviceInfoParameters(final GraphObject graphObject, final Context context) {
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put((Object)"a1");
        final String packageName = context.getPackageName();
        final int n = -1;
        final String s = "";
        int versionCode = n;
        while (true) {
            try {
                final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                versionCode = n;
                versionCode = packageInfo.versionCode;
                final String versionName = packageInfo.versionName;
                versionCode = versionCode;
                jsonArray.put((Object)packageName);
                jsonArray.put(versionCode);
                jsonArray.put((Object)versionName);
                graphObject.setProperty("extinfo", jsonArray.toString());
            }
            catch (PackageManager$NameNotFoundException ex) {
                final String versionName = s;
                continue;
            }
            break;
        }
    }
    
    static String sha1hash(final String s) {
        return hashWithAlgorithm("SHA-1", s);
    }
    
    static String sha1hash(final byte[] array) {
        return hashWithAlgorithm("SHA-1", array);
    }
    
    public static boolean stringsEqualOrEmpty(final String s, final String s2) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        final boolean empty2 = TextUtils.isEmpty((CharSequence)s2);
        return (empty && empty2) || (!empty && !empty2 && s.equals(s2));
    }
    
    public static JSONArray tryGetJSONArrayFromResponse(final GraphObject graphObject, final String s) {
        if (graphObject == null) {
            return null;
        }
        final Object property = graphObject.getProperty(s);
        if (!(property instanceof JSONArray)) {
            return null;
        }
        return (JSONArray)property;
    }
    
    public static JSONObject tryGetJSONObjectFromResponse(final GraphObject graphObject, final String s) {
        if (graphObject == null) {
            return null;
        }
        final Object property = graphObject.getProperty(s);
        if (!(property instanceof JSONObject)) {
            return null;
        }
        return (JSONObject)property;
    }
    
    public static <T> Collection<T> unmodifiableCollection(final T... array) {
        return Collections.unmodifiableCollection((Collection<? extends T>)Arrays.asList(array));
    }
    
    public static class DialogFeatureConfig
    {
        private String dialogName;
        private Uri fallbackUrl;
        private String featureName;
        private int[] featureVersionSpec;
        
        private DialogFeatureConfig(final String dialogName, final String featureName, final Uri fallbackUrl, final int[] featureVersionSpec) {
            this.dialogName = dialogName;
            this.featureName = featureName;
            this.fallbackUrl = fallbackUrl;
            this.featureVersionSpec = featureVersionSpec;
        }
        
        private static DialogFeatureConfig parseDialogConfig(final JSONObject jsonObject) {
            final String optString = jsonObject.optString("name");
            if (!Utility.isNullOrEmpty(optString)) {
                final String[] split = optString.split("\\|");
                if (split.length == 2) {
                    final String s = split[0];
                    final String s2 = split[1];
                    if (!Utility.isNullOrEmpty(s) && !Utility.isNullOrEmpty(s2)) {
                        final String optString2 = jsonObject.optString("url");
                        Uri parse = null;
                        if (!Utility.isNullOrEmpty(optString2)) {
                            parse = Uri.parse(optString2);
                        }
                        return new DialogFeatureConfig(s, s2, parse, parseVersionSpec(jsonObject.optJSONArray("versions")));
                    }
                }
            }
            return null;
        }
        
        private static int[] parseVersionSpec(final JSONArray jsonArray) {
            int[] array = null;
            if (jsonArray != null) {
                final int length = jsonArray.length();
                final int[] array2 = new int[length];
                int n = 0;
            Label_0068_Outer:
                while (true) {
                    array = array2;
                    if (n < length) {
                        final int optInt = jsonArray.optInt(n, -1);
                        while (true) {
                            int int1;
                            if ((int1 = optInt) != -1) {
                                break Label_0068;
                            }
                            final String optString = jsonArray.optString(n);
                            int1 = optInt;
                            if (Utility.isNullOrEmpty(optString)) {
                                break Label_0068;
                            }
                            try {
                                int1 = Integer.parseInt(optString);
                                array2[n] = int1;
                                ++n;
                                continue Label_0068_Outer;
                            }
                            catch (NumberFormatException ex) {
                                Utility.logd("FacebookSDK", ex);
                                int1 = -1;
                                continue;
                            }
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
            return array;
        }
        
        public String getDialogName() {
            return this.dialogName;
        }
        
        public Uri getFallbackUrl() {
            return this.fallbackUrl;
        }
        
        public String getFeatureName() {
            return this.featureName;
        }
        
        public int[] getVersionSpec() {
            return this.featureVersionSpec;
        }
    }
    
    public static class FetchedAppSettings
    {
        private Map<String, Map<String, DialogFeatureConfig>> dialogConfigMap;
        private String nuxContent;
        private boolean nuxEnabled;
        private boolean supportsImplicitLogging;
        
        private FetchedAppSettings(final boolean supportsImplicitLogging, final String nuxContent, final boolean nuxEnabled, final Map<String, Map<String, DialogFeatureConfig>> dialogConfigMap) {
            this.supportsImplicitLogging = supportsImplicitLogging;
            this.nuxContent = nuxContent;
            this.nuxEnabled = nuxEnabled;
            this.dialogConfigMap = dialogConfigMap;
        }
        
        public Map<String, Map<String, DialogFeatureConfig>> getDialogConfigurations() {
            return this.dialogConfigMap;
        }
        
        public String getNuxContent() {
            return this.nuxContent;
        }
        
        public boolean getNuxEnabled() {
            return this.nuxEnabled;
        }
        
        public boolean supportsImplicitLogging() {
            return this.supportsImplicitLogging;
        }
    }
}
