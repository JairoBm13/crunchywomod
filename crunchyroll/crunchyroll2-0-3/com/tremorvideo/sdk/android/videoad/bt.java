// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class bt extends Drawable
{
    Bitmap a;
    Bitmap b;
    Bitmap c;
    
    public bt(final Bitmap bitmap, final Bitmap b) {
        this.a = bitmap;
        this.b = b;
        this.c = bitmap;
    }
    
    public void draw(final Canvas canvas) {
        final Rect copyBounds = this.copyBounds();
        canvas.drawBitmap(this.a, (Rect)null, new Rect(copyBounds.left, copyBounds.top, copyBounds.left + this.a.getWidth(), copyBounds.bottom), (Paint)null);
        final Rect rect = new Rect();
        rect.left = copyBounds.left + this.a.getWidth();
        rect.right = copyBounds.right - this.c.getWidth() - 1;
        rect.top = copyBounds.top;
        rect.bottom = copyBounds.bottom;
        canvas.drawBitmap(this.b, (Rect)null, rect, (Paint)null);
        if (this.c == this.a) {
            final Matrix matrix = new Matrix();
            matrix.postTranslate((float)(-this.c.getWidth()), 0.0f);
            matrix.postScale(-1.0f, copyBounds.height() / this.c.getHeight());
            matrix.postTranslate((float)rect.right, (float)copyBounds.top);
            canvas.drawBitmap(this.c, matrix, (Paint)null);
            return;
        }
        canvas.drawBitmap(this.c, (Rect)null, new Rect(rect.right, copyBounds.top, rect.right + this.c.getWidth(), copyBounds.bottom), (Paint)null);
    }
    
    public int getIntrinsicHeight() {
        return this.a.getHeight();
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
