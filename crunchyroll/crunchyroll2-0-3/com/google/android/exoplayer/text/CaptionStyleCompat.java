// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.text;

import android.graphics.Typeface;

public final class CaptionStyleCompat
{
    public static final CaptionStyleCompat DEFAULT;
    public final int backgroundColor;
    public final int edgeColor;
    public final int edgeType;
    public final int foregroundColor;
    public final Typeface typeface;
    public final int windowColor;
    
    static {
        DEFAULT = new CaptionStyleCompat(-1, -16777216, 0, 0, -1, null);
    }
    
    public CaptionStyleCompat(final int foregroundColor, final int backgroundColor, final int windowColor, final int edgeType, final int edgeColor, final Typeface typeface) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.windowColor = windowColor;
        this.edgeType = edgeType;
        this.edgeColor = edgeColor;
        this.typeface = typeface;
    }
}
