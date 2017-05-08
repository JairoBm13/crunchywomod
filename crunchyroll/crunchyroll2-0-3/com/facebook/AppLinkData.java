// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook;

import java.util.Iterator;
import android.os.Parcelable;
import org.json.JSONArray;
import com.facebook.internal.Utility;
import org.json.JSONException;
import android.util.Log;
import android.content.Intent;
import com.facebook.internal.Validate;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import org.json.JSONObject;
import android.os.Bundle;

public class AppLinkData
{
    private static final String APPLINK_BRIDGE_ARGS_KEY = "bridge_args";
    private static final String APPLINK_METHOD_ARGS_KEY = "method_args";
    private static final String APPLINK_VERSION_KEY = "version";
    public static final String ARGUMENTS_NATIVE_CLASS_KEY = "com.facebook.platform.APPLINK_NATIVE_CLASS";
    public static final String ARGUMENTS_NATIVE_URL = "com.facebook.platform.APPLINK_NATIVE_URL";
    public static final String ARGUMENTS_REFERER_DATA_KEY = "referer_data";
    public static final String ARGUMENTS_TAPTIME_KEY = "com.facebook.platform.APPLINK_TAP_TIME_UTC";
    private static final String BRIDGE_ARGS_METHOD_KEY = "method";
    private static final String BUNDLE_AL_APPLINK_DATA_KEY = "al_applink_data";
    static final String BUNDLE_APPLINK_ARGS_KEY = "com.facebook.platform.APPLINK_ARGS";
    private static final String DEFERRED_APP_LINK_ARGS_FIELD = "applink_args";
    private static final String DEFERRED_APP_LINK_CLASS_FIELD = "applink_class";
    private static final String DEFERRED_APP_LINK_CLICK_TIME_FIELD = "click_time";
    private static final String DEFERRED_APP_LINK_EVENT = "DEFERRED_APP_LINK";
    private static final String DEFERRED_APP_LINK_PATH = "%s/activities";
    private static final String DEFERRED_APP_LINK_URL_FIELD = "applink_url";
    private static final String METHOD_ARGS_REF_KEY = "ref";
    private static final String METHOD_ARGS_TARGET_URL_KEY = "target_url";
    private static final String REFERER_DATA_REF_KEY = "fb_ref";
    private static final String TAG;
    private Bundle argumentBundle;
    private JSONObject arguments;
    private String ref;
    private Uri targetUri;
    
    static {
        TAG = AppLinkData.class.getCanonicalName();
    }
    
    public static AppLinkData createFromActivity(final Activity activity) {
        Validate.notNull(activity, "activity");
        final Intent intent = activity.getIntent();
        AppLinkData appLinkData;
        if (intent == null) {
            appLinkData = null;
        }
        else {
            AppLinkData appLinkData2;
            if ((appLinkData2 = createFromAlApplinkData(intent)) == null) {
                appLinkData2 = createFromJson(intent.getStringExtra("com.facebook.platform.APPLINK_ARGS"));
            }
            if ((appLinkData = appLinkData2) == null) {
                return createFromUri(intent.getData());
            }
        }
        return appLinkData;
    }
    
    private static AppLinkData createFromAlApplinkData(final Intent intent) {
        final Bundle bundleExtra = intent.getBundleExtra("al_applink_data");
        AppLinkData appLinkData;
        if (bundleExtra == null) {
            appLinkData = null;
        }
        else {
            final AppLinkData appLinkData2 = new AppLinkData();
            appLinkData2.targetUri = intent.getData();
            if (appLinkData2.targetUri == null) {
                final String string = bundleExtra.getString("target_url");
                if (string != null) {
                    appLinkData2.targetUri = Uri.parse(string);
                }
            }
            appLinkData2.argumentBundle = bundleExtra;
            appLinkData2.arguments = null;
            final Bundle bundle = bundleExtra.getBundle("referer_data");
            appLinkData = appLinkData2;
            if (bundle != null) {
                appLinkData2.ref = bundle.getString("fb_ref");
                return appLinkData2;
            }
        }
        return appLinkData;
    }
    
