// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class SwrveResourceManager
{
    protected Map<String, SwrveResource> resources;
    
    public SwrveResourceManager() {
        this.resources = new HashMap<String, SwrveResource>();
    }
    
    protected int _getAttributeAsInt(final String s, final String s2, final int n) {
        final SwrveResource resource = this.getResource(s);
        int attributeAsInt = n;
        if (resource != null) {
            attributeAsInt = resource.getAttributeAsInt(s2, n);
        }
        return attributeAsInt;
    }
    
    protected SwrveResource _getResource(final String s) {
        if (this.resources.containsKey(s)) {
            return this.resources.get(s);
        }
        return null;
    }
    
    protected void _setResourcesFromJSON(final JSONArray jsonArray) {
        try {
            final int length = jsonArray.length();
            synchronized (this.resources) {
                this.resources = new HashMap<String, SwrveResource>();
                for (int i = 0; i < length; ++i) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    this.resources.put(jsonObject.getString("uid"), new SwrveResource(SwrveHelper.JSONToMap(jsonObject)));
                }
            }
        }
        catch (JSONException ex) {
            Log.i("SwrveSDK", "Invalid JSON received for resources, resources not updated");
        }
    }
    
    public int getAttributeAsInt(final String s, final String s2, final int n) {
        try {
            return this._getAttributeAsInt(s, s2, n);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return n;
        }
    }
    
    public SwrveResource getResource(final String s) {
        try {
            return this._getResource(s);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return null;
        }
    }
    
    public void setResourcesFromJSON(final JSONArray jsonArray) {
        try {
            this._setResourcesFromJSON(jsonArray);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
        }
    }
}
