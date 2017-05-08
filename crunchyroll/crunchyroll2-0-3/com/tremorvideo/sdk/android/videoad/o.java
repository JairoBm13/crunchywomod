// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap;
import com.tremorvideo.sdk.android.e.a;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

public class o
{
    public int a;
    public int b;
    
    public o(final DisplayMetrics displayMetrics) {
        this.a = 0;
        this.b = 0;
        final int densityDpi = displayMetrics.densityDpi;
        int n;
        if (densityDpi <= 120) {
            n = 26;
        }
        else if (densityDpi <= 160) {
            n = 30;
        }
        else if (densityDpi <= 240) {
            n = 34;
        }
        else if (densityDpi <= 320) {
            n = 46;
        }
        else if (densityDpi <= 480) {
            n = 62;
        }
        else {
            n = 80;
        }
        this.b = n;
        this.a = n;
    }
    
    public RectF a(final Canvas canvas, final a a) {
        int n = 2;
        if (a != o.a.e && a != o.a.f) {
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bw.a(a.a), this.a, this.b, false);
            int n3;
            int n5;
            int n6;
            int n7;
            int n8;
            if (a == o.a.a) {
                final int n2 = canvas.getWidth() / 2;
                final int a2 = this.a;
                n3 = -2;
                final int n4 = canvas.getHeight() / -2;
                n5 = -0;
                n6 = n2 - (a2 + 2);
                n7 = n4 + 2;
                n8 = 2;
            }
            else if (a == o.a.b) {
                final int n9 = canvas.getWidth() / -2;
                n3 = -2;
                final int n10 = canvas.getHeight() / -2;
                n5 = -0;
                n6 = n9 + 2;
                n7 = n10 + 2;
                n8 = 2;
            }
            else if (a == o.a.d) {
                final int n11 = canvas.getWidth() / -2;
                n3 = -2;
                final int n12 = canvas.getHeight() / 2;
                final int b = this.b;
                n5 = -2;
                n6 = n11 + 2;
                n7 = n12 - (b + 2);
                n8 = 2;
            }
            else if (a == o.a.c) {
                final int n13 = canvas.getWidth() / 2;
                final int a3 = this.a;
                n3 = -2;
                final int n14 = canvas.getHeight() / 2;
                final int b2 = this.b;
                n5 = -2;
                n6 = n13 - (a3 + 2);
                n7 = n14 - (b2 + 2);
                n8 = 2;
            }
            else {
                n = 0;
                n8 = 0;
                n3 = 0;
                n5 = 0;
                n7 = 0;
                n6 = 0;
            }
            final RectF rectF = new RectF();
            rectF.left = n3 + n6;
            rectF.top = n7 + n5;
            rectF.right = n8 + (this.a + n6);
            rectF.bottom = n + (this.b + n7);
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(179, 255, 255, 255));
            canvas.drawRoundRect(rectF, (float)0, (float)0, paint);
            canvas.drawBitmap(scaledBitmap, (float)n6, (float)n7, (Paint)null);
            return rectF;
        }
        return null;
    }
    
    public a a(final String s) {
        try {
            return o.a.values()[Integer.parseInt(s)];
        }
        catch (Exception ex) {
            return o.a.f;
        }
    }
    
    public enum a
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f;
    }
}
