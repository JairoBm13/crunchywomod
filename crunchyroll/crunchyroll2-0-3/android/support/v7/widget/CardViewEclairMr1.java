// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.View;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.RectF;

class CardViewEclairMr1 implements CardViewImpl
{
    final RectF sCornerRect;
    
    CardViewEclairMr1() {
        this.sCornerRect = new RectF();
    }
    
    private RoundRectDrawableWithShadow getShadowBackground(final CardViewDelegate cardViewDelegate) {
        return (RoundRectDrawableWithShadow)cardViewDelegate.getBackground();
    }
    
    RoundRectDrawableWithShadow createBackground(final Context context, final int n, final float n2, final float n3, final float n4) {
        return new RoundRectDrawableWithShadow(context.getResources(), n, n2, n3, n4);
    }
    
    @Override
    public float getElevation(final CardViewDelegate cardViewDelegate) {
        return this.getShadowBackground(cardViewDelegate).getShadowSize();
    }
    
    @Override
    public float getMaxElevation(final CardViewDelegate cardViewDelegate) {
        return this.getShadowBackground(cardViewDelegate).getMaxShadowSize();
    }
    
    @Override
    public float getMinHeight(final CardViewDelegate cardViewDelegate) {
        return this.getShadowBackground(cardViewDelegate).getMinHeight();
    }
    
    @Override
    public float getMinWidth(final CardViewDelegate cardViewDelegate) {
        return this.getShadowBackground(cardViewDelegate).getMinWidth();
    }
    
    @Override
    public float getRadius(final CardViewDelegate cardViewDelegate) {
        return this.getShadowBackground(cardViewDelegate).getCornerRadius();
    }
    
    @Override
    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = (RoundRectDrawableWithShadow.RoundRectHelper)new RoundRectDrawableWithShadow.RoundRectHelper() {
            @Override
            public void drawRoundRect(final Canvas canvas, final RectF rectF, float left, final Paint paint) {
                final float n = left * 2.0f;
                final float n2 = rectF.width() - n - 1.0f;
                final float height = rectF.height();
                float n3 = left;
                if (left >= 1.0f) {
                    n3 = left + 0.5f;
                    CardViewEclairMr1.this.sCornerRect.set(-n3, -n3, n3, n3);
                    final int save = canvas.save();
                    canvas.translate(rectF.left + n3, rectF.top + n3);
                    canvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint);
                    canvas.translate(n2, 0.0f);
                    canvas.rotate(90.0f);
                    canvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint);
                    canvas.translate(height - n - 1.0f, 0.0f);
                    canvas.rotate(90.0f);
                    canvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint);
                    canvas.translate(n2, 0.0f);
                    canvas.rotate(90.0f);
                    canvas.drawArc(CardViewEclairMr1.this.sCornerRect, 180.0f, 90.0f, true, paint);
                    canvas.restoreToCount(save);
                    canvas.drawRect(rectF.left + n3 - 1.0f, rectF.top, 1.0f + (rectF.right - n3), rectF.top + n3, paint);
                    canvas.drawRect(rectF.left + n3 - 1.0f, 1.0f + (rectF.bottom - n3), 1.0f + (rectF.right - n3), rectF.bottom, paint);
                }
                left = rectF.left;
                canvas.drawRect(left, Math.max(0.0f, n3 - 1.0f) + rectF.top, rectF.right, 1.0f + (rectF.bottom - n3), paint);
            }
        };
    }
    
    @Override
    public void initialize(final CardViewDelegate cardViewDelegate, final Context context, final int n, final float n2, final float n3, final float n4) {
        final RoundRectDrawableWithShadow background = this.createBackground(context, n, n2, n3, n4);
        background.setAddPaddingForCorners(cardViewDelegate.getPreventCornerOverlap());
        cardViewDelegate.setBackgroundDrawable(background);
        this.updatePadding(cardViewDelegate);
    }
    
    @Override
    public void onCompatPaddingChanged(final CardViewDelegate cardViewDelegate) {
    }
    
    @Override
    public void onPreventCornerOverlapChanged(final CardViewDelegate cardViewDelegate) {
        this.getShadowBackground(cardViewDelegate).setAddPaddingForCorners(cardViewDelegate.getPreventCornerOverlap());
        this.updatePadding(cardViewDelegate);
    }
    
    @Override
    public void setBackgroundColor(final CardViewDelegate cardViewDelegate, final int color) {
        this.getShadowBackground(cardViewDelegate).setColor(color);
    }
    
    @Override
    public void setElevation(final CardViewDelegate cardViewDelegate, final float shadowSize) {
        this.getShadowBackground(cardViewDelegate).setShadowSize(shadowSize);
    }
    
    @Override
    public void setMaxElevation(final CardViewDelegate cardViewDelegate, final float maxShadowSize) {
        this.getShadowBackground(cardViewDelegate).setMaxShadowSize(maxShadowSize);
        this.updatePadding(cardViewDelegate);
    }
    
    @Override
    public void setRadius(final CardViewDelegate cardViewDelegate, final float cornerRadius) {
        this.getShadowBackground(cardViewDelegate).setCornerRadius(cornerRadius);
        this.updatePadding(cardViewDelegate);
    }
    
    @Override
    public void updatePadding(final CardViewDelegate cardViewDelegate) {
        final Rect rect = new Rect();
        this.getShadowBackground(cardViewDelegate).getMaxShadowAndCornerPadding(rect);
        ((View)cardViewDelegate).setMinimumHeight((int)Math.ceil(this.getMinHeight(cardViewDelegate)));
        ((View)cardViewDelegate).setMinimumWidth((int)Math.ceil(this.getMinWidth(cardViewDelegate)));
        cardViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }
}
