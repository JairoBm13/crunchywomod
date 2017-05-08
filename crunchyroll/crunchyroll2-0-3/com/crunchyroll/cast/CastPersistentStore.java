// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.cast;

import android.content.SharedPreferences;
import android.util.Log;
import android.content.Context;

public class CastPersistentStore
{
    private static final String CAST_SHARED_STORE = "CAST_SHARED_STORE";
    private static final String ROUTE_ID_KEY = "ROUTE_ID_KEY";
    private static final String SESSION_ID_KEY = "SESSION_ID_KEY";
    private static final String TAG;
    
    static {
        TAG = CastPersistentStore.class.getName();
    }
    
    public static String getRouteId(final Context context) {
        return getString(context, "ROUTE_ID_KEY");
    }
    
    public static String getSessionId(final Context context) {
        return getString(context, "SESSION_ID_KEY");
    }
    
    private static String getString(final Context context, final String s) {
        return sharedPrefs(context).getString(s, (String)null);
    }
    
    public static void setRouteId(final Context context, final String s) {
        setString(context, "ROUTE_ID_KEY", s);
    }
    
    public static void setSessionId(final Context context, final String s) {
        setString(context, "SESSION_ID_KEY", s);
    }
    
    private static void setString(final Context context, final String s, final String s2) {
        Log.d(CastPersistentStore.TAG, "Set " + s + "=" + s2);
        sharedPrefs(context).edit().putString(s, s2).commit();
    }
    
    private static SharedPreferences sharedPrefs(final Context context) {
        return context.getSharedPreferences("CAST_SHARED_STORE", 0);
    }
}
