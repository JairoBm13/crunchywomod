// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Point;

abstract class SwrveWidget
{
    protected Point position;
    protected Point size;
    
    protected static Point getCenterFrom(final JSONObject jsonObject) throws JSONException {
        return new Point(jsonObject.getJSONObject("x").getInt("value"), jsonObject.getJSONObject("y").getInt("value"));
    }
    
    protected static Point getSizeFrom(final JSONObject jsonObject) throws JSONException {
        return new Point(jsonObject.getJSONObject("w").getInt("value"), jsonObject.getJSONObject("h").getInt("value"));
    }
    
    public Point getPosition() {
        return this.position;
    }
    
    protected void setPosition(final Point position) {
        this.position = position;
    }
    
    protected void setSize(final Point size) {
        this.size = size;
    }
}
