// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import android.view.View$OnFocusChangeListener;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;

public class af extends View
{
    boolean a;
    TextPaint b;
    String c;
    bw d;
    Drawable e;
    Drawable f;
    int g;
    int h;
    float i;
    
    public af(final Context context, final String c, final int n, final bw d, final Drawable e, final Drawable f) {
        super(context);
        this.c = c;
        this.a = false;
        this.d = d;
        this.e = e;
        this.f = f;
        this.h = e.getIntrinsicHeight();
        this.i = ac.K();
        this.a(this.d.a(bw.c.a), this.d.a(bw.c.b));
        this.g = Math.max(80, Math.round(this.b.measureText(this.c)) + 4);
        this.a();
    }
    
    public af(final Context context, final String c, final bw d, final bw.d d2, final bw.d d3) {
        super(context);
        this.c = c;
        this.a = false;
        this.d = d;
        final Bitmap a = this.d.a(d2);
        this.e = (Drawable)new BitmapDrawable(a);
        this.f = (Drawable)new BitmapDrawable(this.d.a(d3));
        this.g = a.getWidth();
        this.h = a.getHeight();
        this.i = ac.H();
        this.a(d.a(bw.c.j), Color.argb(0, 0, 0, 0));
        this.a();
    }
    
    private void a() {
        this.setBackgroundDrawable(this.e);
        this.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    af.this.a = false;
                }
                else {
                    af.this.a = true;
                }
                af.this.b();
                view.invalidate();
                return false;
            }
        });
        this.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean a) {
                af.this.a = a;
                af.this.b();
                view.invalidate();
            }
        });
        this.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                if (n == 23) {
                    af.this.a = true;
                    af.this.b();
                    view.invalidate();
                }
                return false;
            }
        });
    }
    
    private void a(final int color, final int n) {
        (this.b = new TextPaint()).setTextSize(this.i);
        this.b.setColor(color);
        this.b.setTypeface(Typeface.create("helvetica", 1));
        this.b.setAntiAlias(true);
        if (Color.alpha(n) > 0) {
            this.b.setShadowLayer(5.0f, 0.0f, 0.0f, this.d.a(bw.c.b));
        }
    }
    
    private void b() {
        if (this.a) {
            this.setBackgroundDrawable(this.f);
            return;
        }
        this.setBackgroundDrawable(this.e);
    }
    
    public void a(final String c) {
        this.c = c;
        this.invalidate();
    }
    
    public void a(final boolean a) {
        if (this.a != a) {
            this.a = a;
            this.b();
            this.invalidate();
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        final Rect rect = new Rect();
        this.b.getTextBounds(this.c, 0, this.c.length(), rect);
        canvas.drawText(this.c, (float)Math.round((this.g - rect.width()) / 2), (float)(rect.height() + Math.round((this.h - rect.height()) / 2)), (Paint)this.b);
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(this.g, this.h);
    }
}
