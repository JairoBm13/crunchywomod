// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.graphics.ColorFilter;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class al extends Drawable
{
    Bitmap[] a;
    int b;
    
    public al(final Bitmap[] a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    private void a(final Canvas canvas, final Rect rect) {
        final Bitmap[] array = { this.a[al.a.a.ordinal()], this.a[al.a.b.ordinal()], this.a[al.a.c.ordinal()] };
        a(canvas, new Rect(rect.left, rect.top, rect.right, rect.top + array[0].getHeight()), array);
    }
    
    public static void a(final Canvas canvas, Rect rect, final Bitmap[] array) {
        canvas.drawBitmap(array[0], (Rect)null, new Rect(rect.left, rect.top, rect.left + array[0].getWidth(), rect.top + array[0].getHeight()), (Paint)null);
        final Rect rect2 = new Rect();
        rect2.left = rect.left + array[0].getWidth();
        rect2.right = rect.right - array[2].getWidth() - 1;
        rect2.top = rect.top;
        rect2.bottom = rect.top + array[1].getHeight();
        canvas.drawBitmap(array[1], (Rect)null, rect2, (Paint)null);
        rect = new Rect(rect2.right, rect.top, rect2.right + array[2].getWidth(), rect.top + array[2].getHeight());
        canvas.drawBitmap(array[2], (Rect)null, rect, (Paint)null);
    }
    
    private void b(final Canvas canvas, final Rect rect) {
        final int n = rect.top + this.a[al.a.a.ordinal()].getHeight();
        final int n2 = rect.bottom - this.a[al.a.f.ordinal()].getHeight() - 1;
        final int right = rect.left + this.a[al.a.d.ordinal()].getWidth();
        final int right2 = rect.right;
        final int width = this.a[al.a.e.ordinal()].getWidth();
        final Rect rect2 = new Rect();
        rect2.left = rect.left;
        rect2.right = right;
        rect2.top = n;
        rect2.bottom = n2;
        canvas.drawBitmap(this.a[al.a.d.ordinal()], (Rect)null, rect2, (Paint)null);
        rect2.left = rect.right - 1 - this.a[al.a.e.ordinal()].getWidth();
        rect2.right = rect.right - 1;
        rect2.top = n;
        rect2.bottom = n2;
        canvas.drawBitmap(this.a[al.a.e.ordinal()], (Rect)null, rect2, (Paint)null);
        final Paint paint = new Paint();
        paint.setStyle(Paint$Style.FILL);
        paint.setColor(this.b);
        canvas.drawRect((float)right, (float)n, (float)(right2 - width - 1), (float)n2, paint);
    }
    
    private void c(final Canvas canvas, final Rect rect) {
        final Bitmap[] array = { this.a[al.a.f.ordinal()], this.a[al.a.g.ordinal()], this.a[al.a.h.ordinal()] };
        a(canvas, new Rect(rect.left, rect.bottom - array[0].getHeight() - 1, rect.right, rect.bottom - 1), array);
    }
    
    public void draw(final Canvas canvas) {
        final Rect copyBounds = this.copyBounds();
        this.a(canvas, copyBounds);
        this.b(canvas, copyBounds);
        this.c(canvas, copyBounds);
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public enum a
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h;
    }
}
