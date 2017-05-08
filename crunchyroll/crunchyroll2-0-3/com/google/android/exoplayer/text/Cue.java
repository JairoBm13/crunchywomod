// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.text;

import android.text.Layout$Alignment;

public class Cue
{
    public final float line;
    public final int lineAnchor;
    public final int lineType;
    public final float position;
    public final int positionAnchor;
    public final float size;
    public final CharSequence text;
    public final Layout$Alignment textAlignment;
    
    public Cue() {
        this(null);
    }
    
    public Cue(final CharSequence charSequence) {
        this(charSequence, null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }
    
    public Cue(final CharSequence text, final Layout$Alignment textAlignment, final float line, final int lineType, final int lineAnchor, final float position, final int positionAnchor, final float size) {
        this.text = text;
        this.textAlignment = textAlignment;
        this.line = line;
        this.lineType = lineType;
        this.lineAnchor = lineAnchor;
        this.position = position;
        this.positionAnchor = positionAnchor;
        this.size = size;
    }
}