    private static AppLinkData createFromJson(final String s) {
        if (s == null) {
            return null;
        }
        try {
            final JSONObject jsonObject = new JSONObject(s);
            final String string = jsonObject.getString("version");
            if (!jsonObject.getJSONObject("bridge_args").getString("method").equals("applink") || !string.equals("2")) {
                goto Label_0145;
            }
            final AppLinkData appLinkData = new AppLinkData();
            appLinkData.arguments = jsonObject.getJSONObject("method_args");
            if (appLinkData.arguments.has("ref")) {
                appLinkData.ref = appLinkData.arguments.getString("ref");
                if (appLinkData.arguments.has("target_url")) {
                    appLinkData.targetUri = Uri.parse(appLinkData.arguments.getString("target_url"));
                }
                appLinkData.argumentBundle = toBundle(appLinkData.arguments);
                return appLinkData;
            }
            goto Label_0147;
        }
        catch (JSONException ex) {
            Log.d(AppLinkData.TAG, "Unable to parse AppLink JSON", (Throwable)ex);
        }
        catch (FacebookException ex2) {
            Log.d(AppLinkData.TAG, "Unable to parse AppLink JSON", (Throwable)ex2);
            goto Label_0145;
        }
    }
    
    private static AppLinkData createFromUri(final Uri targetUri) {
        if (targetUri == null) {
            return null;
        }
        final AppLinkData appLinkData = new AppLinkData();
        appLinkData.targetUri = targetUri;
        return appLinkData;
    }
    
    public static void fetchDeferredAppLinkData(final Context context, final CompletionHandler completionHandler) {
        fetchDeferredAppLinkData(context, null, completionHandler);
    }
    
