// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import org.json.JSONArray;
import java.util.Random;
import java.util.LinkedHashMap;
import org.json.JSONException;
import java.util.Iterator;
import org.json.JSONObject;
import com.swrve.sdk.localstorage.MemoryCachedLocalStorage;
import java.util.Map;

final class EventHelper
{
    private static final Object BATCH_API_VERSION;
    
    static {
        BATCH_API_VERSION = "2";
    }
    
    public static String eventAsJSON(final String s, final Map<String, Object> map, final Map<String, String> map2, final MemoryCachedLocalStorage memoryCachedLocalStorage) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", (Object)s);
        jsonObject.put("time", System.currentTimeMillis());
        jsonObject.put("seqnum", getNextSequenceNumber(memoryCachedLocalStorage));
        if (map != null) {
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                jsonObject.put((String)entry.getKey(), entry.getValue());
            }
        }
        if (map2 != null) {
            jsonObject.put("payload", (Object)new JSONObject((Map)map2));
        }
        return jsonObject.toString();
    }
    
    public static String eventsAsBatch(final String s, final String s2, final String s3, final LinkedHashMap<Long, String> linkedHashMap, final MemoryCachedLocalStorage memoryCachedLocalStorage) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", (Object)s);
        jsonObject.put("session_token", (Object)s3);
        jsonObject.put("version", EventHelper.BATCH_API_VERSION);
        jsonObject.put("app_version", (Object)s2);
        jsonObject.put("device_id", (int)getDeviceId(memoryCachedLocalStorage));
        jsonObject.put("data", (Object)orderedMapToJSONArray(linkedHashMap));
        return jsonObject.toString();
    }
    
    private static short getDeviceId(final MemoryCachedLocalStorage memoryCachedLocalStorage) {
        synchronized (EventHelper.class) {
            final String sharedCacheEntry = memoryCachedLocalStorage.getSharedCacheEntry("device_id");
            short short1;
            if (sharedCacheEntry == null || sharedCacheEntry.length() <= 0) {
                short1 = (short)new Random().nextInt(32767);
                memoryCachedLocalStorage.setAndFlushSharedEntry("device_id", Short.toString(short1));
            }
            else {
                short1 = Short.parseShort(sharedCacheEntry);
            }
            return short1;
        }
    }
    
    public static String getEventName(final String s, final Map<String, Object> map) {
        final String s2 = "";
        String s3;
        if (s.equals("session_start")) {
            s3 = "Swrve.session.start";
        }
        else {
            if (s.equals("session_end")) {
                return "Swrve.session.end";
            }
            if (s.equals("buy_in")) {
                return "Swrve.buy_in";
            }
            if (s.equals("iap")) {
                return "Swrve.iap";
            }
            if (s.equals("event")) {
                return map.get("name");
            }
            if (s.equals("purchase")) {
                return "Swrve.user_purchase";
            }
            if (s.equals("currency_given")) {
                return "Swrve.currency_given";
            }
            s3 = s2;
            if (s.equals("user")) {
                return "Swrve.user_properties_changed";
            }
        }
        return s3;
    }
    
    private static int getNextSequenceNumber(final MemoryCachedLocalStorage memoryCachedLocalStorage) {
        synchronized (EventHelper.class) {
            final String sharedCacheEntry = memoryCachedLocalStorage.getSharedCacheEntry("seqnum");
            int n = 1;
            if (!SwrveHelper.isNullOrEmpty(sharedCacheEntry)) {
                n = Integer.parseInt(sharedCacheEntry) + 1;
            }
            memoryCachedLocalStorage.setAndFlushSharedEntry("seqnum", Integer.toString(n));
            return n;
        }
    }
    
    private static JSONArray orderedMapToJSONArray(final LinkedHashMap<Long, String> linkedHashMap) throws JSONException {
        final JSONArray jsonArray = new JSONArray();
        final Iterator<Map.Entry<Long, String>> iterator = linkedHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            jsonArray.put((Object)new JSONObject((String)iterator.next().getValue()));
        }
        return jsonArray;
    }
}
