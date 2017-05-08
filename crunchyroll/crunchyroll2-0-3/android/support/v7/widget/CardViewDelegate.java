// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;

interface CardViewDelegate
{
    Drawable getBackground();
    
    boolean getPreventCornerOverlap();
    
    float getRadius();
    
    boolean getUseCompatPadding();
    
    void setBackgroundDrawable(final Drawable p0);
    
    void setShadowPadding(final int p0, final int p1, final int p2, final int p3);
}
