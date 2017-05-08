// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import android.database.Cursor;
import android.util.Log;
import java.lang.reflect.Method;
import com.facebook.FacebookException;
import android.os.Looper;
import android.content.Context;
import android.net.Uri;

public class AttributionIdentifiers
{
    private static final String ANDROID_ID_COLUMN_NAME = "androidid";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final Uri ATTRIBUTION_ID_CONTENT_URI;
    private static final int CONNECTION_RESULT_SUCCESS = 0;
    private static final long IDENTIFIER_REFRESH_INTERVAL_MILLIS = 3600000L;
    private static final String LIMIT_TRACKING_COLUMN_NAME = "limit_tracking";
    private static final String TAG;
    private static AttributionIdentifiers recentlyFetchedIdentifiers;
    private String androidAdvertiserId;
    private String attributionId;
    private long fetchTime;
    private boolean limitTracking;
    
    static {
        TAG = AttributionIdentifiers.class.getCanonicalName();
        ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    }
    
    private static AttributionIdentifiers getAndroidId(final Context context) {
        final AttributionIdentifiers attributionIdentifiers = new AttributionIdentifiers();
        Label_0036: {
            try {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    throw new FacebookException("getAndroidId cannot be called on the main thread.");
                }
                break Label_0036;
            }
            catch (Exception ex) {
                Utility.logd("android_id", ex);
            }
            return attributionIdentifiers;
        }
        final Method methodQuietly = Utility.getMethodQuietly("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", Context.class);
        if (methodQuietly == null) {
            return attributionIdentifiers;
        }
        final Object invokeMethodQuietly = Utility.invokeMethodQuietly(null, methodQuietly, context);
        if (!(invokeMethodQuietly instanceof Integer) || (int)invokeMethodQuietly != 0) {
            return attributionIdentifiers;
        }
        final Method methodQuietly2 = Utility.getMethodQuietly("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", Context.class);
        if (methodQuietly2 == null) {
            return attributionIdentifiers;
        }
        final Object invokeMethodQuietly2 = Utility.invokeMethodQuietly(null, methodQuietly2, context);
        if (invokeMethodQuietly2 == null) {
            return attributionIdentifiers;
        }
        final Method methodQuietly3 = Utility.getMethodQuietly(invokeMethodQuietly2.getClass(), "getId", (Class<?>[])new Class[0]);
        final Method methodQuietly4 = Utility.getMethodQuietly(invokeMethodQuietly2.getClass(), "isLimitAdTrackingEnabled", (Class<?>[])new Class[0]);
        if (methodQuietly3 != null && methodQuietly4 != null) {
            attributionIdentifiers.androidAdvertiserId = (String)Utility.invokeMethodQuietly(invokeMethodQuietly2, methodQuietly3, new Object[0]);
            attributionIdentifiers.limitTracking = (boolean)Utility.invokeMethodQuietly(invokeMethodQuietly2, methodQuietly4, new Object[0]);
            return attributionIdentifiers;
        }
        return attributionIdentifiers;
    }
    
    public static AttributionIdentifiers getAttributionIdentifiers(final Context context) {
        if (AttributionIdentifiers.recentlyFetchedIdentifiers != null && System.currentTimeMillis() - AttributionIdentifiers.recentlyFetchedIdentifiers.fetchTime < 3600000L) {
            final Object recentlyFetchedIdentifiers = AttributionIdentifiers.recentlyFetchedIdentifiers;
            return (AttributionIdentifiers)recentlyFetchedIdentifiers;
        }
        final AttributionIdentifiers androidId = getAndroidId(context);
        Object o = null;
        Object recentlyFetchedIdentifiers = null;
        try {
            final Cursor query = context.getContentResolver().query(AttributionIdentifiers.ATTRIBUTION_ID_CONTENT_URI, new String[] { "aid", "androidid", "limit_tracking" }, (String)null, (String[])null, (String)null);
            if (query != null) {
                recentlyFetchedIdentifiers = query;
                o = query;
                if (query.moveToFirst()) {
                    recentlyFetchedIdentifiers = query;
                    o = query;
                    final int columnIndex = query.getColumnIndex("aid");
                    recentlyFetchedIdentifiers = query;
                    o = query;
                    final int columnIndex2 = query.getColumnIndex("androidid");
                    recentlyFetchedIdentifiers = query;
                    o = query;
                    final int columnIndex3 = query.getColumnIndex("limit_tracking");
                    recentlyFetchedIdentifiers = query;
                    o = query;
                    androidId.attributionId = query.getString(columnIndex);
                    if (columnIndex2 > 0 && columnIndex3 > 0) {
                        recentlyFetchedIdentifiers = query;
                        o = query;
                        if (androidId.getAndroidAdvertiserId() == null) {
                            recentlyFetchedIdentifiers = query;
                            o = query;
                            androidId.androidAdvertiserId = query.getString(columnIndex2);
                            recentlyFetchedIdentifiers = query;
                            o = query;
                            androidId.limitTracking = Boolean.parseBoolean(query.getString(columnIndex3));
                        }
                    }
                    if (query != null) {
                        query.close();
                    }
                    androidId.fetchTime = System.currentTimeMillis();
                    return AttributionIdentifiers.recentlyFetchedIdentifiers = androidId;
                }
            }
            recentlyFetchedIdentifiers = androidId;
            return androidId;
        }
        catch (Exception ex) {
            o = recentlyFetchedIdentifiers;
            Log.d(AttributionIdentifiers.TAG, "Caught unexpected exception in getAttributionId(): " + ex.toString());
            return null;
        }
        finally {
            if (o != null) {
                ((Cursor)o).close();
            }
        }
    }
    
    public String getAndroidAdvertiserId() {
        return this.androidAdvertiserId;
    }
    
    public String getAttributionId() {
        return this.attributionId;
    }
    
    public boolean isTrackingLimited() {
        return this.limitTracking;
    }
}
