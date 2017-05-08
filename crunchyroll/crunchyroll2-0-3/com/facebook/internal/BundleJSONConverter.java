// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.internal;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

public class BundleJSONConverter
{
    private static final Map<Class<?>, Setter> SETTERS;
    
    static {
        (SETTERS = new HashMap<Class<?>, Setter>()).put(Boolean.class, new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                bundle.putBoolean(s, (boolean)o);
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                jsonObject.put(s, o);
            }
        });
        BundleJSONConverter.SETTERS.put(Integer.class, (Setter)new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                bundle.putInt(s, (int)o);
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                jsonObject.put(s, o);
            }
        });
        BundleJSONConverter.SETTERS.put(Long.class, (Setter)new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                bundle.putLong(s, (long)o);
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                jsonObject.put(s, o);
            }
        });
        BundleJSONConverter.SETTERS.put(Double.class, (Setter)new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                bundle.putDouble(s, (double)o);
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                jsonObject.put(s, o);
            }
        });
        BundleJSONConverter.SETTERS.put(String.class, (Setter)new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                bundle.putString(s, (String)o);
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                jsonObject.put(s, o);
            }
        });
        BundleJSONConverter.SETTERS.put(String[].class, (Setter)new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                throw new IllegalArgumentException("Unexpected type from JSON");
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                final JSONArray jsonArray = new JSONArray();
                final String[] array = (String[])o;
                for (int length = array.length, i = 0; i < length; ++i) {
                    jsonArray.put((Object)array[i]);
                }
                jsonObject.put(s, (Object)jsonArray);
            }
        });
        BundleJSONConverter.SETTERS.put(JSONArray.class, (Setter)new Setter() {
            @Override
            public void setOnBundle(final Bundle bundle, final String s, final Object o) throws JSONException {
                final JSONArray jsonArray = (JSONArray)o;
                final ArrayList<String> list = new ArrayList<String>();
                if (jsonArray.length() == 0) {
                    bundle.putStringArrayList(s, (ArrayList)list);
                    return;
                }
                for (int i = 0; i < jsonArray.length(); ++i) {
                    final Object value = jsonArray.get(i);
                    if (!(value instanceof String)) {
                        throw new IllegalArgumentException("Unexpected type in an array: " + ((String)value).getClass());
                    }
                    list.add((String)value);
                }
                bundle.putStringArrayList(s, (ArrayList)list);
            }
            
            @Override
            public void setOnJSON(final JSONObject jsonObject, final String s, final Object o) throws JSONException {
                throw new IllegalArgumentException("JSONArray's are not supported in bundles.");
            }
        });
    }
    
    public static Bundle convertToBundle(final JSONObject jsonObject) throws JSONException {
        final Bundle bundle = new Bundle();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final Object value = jsonObject.get(s);
            if (value != null && value != JSONObject.NULL) {
                if (value instanceof JSONObject) {
                    bundle.putBundle(s, convertToBundle((JSONObject)value));
                }
                else {
                    final Setter setter = BundleJSONConverter.SETTERS.get(((JSONObject)value).getClass());
                    if (setter == null) {
                        throw new IllegalArgumentException("Unsupported type: " + ((JSONObject)value).getClass());
                    }
                    setter.setOnBundle(bundle, s, value);
                }
            }
        }
        return bundle;
    }
    
    public static JSONObject convertToJSON(final Bundle bundle) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        for (final String s : bundle.keySet()) {
            final Object value = bundle.get(s);
            if (value != null) {
                if (value instanceof List) {
                    final JSONArray jsonArray = new JSONArray();
                    final Iterator<String> iterator2 = ((List<String>)value).iterator();
                    while (iterator2.hasNext()) {
                        jsonArray.put((Object)iterator2.next());
                    }
                    jsonObject.put(s, (Object)jsonArray);
                }
                else if (value instanceof Bundle) {
                    jsonObject.put(s, (Object)convertToJSON((Bundle)value));
                }
                else {
                    final Setter setter = BundleJSONConverter.SETTERS.get(((List<String>)value).getClass());
                    if (setter == null) {
                        throw new IllegalArgumentException("Unsupported type: " + ((List<String>)value).getClass());
                    }
                    setter.setOnJSON(jsonObject, s, value);
                }
            }
        }
        return jsonObject;
    }
    
    public interface Setter
    {
        void setOnBundle(final Bundle p0, final String p1, final Object p2) throws JSONException;
        
        void setOnJSON(final JSONObject p0, final String p1, final Object p2) throws JSONException;
    }
}
