// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import android.graphics.Point;
import org.json.JSONException;
import org.json.JSONObject;

public class SwrveImage extends SwrveWidget
{
    protected String file;
    
    public SwrveImage(final JSONObject jsonObject) throws JSONException {
        this.setPosition(SwrveWidget.getCenterFrom(jsonObject));
        this.setSize(SwrveWidget.getSizeFrom(jsonObject));
        this.setFile(jsonObject.getJSONObject("image").getString("value"));
    }
    
    public String getFile() {
        return this.file;
    }
    
    protected void setFile(final String file) {
        this.file = file;
    }
}
