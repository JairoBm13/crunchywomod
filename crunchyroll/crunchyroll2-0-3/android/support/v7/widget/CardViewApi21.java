// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.content.Context;
import android.view.View;

class CardViewApi21 implements CardViewImpl
{
    @Override
    public float getElevation(final CardViewDelegate cardViewDelegate) {
        return ((View)cardViewDelegate).getElevation();
    }
    
    @Override
    public float getMaxElevation(final CardViewDelegate cardViewDelegate) {
        return ((RoundRectDrawable)cardViewDelegate.getBackground()).getPadding();
    }
    
    @Override
    public float getMinHeight(final CardViewDelegate cardViewDelegate) {
        return this.getRadius(cardViewDelegate) * 2.0f;
    }
    
    @Override
    public float getMinWidth(final CardViewDelegate cardViewDelegate) {
        return this.getRadius(cardViewDelegate) * 2.0f;
    }
    
    @Override
    public float getRadius(final CardViewDelegate cardViewDelegate) {
        return ((RoundRectDrawable)cardViewDelegate.getBackground()).getRadius();
    }
    
    @Override
    public void initStatic() {
    }
    
    @Override
    public void initialize(final CardViewDelegate cardViewDelegate, final Context context, final int n, final float n2, final float elevation, final float n3) {
        cardViewDelegate.setBackgroundDrawable(new RoundRectDrawable(n, n2));
        final View view = (View)cardViewDelegate;
        view.setClipToOutline(true);
        view.setElevation(elevation);
        this.setMaxElevation(cardViewDelegate, n3);
    }
    
    @Override
    public void onCompatPaddingChanged(final CardViewDelegate cardViewDelegate) {
        this.setMaxElevation(cardViewDelegate, this.getMaxElevation(cardViewDelegate));
    }
    
    @Override
    public void onPreventCornerOverlapChanged(final CardViewDelegate cardViewDelegate) {
        this.setMaxElevation(cardViewDelegate, this.getMaxElevation(cardViewDelegate));
    }
    
    @Override
    public void setBackgroundColor(final CardViewDelegate cardViewDelegate, final int color) {
        ((RoundRectDrawable)cardViewDelegate.getBackground()).setColor(color);
    }
    
    @Override
    public void setElevation(final CardViewDelegate cardViewDelegate, final float elevation) {
        ((View)cardViewDelegate).setElevation(elevation);
    }
    
    @Override
    public void setMaxElevation(final CardViewDelegate cardViewDelegate, final float n) {
        ((RoundRectDrawable)cardViewDelegate.getBackground()).setPadding(n, cardViewDelegate.getUseCompatPadding(), cardViewDelegate.getPreventCornerOverlap());
        this.updatePadding(cardViewDelegate);
    }
    
    @Override
    public void setRadius(final CardViewDelegate cardViewDelegate, final float radius) {
        ((RoundRectDrawable)cardViewDelegate.getBackground()).setRadius(radius);
    }
    
    @Override
    public void updatePadding(final CardViewDelegate cardViewDelegate) {
        if (!cardViewDelegate.getUseCompatPadding()) {
            cardViewDelegate.setShadowPadding(0, 0, 0, 0);
            return;
        }
        final float maxElevation = this.getMaxElevation(cardViewDelegate);
        final float radius = this.getRadius(cardViewDelegate);
        final int n = (int)Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(maxElevation, radius, cardViewDelegate.getPreventCornerOverlap()));
        final int n2 = (int)Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(maxElevation, radius, cardViewDelegate.getPreventCornerOverlap()));
        cardViewDelegate.setShadowPadding(n, n2, n, n2);
    }
}
