// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model.styles;

import android.graphics.Color;
import java.io.Serializable;

public class AtomStyle implements Serializable
{
    private BackgroundStyle bg;
    private ForegroundStyle fg;
    public String type;
    
    public BackgroundStyle getBg() {
        return this.bg;
    }
    
    public int getBgColorInt() {
        if (this.getBg().isTypeColor()) {
            return Color.parseColor(this.getBg().getValue());
        }
        return 0;
    }
    
    public ForegroundStyle getFg() {
        return this.fg;
    }
    
    public int getTextColorInt() {
        return Color.parseColor(this.getFg().getValue());
    }
    
    public boolean isOutlineStyle() {
        return this.type.equalsIgnoreCase("outline");
    }
    
    public boolean isSolidStyle() {
        return this.type.equalsIgnoreCase("solid");
    }
}
