// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import android.graphics.drawable.Drawable;
import org.json.JSONObject;
import com.swrve.sdk.conversations.engine.GsonHelper;
import com.swrve.sdk.conversations.engine.model.styles.PageStyle;
import java.util.ArrayList;
import java.io.Serializable;

public class ConversationPage implements Serializable
{
    private ArrayList<ConversationAtom> content;
    private ArrayList<ButtonControl> controls;
    private PageStyle style;
    private String tag;
    private String title;
    
    public static ConversationPage fromJSON(final String s) {
        return GsonHelper.getConfiguredGson().fromJson(s, ConversationPage.class);
    }
    
    public static ConversationPage fromJson(final JSONObject jsonObject) {
        return fromJSON(jsonObject.toString());
    }
    
    public Drawable getBackground() {
        return this.getStyle().getBg().getPrimaryDrawable();
    }
    
    public ArrayList<ConversationAtom> getContent() {
        return this.content;
    }
    
    public ArrayList<ButtonControl> getControls() {
        if (this.controls == null) {
            this.controls = new ArrayList<ButtonControl>();
        }
        return this.controls;
    }
    
    public PageStyle getStyle() {
        return this.style;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public boolean hasTag(final String s) {
        return this.getTag().equalsIgnoreCase(s);
    }
}
