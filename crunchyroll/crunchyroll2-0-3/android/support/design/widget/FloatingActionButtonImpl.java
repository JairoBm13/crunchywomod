// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import android.content.res.Resources;
import android.graphics.PorterDuff$Mode;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.design.R;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.view.View;

abstract class FloatingActionButtonImpl
{
    static final int[] EMPTY_STATE_SET;
    static final int[] FOCUSED_ENABLED_STATE_SET;
    static final int[] PRESSED_ENABLED_STATE_SET;
    final ShadowViewDelegate mShadowViewDelegate;
    final View mView;
    
    static {
        PRESSED_ENABLED_STATE_SET = new int[] { 16842919, 16842910 };
        FOCUSED_ENABLED_STATE_SET = new int[] { 16842908, 16842910 };
        EMPTY_STATE_SET = new int[0];
    }
    
    FloatingActionButtonImpl(final View mView, final ShadowViewDelegate mShadowViewDelegate) {
        this.mView = mView;
        this.mShadowViewDelegate = mShadowViewDelegate;
    }
    
    Drawable createBorderDrawable(final int n, final ColorStateList list) {
        final Resources resources = this.mView.getResources();
        final CircularBorderDrawable circularDrawable = this.newCircularDrawable();
        circularDrawable.setGradientColors(resources.getColor(R.color.fab_stroke_top_outer_color), resources.getColor(R.color.fab_stroke_top_inner_color), resources.getColor(R.color.fab_stroke_end_inner_color), resources.getColor(R.color.fab_stroke_end_outer_color));
        circularDrawable.setBorderWidth(n);
        final Drawable wrap = DrawableCompat.wrap(circularDrawable);
        DrawableCompat.setTintList(wrap, list);
        DrawableCompat.setTintMode(wrap, PorterDuff$Mode.DST_OVER);
        return wrap;
    }
    
    abstract void jumpDrawableToCurrentState();
    
    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }
    
    abstract void onDrawableStateChanged(final int[] p0);
    
    abstract void setBackgroundDrawable(final Drawable p0, final ColorStateList p1, final PorterDuff$Mode p2, final int p3, final int p4);
    
    abstract void setBackgroundTintList(final ColorStateList p0);
    
    abstract void setBackgroundTintMode(final PorterDuff$Mode p0);
    
    abstract void setElevation(final float p0);
    
    abstract void setPressedTranslationZ(final float p0);
    
    abstract void setRippleColor(final int p0);
}
