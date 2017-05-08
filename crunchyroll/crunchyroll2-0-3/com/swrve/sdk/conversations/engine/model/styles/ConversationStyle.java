// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model.styles;

import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import java.io.Serializable;

public class ConversationStyle implements Serializable
{
    private String type;
    private String value;
    
    public Drawable getPrimaryDrawable() {
        if (this.isTypeColor()) {
            return (Drawable)new ColorDrawable(Color.parseColor(this.value));
        }
        if (this.isTypeTransparent()) {
            final ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
            colorDrawable.setAlpha(0);
            return (Drawable)colorDrawable;
        }
        return null;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean isTypeColor() {
        return "color".equalsIgnoreCase(this.type);
    }
    
    public boolean isTypeTransparent() {
        return "transparent".equalsIgnoreCase(this.type);
    }
}
