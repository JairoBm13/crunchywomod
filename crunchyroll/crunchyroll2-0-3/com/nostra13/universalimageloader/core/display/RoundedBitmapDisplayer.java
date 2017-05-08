// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core.display;

import android.graphics.ColorFilter;
import android.graphics.Matrix$ScaleToFit;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.Shader$TileMode;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.BitmapShader;
import android.graphics.drawable.Drawable;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import android.graphics.Bitmap;

public class RoundedBitmapDisplayer implements BitmapDisplayer
{
    protected final int cornerRadius;
    protected final int margin;
    
    public RoundedBitmapDisplayer(final int n) {
        this(n, 0);
    }
    
    public RoundedBitmapDisplayer(final int cornerRadius, final int margin) {
        this.cornerRadius = cornerRadius;
        this.margin = margin;
    }
    
    @Override
    public void display(final Bitmap bitmap, final ImageAware imageAware, final LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        imageAware.setImageDrawable(new RoundedDrawable(bitmap, this.cornerRadius, this.margin));
    }
    
    public static class RoundedDrawable extends Drawable
    {
        protected final BitmapShader bitmapShader;
        protected final float cornerRadius;
        protected final RectF mBitmapRect;
        protected final RectF mRect;
        protected final int margin;
        protected final Paint paint;
        
        public RoundedDrawable(final Bitmap bitmap, final int n, final int margin) {
            this.mRect = new RectF();
            this.cornerRadius = n;
            this.margin = margin;
            this.bitmapShader = new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
            this.mBitmapRect = new RectF((float)margin, (float)margin, (float)(bitmap.getWidth() - margin), (float)(bitmap.getHeight() - margin));
            (this.paint = new Paint()).setAntiAlias(true);
            this.paint.setShader((Shader)this.bitmapShader);
        }
        
        public void draw(final Canvas canvas) {
            canvas.drawRoundRect(this.mRect, this.cornerRadius, this.cornerRadius, this.paint);
        }
        
        public int getOpacity() {
            return -3;
        }
        
        protected void onBoundsChange(final Rect rect) {
            super.onBoundsChange(rect);
            this.mRect.set((float)this.margin, (float)this.margin, (float)(rect.width() - this.margin), (float)(rect.height() - this.margin));
            final Matrix localMatrix = new Matrix();
            localMatrix.setRectToRect(this.mBitmapRect, this.mRect, Matrix$ScaleToFit.FILL);
            this.bitmapShader.setLocalMatrix(localMatrix);
        }
        
        public void setAlpha(final int alpha) {
            this.paint.setAlpha(alpha);
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
            this.paint.setColorFilter(colorFilter);
        }
    }
}
