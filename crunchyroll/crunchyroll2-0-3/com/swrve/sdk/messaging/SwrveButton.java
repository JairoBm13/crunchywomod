// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import android.graphics.Point;
import org.json.JSONException;
import org.json.JSONObject;

public class SwrveButton extends SwrveWidget
{
    protected String action;
    protected SwrveActionType actionType;
    protected int appId;
    protected String image;
    protected SwrveMessage message;
    protected String name;
    
    public SwrveButton() {
    }
    
    public SwrveButton(final SwrveMessage message, final JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("name")) {
            this.setName(jsonObject.getString("name"));
        }
        this.setPosition(SwrveWidget.getCenterFrom(jsonObject));
        this.setSize(SwrveWidget.getSizeFrom(jsonObject));
        this.setImage(jsonObject.getJSONObject("image_up").getString("value"));
        this.setMessage(message);
        if (jsonObject.has("game_id")) {
            final String string = jsonObject.getJSONObject("game_id").getString("value");
            if (string != null && !string.equals("")) {
                this.setAppId(Integer.parseInt(string));
            }
        }
        this.setAction(jsonObject.getJSONObject("action").getString("value"));
        this.setActionType(SwrveActionType.parse(jsonObject.getJSONObject("type").getString("value")));
    }
    
    public String getAction() {
        return this.action;
    }
    
    public SwrveActionType getActionType() {
        return this.actionType;
    }
    
    public int getAppId() {
        return this.appId;
    }
    
    public String getImage() {
        return this.image;
    }
    
    public SwrveMessage getMessage() {
        return this.message;
    }
    
    public String getName() {
        return this.name;
    }
    
    protected void setAction(final String action) {
        this.action = action;
    }
    
    protected void setActionType(final SwrveActionType actionType) {
        this.actionType = actionType;
    }
    
    protected void setAppId(final int appId) {
        this.appId = appId;
    }
    
    protected void setImage(final String image) {
        this.image = image;
    }
    
    protected void setMessage(final SwrveMessage message) {
        this.message = message;
    }
    
    protected void setName(final String name) {
        this.name = name;
    }
}
