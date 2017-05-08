// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.display;

import android.graphics.Shader;
import android.graphics.ComposeShader;
import android.graphics.PorterDuff$Mode;
import android.graphics.Matrix;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import android.graphics.Bitmap;

public class RoundedVignetteBitmapDisplayer extends RoundedBitmapDisplayer
{
    public RoundedVignetteBitmapDisplayer(final int n, final int n2) {
        super(n, n2);
    }
    
    @Override
    public void display(final Bitmap bitmap, final ImageAware imageAware, final LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        imageAware.setImageDrawable(new RoundedVignetteDrawable(bitmap, this.cornerRadius, this.margin));
    }
    
    protected static class RoundedVignetteDrawable extends RoundedDrawable
    {
        RoundedVignetteDrawable(final Bitmap bitmap, final int n, final int n2) {
            super(bitmap, n, n2);
        }
        
        @Override
        protected void onBoundsChange(final Rect rect) {
            super.onBoundsChange(rect);
            final RadialGradient radialGradient = new RadialGradient(this.mRect.centerX(), this.mRect.centerY() * 1.0f / 0.7f, this.mRect.centerX() * 1.3f, new int[] { 0, 0, 2130706432 }, new float[] { 0.0f, 0.7f, 1.0f }, Shader$TileMode.CLAMP);
            final Matrix localMatrix = new Matrix();
            localMatrix.setScale(1.0f, 0.7f);
            radialGradient.setLocalMatrix(localMatrix);
            this.paint.setShader((Shader)new ComposeShader((Shader)this.bitmapShader, (Shader)radialGradient, PorterDuff$Mode.SRC_OVER));
        }
    }
}
