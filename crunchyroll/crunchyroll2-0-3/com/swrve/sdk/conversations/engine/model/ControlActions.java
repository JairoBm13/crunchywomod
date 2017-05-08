// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import android.net.Uri;
import java.util.HashMap;
import java.io.Serializable;

public class ControlActions implements Serializable
{
    public static final Object CALL_ACTION;
    public static final Object DEEPLINK_ACTION;
    public static final Object VISIT_URL_ACTION;
    private HashMap<String, Object> actionItems;
    
    static {
        CALL_ACTION = "call";
        VISIT_URL_ACTION = "visit";
        DEEPLINK_ACTION = "deeplink";
    }
    
    public ControlActions() {
        this.actionItems = new HashMap<String, Object>();
    }
    
    public Uri getCallUri() {
        return Uri.parse("tel:" + this.actionItems.get(ControlActions.CALL_ACTION).toString());
    }
    
    public HashMap<String, String> getDeepLinkDetails() {
        return this.actionItems.get(ControlActions.DEEPLINK_ACTION);
    }
    
    public HashMap<String, String> getVisitDetails() {
        return this.actionItems.get(ControlActions.VISIT_URL_ACTION);
    }
    
    public void includeAction(final String s, final Object o) {
        this.actionItems.put(s, o);
    }
    
    public boolean isCall() {
        return this.actionItems.containsKey(ControlActions.CALL_ACTION);
    }
    
    public boolean isDeepLink() {
        return this.actionItems.containsKey(ControlActions.DEEPLINK_ACTION);
    }
    
    public boolean isVisit() {
        return this.actionItems.containsKey(ControlActions.VISIT_URL_ACTION);
    }
}
