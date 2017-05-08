// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk;

import android.util.Log;
import java.util.Map;

public class SwrveResource
{
    protected Map<String, String> attributes;
    
    public SwrveResource(final Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    protected int _getAttributeAsInt(final String s, final int n) {
        int int1 = n;
        if (!this.attributes.containsKey(s)) {
            return int1;
        }
        try {
            int1 = Integer.parseInt(this.attributes.get(s));
            return int1;
        }
        catch (NumberFormatException ex) {
            Log.e("SwrveSDK", "Could not retrieve attribute " + s + " as integer value, returning default value instead");
            return n;
        }
    }
    
    public int getAttributeAsInt(final String s, final int n) {
        try {
            return this._getAttributeAsInt(s, n);
        }
        catch (Exception ex) {
            Log.e("SwrveSDK", "Exception thrown in Swrve SDK", (Throwable)ex);
            return n;
        }
    }
}