    public static void fetchDeferredAppLinkData(Context applicationContext, final String s, final CompletionHandler completionHandler) {
        Validate.notNull(applicationContext, "context");
        Validate.notNull(completionHandler, "completionHandler");
        String metadataApplicationId = s;
        if (s == null) {
            metadataApplicationId = Utility.getMetadataApplicationId(applicationContext);
        }
        Validate.notNull(metadataApplicationId, "applicationId");
        applicationContext = applicationContext.getApplicationContext();
        Settings.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                fetchDeferredAppLinkFromServer(applicationContext, metadataApplicationId, completionHandler);
            }
        });
    }
    
    private static void fetchDeferredAppLinkFromServer(final Context p0, final String p1, final CompletionHandler p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: invokestatic    com/facebook/model/GraphObject$Factory.create:()Lcom/facebook/model/GraphObject;
        //     3: astore          5
        //     5: aload           5
        //     7: ldc             "event"
        //     9: ldc             "DEFERRED_APP_LINK"
        //    11: invokeinterface com/facebook/model/GraphObject.setProperty:(Ljava/lang/String;Ljava/lang/Object;)V
        //    16: aload           5
        //    18: aload_0        
        //    19: invokestatic    com/facebook/internal/AttributionIdentifiers.getAttributionIdentifiers:(Landroid/content/Context;)Lcom/facebook/internal/AttributionIdentifiers;
        //    22: aload_0        
        //    23: invokestatic    com/facebook/AppEventsLogger.getAnonymousAppDeviceGUID:(Landroid/content/Context;)Ljava/lang/String;
        //    26: aload_0        
        //    27: invokestatic    com/facebook/Settings.getLimitEventAndDataUsage:(Landroid/content/Context;)Z
        //    30: invokestatic    com/facebook/internal/Utility.setAppEventAttributionParameters:(Lcom/facebook/model/GraphObject;Lcom/facebook/internal/AttributionIdentifiers;Ljava/lang/String;Z)V
        //    33: aload           5
        //    35: ldc_w           "application_package_name"
        //    38: aload_0        
        //    39: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //    42: invokeinterface com/facebook/model/GraphObject.setProperty:(Ljava/lang/String;Ljava/lang/Object;)V
        //    47: ldc             "%s/activities"
        //    49: iconst_1       
        //    50: anewarray       Ljava/lang/Object;
        //    53: dup            
        //    54: iconst_0       
        //    55: aload_1        
        //    56: aastore        
        //    57: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    60: astore_1       
        //    61: aconst_null    
        //    62: astore          7
        //    64: aconst_null    
        //    65: astore          6
        //    67: aload           7
        //    69: astore_0       
        //    70: aconst_null    
        //    71: aload_1        
        //    72: aload           5
        //    74: aconst_null    
        //    75: invokestatic    com/facebook/Request.newPostRequest:(Lcom/facebook/Session;Ljava/lang/String;Lcom/facebook/model/GraphObject;Lcom/facebook/Request$Callback;)Lcom/facebook/Request;
        //    78: invokevirtual   com/facebook/Request.executeAndWait:()Lcom/facebook/Response;
        //    81: invokevirtual   com/facebook/Response.getGraphObject:()Lcom/facebook/model/GraphObject;
        //    84: astore_1       
        //    85: aload_1        
        //    86: ifnull          377
        //    89: aload           7
        //    91: astore_0       
        //    92: aload_1        
        //    93: invokeinterface com/facebook/model/GraphObject.getInnerJSONObject:()Lorg/json/JSONObject;
        //    98: astore          5
        //   100: aload           6
        //   102: astore_1       
        //   103: aload           5
        //   105: ifnull          369
        //   108: aload           7
        //   110: astore_0       
        //   111: aload           5
        //   113: ldc             "applink_args"
        //   115: invokevirtual   org/json/JSONObject.optString:(Ljava/lang/String;)Ljava/lang/String;
        //   118: astore          10
        //   120: aload           7
        //   122: astore_0       
        //   123: aload           5
        //   125: ldc             "click_time"
        //   127: ldc2_w          -1
        //   130: invokevirtual   org/json/JSONObject.optLong:(Ljava/lang/String;J)J
        //   133: lstore_3       
        //   134: aload           7
        //   136: astore_0       
        //   137: aload           5
        //   139: ldc             "applink_class"
        //   141: invokevirtual   org/json/JSONObject.optString:(Ljava/lang/String;)Ljava/lang/String;
        //   144: astore          8
        //   146: aload           7
        //   148: astore_0       
        //   149: aload           5
        //   151: ldc             "applink_url"
        //   153: invokevirtual   org/json/JSONObject.optString:(Ljava/lang/String;)Ljava/lang/String;
        //   156: astore          9
        //   158: aload           6
        //   160: astore_1       
        //   161: aload           7
        //   163: astore_0       
        //   164: aload           10
        //   166: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   169: ifne            369
        //   172: aload           7
        //   174: astore_0       
        //   175: aload           10
        //   177: invokestatic    com/facebook/AppLinkData.createFromJson:(Ljava/lang/String;)Lcom/facebook/AppLinkData;
        //   180: astore          5
        //   182: lload_3        
        //   183: ldc2_w          -1
        //   186: lcmp           
        //   187: ifeq            244
        //   190: aload           5
        //   192: astore_0       
        //   193: aload           5
        //   195: getfield        com/facebook/AppLinkData.arguments:Lorg/json/JSONObject;
        //   198: ifnull          216
        //   201: aload           5
        //   203: astore_0       
        //   204: aload           5
        //   206: getfield        com/facebook/AppLinkData.arguments:Lorg/json/JSONObject;
        //   209: ldc             "com.facebook.platform.APPLINK_TAP_TIME_UTC"
        //   211: lload_3        
        //   212: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;J)Lorg/json/JSONObject;
        //   215: pop            
        //   216: aload           5
        //   218: astore_0       
        //   219: aload           5
        //   221: getfield        com/facebook/AppLinkData.argumentBundle:Landroid/os/Bundle;
        //   224: ifnull          244
        //   227: aload           5
        //   229: astore_0       
        //   230: aload           5
        //   232: getfield        com/facebook/AppLinkData.argumentBundle:Landroid/os/Bundle;
        //   235: ldc             "com.facebook.platform.APPLINK_TAP_TIME_UTC"
        //   237: lload_3        
        //   238: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   241: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //   244: aload           8
        //   246: ifnull          302
        //   249: aload           5
        //   251: astore_0       
        //   252: aload           5
        //   254: getfield        com/facebook/AppLinkData.arguments:Lorg/json/JSONObject;
        //   257: ifnull          276
        //   260: aload           5
        //   262: astore_0       
        //   263: aload           5
        //   265: getfield        com/facebook/AppLinkData.arguments:Lorg/json/JSONObject;
        //   268: ldc             "com.facebook.platform.APPLINK_NATIVE_CLASS"
        //   270: aload           8
        //   272: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   275: pop            
        //   276: aload           5
        //   278: astore_0       
        //   279: aload           5
        //   281: getfield        com/facebook/AppLinkData.argumentBundle:Landroid/os/Bundle;
        //   284: ifnull          302
        //   287: aload           5
        //   289: astore_0       
        //   290: aload           5
        //   292: getfield        com/facebook/AppLinkData.argumentBundle:Landroid/os/Bundle;
        //   295: ldc             "com.facebook.platform.APPLINK_NATIVE_CLASS"
        //   297: aload           8
        //   299: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //   302: aload           5
        //   304: astore_1       
        //   305: aload           9
        //   307: ifnull          369
        //   310: aload           5
        //   312: astore_0       
        //   313: aload           5
        //   315: getfield        com/facebook/AppLinkData.arguments:Lorg/json/JSONObject;
        //   318: ifnull          337
        //   321: aload           5
        //   323: astore_0       
        //   324: aload           5
        //   326: getfield        com/facebook/AppLinkData.arguments:Lorg/json/JSONObject;
        //   329: ldc             "com.facebook.platform.APPLINK_NATIVE_URL"
        //   331: aload           9
        //   333: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   336: pop            
        //   337: aload           5
        //   339: astore_1       
        //   340: aload           5
        //   342: astore_0       
        //   343: aload           5
        //   345: getfield        com/facebook/AppLinkData.argumentBundle:Landroid/os/Bundle;
        //   348: ifnull          369
        //   351: aload           5
        //   353: astore_0       
        //   354: aload           5
        //   356: getfield        com/facebook/AppLinkData.argumentBundle:Landroid/os/Bundle;
        //   359: ldc             "com.facebook.platform.APPLINK_NATIVE_URL"
        //   361: aload           9
        //   363: invokevirtual   android/os/Bundle.putString:(Ljava/lang/String;Ljava/lang/String;)V
        //   366: aload           5
        //   368: astore_1       
        //   369: aload_2        
        //   370: aload_1        
        //   371: invokeinterface com/facebook/AppLinkData$CompletionHandler.onDeferredAppLinkDataFetched:(Lcom/facebook/AppLinkData;)V
        //   376: return         
        //   377: aconst_null    
        //   378: astore          5
        //   380: goto            100
        //   383: astore_0       
        //   384: aload           5
        //   386: astore_0       
        //   387: getstatic       com/facebook/AppLinkData.TAG:Ljava/lang/String;
        //   390: ldc_w           "Unable to put tap time in AppLinkData.arguments"
        //   393: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   396: pop            
        //   397: goto            244
        //   400: astore_1       
        //   401: getstatic       com/facebook/AppLinkData.TAG:Ljava/lang/String;
        //   404: ldc_w           "Unable to fetch deferred applink from server"
        //   407: invokestatic    com/facebook/internal/Utility.logd:(Ljava/lang/String;Ljava/lang/String;)V
        //   410: aload_0        
        //   411: astore_1       
        //   412: goto            369
        //   415: astore_0       
        //   416: aload           5
        //   418: astore_0       
        //   419: getstatic       com/facebook/AppLinkData.TAG:Ljava/lang/String;
        //   422: ldc_w           "Unable to put tap time in AppLinkData.arguments"
        //   425: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   428: pop            
        //   429: goto            302
        //   432: astore_0       
        //   433: aload           5
        //   435: astore_0       
        //   436: getstatic       com/facebook/AppLinkData.TAG:Ljava/lang/String;
        //   439: ldc_w           "Unable to put tap time in AppLinkData.arguments"
        //   442: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   445: pop            
        //   446: aload           5
        //   448: astore_1       
        //   449: goto            369
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                    
        //  -----  -----  -----  -----  ------------------------
        //  70     85     400    415    Ljava/lang/Exception;
        //  92     100    400    415    Ljava/lang/Exception;
        //  111    120    400    415    Ljava/lang/Exception;
        //  123    134    400    415    Ljava/lang/Exception;
        //  137    146    400    415    Ljava/lang/Exception;
        //  149    158    400    415    Ljava/lang/Exception;
        //  164    172    400    415    Ljava/lang/Exception;
        //  175    182    400    415    Ljava/lang/Exception;
        //  193    201    383    400    Lorg/json/JSONException;
        //  193    201    400    415    Ljava/lang/Exception;
        //  204    216    383    400    Lorg/json/JSONException;
        //  204    216    400    415    Ljava/lang/Exception;
        //  219    227    383    400    Lorg/json/JSONException;
        //  219    227    400    415    Ljava/lang/Exception;
        //  230    244    383    400    Lorg/json/JSONException;
        //  230    244    400    415    Ljava/lang/Exception;
        //  252    260    415    432    Lorg/json/JSONException;
        //  252    260    400    415    Ljava/lang/Exception;
        //  263    276    415    432    Lorg/json/JSONException;
        //  263    276    400    415    Ljava/lang/Exception;
        //  279    287    415    432    Lorg/json/JSONException;
        //  279    287    400    415    Ljava/lang/Exception;
        //  290    302    415    432    Lorg/json/JSONException;
        //  290    302    400    415    Ljava/lang/Exception;
        //  313    321    432    452    Lorg/json/JSONException;
        //  313    321    400    415    Ljava/lang/Exception;
        //  324    337    432    452    Lorg/json/JSONException;
        //  324    337    400    415    Ljava/lang/Exception;
        //  343    351    432    452    Lorg/json/JSONException;
        //  343    351    400    415    Ljava/lang/Exception;
        //  354    366    432    452    Lorg/json/JSONException;
        //  354    366    400    415    Ljava/lang/Exception;
        //  387    397    400    415    Ljava/lang/Exception;
        //  419    429    400    415    Ljava/lang/Exception;
        //  436    446    400    415    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 220, Size: 220
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
    
    private static Bundle toBundle(final JSONObject jsonObject) throws JSONException {
        final Bundle bundle = new Bundle();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final Object value = jsonObject.get(s);
            if (value instanceof JSONObject) {
                bundle.putBundle(s, toBundle((JSONObject)value));
            }
            else if (value instanceof JSONArray) {
                final JSONArray jsonArray = (JSONArray)value;
                if (jsonArray.length() == 0) {
                    bundle.putStringArray(s, new String[0]);
                }
                else {
                    final Object value2 = jsonArray.get(0);
                    if (value2 instanceof JSONObject) {
                        final Bundle[] array = new Bundle[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            array[i] = toBundle(jsonArray.getJSONObject(i));
                        }
                        bundle.putParcelableArray(s, (Parcelable[])array);
                    }
                    else {
                        if (value2 instanceof JSONArray) {
                            throw new FacebookException("Nested arrays are not supported.");
                        }
                        final String[] array2 = new String[jsonArray.length()];
                        for (int j = 0; j < jsonArray.length(); ++j) {
                            array2[j] = jsonArray.get(j).toString();
                        }
                        bundle.putStringArray(s, array2);
                    }
                }
            }
            else {
                bundle.putString(s, value.toString());
            }
        }
        return bundle;
    }
    
    public Bundle getArgumentBundle() {
        return this.argumentBundle;
    }
    
    @Deprecated
    public JSONObject getArguments() {
        return this.arguments;
    }
    
    public String getRef() {
        return this.ref;
    }
    
    public Bundle getRefererData() {
        if (this.argumentBundle != null) {
            return this.argumentBundle.getBundle("referer_data");
        }
        return null;
    }
    
    public Uri getTargetUri() {
        return this.targetUri;
    }
    
    public interface CompletionHandler
    {
        void onDeferredAppLinkDataFetched(final AppLinkData p0);
    }
}
