// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.text;

import android.util.TypedValue;
import android.content.res.Resources;
import android.graphics.Canvas;
import java.util.ArrayList;
import android.util.AttributeSet;
import android.content.Context;
import java.util.List;
import android.view.View;

public final class SubtitleLayout extends View
{
    private boolean applyEmbeddedStyles;
    private float bottomPaddingFraction;
    private List<Cue> cues;
    private final List<CuePainter> painters;
    private CaptionStyleCompat style;
    private float textSize;
    private int textSizeType;
    
    public SubtitleLayout(final Context context) {
        this(context, null);
    }
    
    public SubtitleLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.painters = new ArrayList<CuePainter>();
        this.textSizeType = 0;
        this.textSize = 0.0533f;
        this.applyEmbeddedStyles = true;
        this.style = CaptionStyleCompat.DEFAULT;
        this.bottomPaddingFraction = 0.08f;
    }
    
    private void setTextSize(final int textSizeType, final float textSize) {
        if (this.textSizeType == textSizeType && this.textSize == textSize) {
            return;
        }
        this.textSizeType = textSizeType;
        this.textSize = textSize;
        this.invalidate();
    }
    
    public void dispatchDraw(final Canvas canvas) {
        int size;
        if (this.cues == null) {
            size = 0;
        }
        else {
            size = this.cues.size();
        }
        final int top = this.getTop();
        final int bottom = this.getBottom();
        final int n = this.getLeft() + this.getPaddingLeft();
        final int n2 = top + this.getPaddingTop();
        final int n3 = this.getRight() + this.getPaddingRight();
        final int n4 = bottom - this.getPaddingBottom();
        if (n4 > n2 && n3 > n) {
            float textSize;
            if (this.textSizeType == 2) {
                textSize = this.textSize;
            }
            else {
                final float textSize2 = this.textSize;
                int n5;
                if (this.textSizeType == 0) {
                    n5 = n4 - n2;
                }
                else {
                    n5 = bottom - top;
                }
                textSize = textSize2 * n5;
            }
            if (textSize > 0.0f) {
                for (int i = 0; i < size; ++i) {
                    this.painters.get(i).draw(this.cues.get(i), this.applyEmbeddedStyles, this.style, textSize, this.bottomPaddingFraction, canvas, n, n2, n3, n4);
                }
            }
        }
    }
    
    public void setApplyEmbeddedStyles(final boolean applyEmbeddedStyles) {
        if (this.applyEmbeddedStyles == applyEmbeddedStyles) {
            return;
        }
        this.applyEmbeddedStyles = applyEmbeddedStyles;
        this.invalidate();
    }
    
    public void setBottomPaddingFraction(final float bottomPaddingFraction) {
        if (this.bottomPaddingFraction == bottomPaddingFraction) {
            return;
        }
        this.bottomPaddingFraction = bottomPaddingFraction;
        this.invalidate();
    }
    
    public void setCues(final List<Cue> cues) {
        if (this.cues == cues) {
            return;
        }
        int size;
        if ((this.cues = cues) == null) {
            size = 0;
        }
        else {
            size = cues.size();
        }
        while (this.painters.size() < size) {
            this.painters.add(new CuePainter(this.getContext()));
        }
        this.invalidate();
    }
    
    public void setFixedTextSize(final int n, final float n2) {
        final Context context = this.getContext();
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        }
        else {
            resources = context.getResources();
        }
        this.setTextSize(2, TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
    }
    
    public void setFractionalTextSize(final float n) {
        this.setFractionalTextSize(n, false);
    }
    
    public void setFractionalTextSize(final float n, final boolean b) {
        int n2;
        if (b) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        this.setTextSize(n2, n);
    }
    
    public void setStyle(final CaptionStyleCompat style) {
        if (this.style == style) {
            return;
        }
        this.style = style;
        this.invalidate();
    }
}
