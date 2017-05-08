// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import com.swrve.sdk.config.SwrveConfigBase;
import org.json.JSONException;
import org.json.JSONArray;
import android.util.Log;
import android.graphics.Color;
import com.swrve.sdk.SwrveHelper;
import java.util.ArrayList;
import org.json.JSONObject;
import com.swrve.sdk.SwrveBase;
import android.graphics.Point;
import java.util.List;

public class SwrveMessageFormat
{
    protected int backgroundColor;
    protected List<SwrveButton> buttons;
    protected List<SwrveImage> images;
    protected String language;
    protected SwrveMessage message;
    protected String name;
    protected SwrveOrientation orientation;
    protected float scale;
    protected Point size;
    
    public SwrveMessageFormat(final SwrveBase<?, ?> swrveBase, final SwrveMessage message, final JSONObject jsonObject) throws JSONException {
        this.message = message;
        this.buttons = new ArrayList<SwrveButton>();
        this.images = new ArrayList<SwrveImage>();
        this.scale = 1.0f;
        this.setName(jsonObject.getString("name"));
        this.setLanguage(jsonObject.getString("language"));
        if (jsonObject.has("orientation")) {
            this.setOrientation(SwrveOrientation.parse(jsonObject.getString("orientation")));
        }
        if (jsonObject.has("scale")) {
            this.setScale(Float.parseFloat(jsonObject.getString("scale")));
        }
        this.setBackgroundColor(((SwrveConfigBase)swrveBase.getConfig()).getDefaultBackgroundColor());
        if (jsonObject.has("color")) {
            final String string = jsonObject.getString("color");
            if (!SwrveHelper.isNullOrEmpty(string)) {
                this.setBackgroundColor(Color.parseColor("#" + string));
            }
        }
        this.setSize(getSizeFrom(jsonObject.getJSONObject("size")));
        Log.i("SwrveMessagingSDK", "Format " + this.getName() + " Size: " + this.size.x + "x" + this.size.y + " scale " + this.scale);
        final JSONArray jsonArray = jsonObject.getJSONArray("buttons");
        for (int i = 0; i < jsonArray.length(); ++i) {
            this.getButtons().add(new SwrveButton(message, jsonArray.getJSONObject(i)));
        }
        final JSONArray jsonArray2 = jsonObject.getJSONArray("images");
        for (int j = 0; j < jsonArray2.length(); ++j) {
            this.getImages().add(new SwrveImage(jsonArray2.getJSONObject(j)));
        }
    }
    
    protected static Point getSizeFrom(final JSONObject jsonObject) throws JSONException {
        return new Point(jsonObject.getJSONObject("w").getInt("value"), jsonObject.getJSONObject("h").getInt("value"));
    }
    
    public int getBackgroundColor() {
        return this.backgroundColor;
    }
    
    public List<SwrveButton> getButtons() {
        return this.buttons;
    }
    
    public List<SwrveImage> getImages() {
        return this.images;
    }
    
    public SwrveMessage getMessage() {
        return this.message;
    }
    
    public String getName() {
        return this.name;
    }
    
    public SwrveOrientation getOrientation() {
        return this.orientation;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public Point getSize() {
        return this.size;
    }
    
    protected void setBackgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    protected void setLanguage(final String language) {
        this.language = language;
    }
    
    protected void setName(final String name) {
        this.name = name;
    }
    
    protected void setOrientation(final SwrveOrientation orientation) {
        this.orientation = orientation;
    }
    
    protected void setScale(final float scale) {
        this.scale = scale;
    }
    
    protected void setSize(final Point size) {
        this.size = size;
    }
}
