// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import java.util.TimerTask;
import java.util.Calendar;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import android.view.View$OnFocusChangeListener;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import android.graphics.Typeface;
import java.util.Timer;
import android.text.TextPaint;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Gallery$LayoutParams;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import java.util.Iterator;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import android.view.View$OnClickListener;
import android.content.Context;
import android.view.View;
import android.graphics.Bitmap;

public class v
{
    static int m;
    Bitmap[] a;
    int b;
    int c;
    View d;
    View e;
    boolean f;
    Context g;
    int h;
    View$OnClickListener i;
    bw j;
    List<c> k;
    boolean l;
    
    static {
        v.m = 20;
    }
    
    public v(final Context context, final View$OnClickListener view$OnClickListener, final t t, final int n) {
        this(context, view$OnClickListener, t, n, false);
    }
    
    public v(final Context context, final View$OnClickListener view$OnClickListener, final t t, final int n, final int n2) {
        this(context, view$OnClickListener, t, n, n2, false);
    }
    
    public v(final Context g, final View$OnClickListener i, final t t, int c, int h, final boolean b) {
        this.a = new Bitmap[8];
        this.g = g;
        this.i = i;
        this.h = h;
        this.e = null;
        this.f = true;
        this.j = t.q();
        this.k = new ArrayList<c>();
        this.a(h, b);
        final int a = a(t.S());
        this.c = c;
        this.b = a(this.a, false, h);
        final LinearLayout d = new LinearLayout(this.g);
        this.b(d, 10);
        final boolean v = t.V();
        final Iterator<aw> iterator = t.S().iterator();
        int n = a - c;
        c = 0;
        while (iterator.hasNext()) {
            final aw aw = iterator.next();
            if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.v || v) {
                h = c;
                if (aw.l()) {
                    if (n > 0) {
                        --n;
                        continue;
                    }
                    ++c;
                    c c2;
                    if (Math.min(this.c, this.b) == 1) {
                        c2 = this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.d);
                    }
                    else if (c == 1) {
                        c2 = this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.a);
                    }
                    else if (c < this.c && c < this.b) {
                        c2 = this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.b);
                    }
                    else {
                        c2 = this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.c);
                    }
                    if (c2 != null) {
                        c2.a(true);
                    }
                    h = c;
                    if (c < this.c && (h = c) < this.b) {
                        this.a(d, 5);
                        h = c;
                    }
                }
                if (h >= this.b) {
                    break;
                }
                c = h;
            }
        }
        (this.d = (View)d).setFocusableInTouchMode(false);
        this.d.setFocusable(true);
        this.i();
    }
    
    public v(final Context g, final View$OnClickListener i, final t t, int h, final boolean b) {
        this.a = new Bitmap[8];
        this.g = g;
        this.i = i;
        this.h = h;
        this.e = null;
        this.f = false;
        this.j = t.q();
        this.k = new ArrayList<c>();
        this.a(h, b);
        final boolean v = t.V();
        this.c = a(t.S());
        this.b = a(this.a, t.O() && !v, h);
        final LinearLayout d = new LinearLayout(this.g);
        this.b(d, 10);
        final Iterator<aw> iterator = t.S().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final aw aw = iterator.next();
            if (aw.a() != com.tremorvideo.sdk.android.videoad.aw.b.v || !v) {
                h = n;
                if (aw.l()) {
                    final int n2 = n + 1;
                    if (Math.min(this.c, this.b) == 1) {
                        this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.d);
                    }
                    else if (n2 == 1) {
                        this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.a);
                    }
                    else if (n2 < this.c && n2 < this.b) {
                        this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.b);
                    }
                    else {
                        this.a(d, aw, com.tremorvideo.sdk.android.videoad.v.b.c);
                    }
                    h = n2;
                    if (n2 < this.c && (h = n2) < this.b) {
                        this.a(d, 5);
                        h = n2;
                    }
                }
                if (h >= this.b) {
                    break;
                }
                n = h;
            }
        }
        if (t.O() && !v) {
            final View view = new View(this.g);
            view.setVisibility(4);
            view.setFocusable(false);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(0, 0);
            linearLayout$LayoutParams.weight = 1.0f;
            d.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            this.a(d, t);
            this.b(d, 10);
        }
        (this.d = (View)d).setFocusableInTouchMode(false);
        this.d.setFocusable(false);
        this.i();
    }
    
    public v(final Context g, final View$OnClickListener i, final List<w> list, final bw j, final boolean b, final String s, int h) {
        this.a = new Bitmap[8];
        this.g = g;
        this.i = i;
        this.h = h;
        this.e = null;
        this.f = false;
        this.j = j;
        this.k = new ArrayList<c>();
        this.j();
        this.c = list.size();
        this.b = a(this.a, b, h);
        final LinearLayout d = new LinearLayout(this.g);
        this.b(d, 10);
        final Iterator<w> iterator = list.iterator();
        h = 0;
        while (iterator.hasNext()) {
            final w w = iterator.next();
            final int n = h + 1;
            final Bitmap a = this.j.a(w.b);
            if (Math.min(this.c, this.b) == 1) {
                this.a(d, w.a, a, v.b.d);
            }
            else if (n == 1) {
                this.a(d, w.a, a, v.b.a);
            }
            else if (n < this.c) {
                this.a(d, w.a, a, v.b.b);
            }
            else {
                this.a(d, w.a, a, v.b.c);
            }
            if (n < this.c) {
                this.a(d, 5);
            }
            if ((h = n) >= this.b) {
                break;
            }
        }
        if (b) {
            final View view = new View(this.g);
            view.setVisibility(4);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(0, 0);
            linearLayout$LayoutParams.weight = 1.0f;
            d.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            String b2;
            if ((b2 = s) == null) {
                b2 = aw.b(aw.b.v);
            }
            this.e = this.a(d, b2, this.j.a(bw.d.q), v.b.d);
            this.b(d, 10);
        }
        (this.d = (View)d).setFocusableInTouchMode(false);
        this.d.setFocusable(true);
        this.i();
    }
    
    public v(final Context g, final View$OnClickListener i, final aw.b[] array, final bw j, final boolean b, final String s, int k) {
        this.a = new Bitmap[8];
        this.g = g;
        this.i = i;
        this.h = k;
        this.e = null;
        this.f = false;
        this.j = j;
        this.k = new ArrayList<c>();
        this.j();
        this.c = array.length;
        this.b = a(this.a, b, k);
        final LinearLayout d = new LinearLayout(this.g);
        this.b(d, 10);
        int n = 0;
        int length;
        aw.b b2;
        String c;
        Bitmap a;
        for (length = array.length, k = 0; k < length; ++k) {
            b2 = array[k];
            ++n;
            c = b2.c();
            a = this.j.a(b2.b());
            if (Math.min(this.c, this.b) == 1) {
                this.a(d, c, a, v.b.d);
            }
            else if (n == 1) {
                this.a(d, c, a, v.b.a);
            }
            else if (n < this.c) {
                this.a(d, c, a, v.b.b);
            }
            else {
                this.a(d, c, a, v.b.c);
            }
            if (n < this.c) {
                this.a(d, 5);
            }
            if (n >= this.b) {
                break;
            }
        }
        if (b) {
            final View view = new View(this.g);
            view.setVisibility(4);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(0, 0);
            linearLayout$LayoutParams.weight = 1.0f;
            d.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            String b3;
            if ((b3 = s) == null) {
                b3 = aw.b(aw.b.v);
            }
            this.e = this.a(d, b3, this.j.a(bw.d.q), v.b.d);
            this.b(d, 10);
        }
        (this.d = (View)d).setFocusableInTouchMode(false);
        this.d.setFocusable(false);
        this.i();
    }
    
    private static int a(final List<aw> list) {
        final Iterator<aw> iterator = list.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (iterator.next().l()) {
                ++n;
            }
        }
        return n;
    }
    
    private static int a(final Bitmap[] array, final boolean b, int n) {
        final int n2 = array[2].getWidth() + array[0].getWidth();
        final int n3 = n = n - n2 - 20;
        if (b) {
            n = n3 - (n2 + 5) - array[1].getWidth();
        }
        return n / (array[0].getWidth() + array[1].getWidth() + array[5].getWidth());
    }
    
    private c a(final LinearLayout linearLayout, final aw tag, final b b) {
        Bitmap bitmap;
        if (this.l) {
            bitmap = this.j.a(tag.o() + "-small-size");
        }
        else {
            bitmap = this.j.a(tag.o());
        }
        Bitmap bitmap2 = bitmap;
        if (bitmap == null) {
            if (this.l) {
                bitmap2 = this.j.a(bw.d.b);
            }
            else {
                bitmap2 = this.j.a(tag.a().b());
            }
        }
        final c c = new c(this.g, bitmap2, tag.h(), b);
        final int m = v.m;
        v.m = m + 1;
        c.setId(m);
        c.setTag((Object)tag);
        c.setVisibility(0);
        c.setFocusable(false);
        c.setFocusableInTouchMode(false);
        c.setOnClickListener(this.i);
        linearLayout.addView((View)c);
        this.k.add(c);
        return c;
    }
    
    private c a(final LinearLayout linearLayout, final String tag, final Bitmap bitmap, final b b) {
        final c c = new c(this.g, bitmap, tag, b);
        final int m = v.m;
        v.m = m + 1;
        c.setId(m);
        c.setTag((Object)tag);
        c.setVisibility(0);
        c.setFocusable(false);
        c.setFocusableInTouchMode(false);
        c.setOnClickListener(this.i);
        this.k.add(c);
        linearLayout.addView((View)c);
        return c;
    }
    
    private void a(final int n, final boolean b) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)this.g.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        final int n2 = (int)displayMetrics.density;
        if (b && n / n2 <= 320) {
            try {
                this.l = true;
                this.a[0] = this.j.a(bw.d.K);
                this.a[1] = this.j.a(bw.d.L);
                this.a[2] = this.j.a(bw.d.M);
                this.a[3] = this.j.a(bw.d.O);
                this.a[5] = this.j.a(bw.d.J);
                this.a[6] = this.j.a(bw.d.N);
                this.a[7] = this.j.a(bw.d.P);
                return;
            }
            catch (Exception ex) {
                ac.e("TremorLog_error::ButtonBar::Image resize " + ex);
                return;
            }
        }
        this.a[0] = this.j.a(bw.d.D);
        this.a[1] = this.j.a(bw.d.E);
        this.a[2] = this.j.a(bw.d.F);
        this.a[3] = this.j.a(bw.d.H);
        this.a[5] = this.j.a(bw.d.C);
        this.a[6] = this.j.a(bw.d.G);
        this.a[7] = this.j.a(bw.d.I);
    }
    
    private void a(final LinearLayout linearLayout, final int n) {
        final ImageView imageView = new ImageView(this.g);
        imageView.setImageDrawable((Drawable)new a(this.a[n]));
        imageView.setLayoutParams((ViewGroup$LayoutParams)new Gallery$LayoutParams(-2, -2));
        linearLayout.addView((View)imageView);
    }
    
    private void a(final LinearLayout linearLayout, final t t) {
        if (t.O()) {
            for (final aw aw : t.l()) {
                if (aw.a() == aw.b.v) {
                    (this.e = this.a(linearLayout, aw, v.b.d)).setVisibility(4);
                    break;
                }
            }
        }
    }
    
    private void b(final LinearLayout linearLayout, final int n) {
        linearLayout.addView(new View(this.g), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(n, 0));
    }
    
    private void h() {
        if (this.e != null) {
            this.e.setFocusable(true);
            if (this.k.size() > 0) {
                this.k.get(this.k.size() - 1).setNextFocusRightId(this.e.getId());
            }
        }
    }
    
    private void i() {
        this.d.setFocusable(true);
        for (int i = 0; i < this.k.size(); ++i) {
            final c c = this.k.get(i);
            c.setFocusable(true);
            if (i > 0) {
                c.setNextFocusLeftId(this.k.get(i - 1).getId());
            }
            if (i < this.k.size() - 1) {
                c.setNextFocusRightId(this.k.get(i + 1).getId());
            }
        }
        this.h();
        if (this.k.size() > 0) {
            this.k.get(0).requestFocus();
        }
        else if (this.e != null) {
            this.e.requestFocus();
        }
    }
    
    private void j() {
        this.a[0] = this.j.a(bw.d.D);
        this.a[1] = this.j.a(bw.d.E);
        this.a[2] = this.j.a(bw.d.F);
        this.a[3] = this.j.a(bw.d.H);
        this.a[5] = this.j.a(bw.d.C);
        this.a[6] = this.j.a(bw.d.G);
        this.a[7] = this.j.a(bw.d.I);
    }
    
    public void a(final aw aw) {
        for (final View view : this.d.getTouchables()) {
            if (view instanceof c) {
                final c c = (c)view;
                if (c.getTag() != aw) {
                    continue;
                }
                c.a();
            }
        }
    }
    
    public boolean a() {
        return this.c > this.b;
    }
    
    public int b() {
        return this.c - this.b;
    }
    
    public int c() {
        return this.a[0].getWidth() + this.a[2].getWidth() + (this.a[0].getWidth() + this.a[1].getWidth() + this.a[5].getWidth()) * this.c;
    }
    
    public View d() {
        return this.d;
    }
    
    public void e() {
        if (this.e != null) {
            this.e.post((Runnable)new d(this.e));
            this.h();
        }
    }
    
    public void f() {
        if (this.e != null) {
            this.e.setVisibility(0);
            this.h();
        }
    }
    
    public void g() {
        for (final View view : this.d.getTouchables()) {
            if (view instanceof c) {
                ((c)view).b(false);
            }
        }
    }
    
    class a extends BitmapDrawable
    {
        Bitmap a;
        
        public a(final Bitmap a) {
            super(a);
            this.a = a;
        }
        
        public void draw(final Canvas canvas) {
            if (!v.this.f) {
                super.draw(canvas);
                return;
            }
            final Matrix matrix = new Matrix();
            matrix.postScale(1.0f, -1.0f);
            matrix.postTranslate(0.0f, (float)this.a.getHeight());
            canvas.drawBitmap(this.a, matrix, (Paint)null);
        }
        
        public int getIntrinsicHeight() {
            return this.a.getHeight();
        }
        
        public int getIntrinsicWidth() {
            return this.a.getWidth();
        }
    }
    
    public enum b
    {
        a, 
        b, 
        c, 
        d;
    }
    
    private class c extends View
    {
        b a;
        Bitmap b;
        boolean c;
        TextPaint d;
        String e;
        boolean f;
        float g;
        Timer h;
        long i;
        
        public c(final Context context, final Bitmap b, final String e, final b a) {
            super(context);
            this.a = a;
            this.b = b;
            this.e = e;
            this.g = 0.0f;
            this.c = false;
            this.f = false;
            this.d = new TextPaint();
            if (v.this.l) {
                this.d.setTextSize((float)ac.a(8));
                this.d.setTypeface(Typeface.create("helvetica", 0));
            }
            else {
                this.d.setTextSize((float)ac.K());
                this.d.setTypeface(Typeface.create("helvetica", 1));
            }
            this.d.setColor(v.this.j.a(bw.c.a));
            this.d.setShadowLayer(5.0f, 0.0f, 0.0f, v.this.j.a(bw.c.b));
            this.d.setAntiAlias(true);
            this.e = (String)TextUtils.ellipsize((CharSequence)this.e, this.d, (float)(v.this.a[1].getWidth() - 5), TextUtils$TruncateAt.END);
            this.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                        v.c.this.c = false;
                    }
                    else {
                        v.c.this.c = true;
                    }
                    view.invalidate();
                    return false;
                }
            });
            this.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
                public void onFocusChange(final View view, final boolean c) {
                    v.c.this.c = c;
                    view.invalidate();
                }
            });
            this.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
                public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                    if (n == 23) {
                        v.c.this.c = true;
                        view.invalidate();
                    }
                    return false;
                }
            });
        }
        
        private void a(final Canvas canvas) {
            Bitmap bitmap;
            if (this.c) {
                bitmap = v.this.a[3];
            }
            else {
                bitmap = v.this.a[1];
            }
            Bitmap bitmap2;
            if (this.c) {
                bitmap2 = v.this.a[6];
            }
            else {
                bitmap2 = v.this.a[0];
            }
            Bitmap bitmap3;
            if (this.c) {
                bitmap3 = v.this.a[7];
            }
            else {
                bitmap3 = v.this.a[2];
            }
            if (this.a == v.b.b) {
                this.a(canvas, bitmap, new Rect(0, 0, bitmap2.getWidth(), bitmap.getHeight()));
                this.a(canvas, bitmap, bitmap2.getWidth(), 0);
            }
            else {
                if (this.a == v.b.a) {
                    this.a(canvas, bitmap2, 0, 0);
                    this.a(canvas, bitmap, bitmap2.getWidth(), 0);
                    return;
                }
                if (this.a == v.b.c) {
                    this.a(canvas, bitmap, 0, 0);
                    this.a(canvas, bitmap3, bitmap.getWidth(), 0);
                    return;
                }
                if (this.a == v.b.d) {
                    this.a(canvas, bitmap2, 0, 0);
                    this.a(canvas, bitmap, bitmap2.getWidth(), 0);
                    this.a(canvas, bitmap3, bitmap.getWidth() + bitmap2.getWidth(), 0);
                }
            }
        }
        
        private void a(final Canvas canvas, final Bitmap bitmap, final int n, final int n2) {
            if (!this.f) {
                canvas.drawBitmap(bitmap, (float)n, (float)n2, (Paint)null);
                return;
            }
            final Matrix matrix = new Matrix();
            matrix.postTranslate(0.0f, (float)(-bitmap.getHeight()));
            matrix.postScale(1.0f, -1.0f);
            matrix.postTranslate((float)n, (float)n2);
            canvas.drawBitmap(bitmap, matrix, (Paint)null);
        }
        
        private void a(final Canvas canvas, final Bitmap bitmap, final Rect rect) {
            if (!this.f) {
                canvas.drawBitmap(bitmap, rect, rect, (Paint)null);
                return;
            }
            canvas.save(2);
            if (canvas.clipRect(rect)) {
                final Matrix matrix = new Matrix();
                matrix.postTranslate(0.0f, (float)(-bitmap.getHeight()));
                matrix.postScale(1.0f, -1.0f);
                matrix.postTranslate((float)rect.left, (float)rect.top);
                canvas.drawBitmap(bitmap, matrix, (Paint)null);
            }
            canvas.restore();
        }
        
        private int b() {
            int n = 0;
            final Bitmap bitmap = v.this.a[1];
            final Bitmap bitmap2 = v.this.a[0];
            final Bitmap bitmap3 = v.this.a[0];
            if (this.a == v.b.a) {
                n = bitmap2.getWidth() + bitmap.getWidth();
            }
            else {
                if (this.a == v.b.c) {
                    return bitmap3.getWidth() + bitmap.getWidth();
                }
                if (this.a == v.b.b) {
                    return bitmap2.getWidth() + bitmap.getWidth();
                }
                if (this.a == v.b.d) {
                    return bitmap2.getWidth() + bitmap3.getWidth() + bitmap.getWidth();
                }
            }
            return n;
        }
        
        private int c() {
            return v.this.a[1].getHeight();
        }
        
        public void a() {
            this.i = Calendar.getInstance().getTimeInMillis();
            this.g = 3.0f;
            (this.h = new Timer()).scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    final long timeInMillis = Calendar.getInstance().getTimeInMillis();
                    final long i = v.c.this.i;
                    v.c.this.i = timeInMillis;
                    v.c.this.g = Math.max(0.0f, v.c.this.g - (timeInMillis - i) / 1000.0f * 1.5f);
                    while (true) {
                        try {
                            v.c.this.postInvalidate();
                            if (v.c.this.g == 0.0f) {
                                this.cancel();
                            }
                        }
                        catch (Exception ex) {
                            ac.a(ex);
                            continue;
                        }
                        break;
                    }
                }
            }, 10L, 10L);
        }
        
        public void a(final boolean f) {
            this.f = f;
        }
        
        public void b(final boolean c) {
            if (this.c != c) {
                this.c = c;
                this.invalidate();
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            final int b = this.b();
            final int c = this.c();
            this.a(canvas);
            final float n = (float)(Math.sin((this.g - (int)this.g) * 3.141592653589793) * Math.round(-15.0f * ac.M()));
            if (this.b == null) {
                final Rect rect = new Rect();
                this.d.getTextBounds(this.e, 0, this.e.length(), rect);
                canvas.drawText(this.e, (float)((b - rect.width()) / 2), (c - rect.height()) / 2 + 10.0f + n, (Paint)this.d);
                return;
            }
            int n2;
            if (this.f) {
                n2 = 0;
            }
            else {
                n2 = -5;
            }
            int n3;
            if (this.f) {
                n3 = 5;
            }
            else {
                n3 = 0;
            }
            final Rect rect2 = new Rect();
            this.d.getTextBounds(this.e, 0, this.e.length(), rect2);
            final int round = Math.round(this.d.getTextSize() + this.d.baselineShift);
            canvas.drawBitmap(this.b, (b - this.b.getWidth()) / 2.0f, (c - (n3 + round) - this.b.getHeight()) / 2 + n, (Paint)null);
            canvas.drawText(this.e, (float)((b - rect2.width()) / 2), (float)(c - round - n2), (Paint)this.d);
        }
        
        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(this.b(), v.this.a[1].getHeight());
        }
    }
    
    private class d implements Runnable
    {
        View a;
        boolean b;
        
        d(final View a) {
            this.b = false;
            (this.a = a).setVisibility(4);
        }
        
        @Override
        public void run() {
            if (!this.b) {
                this.b = true;
                final TranslateAnimation translateAnimation = new TranslateAnimation(0, 0.0f, 0, 0.0f, 1, 1.0f, 1, 0.0f);
                translateAnimation.setInterpolator((Interpolator)new DecelerateInterpolator());
                translateAnimation.setDuration(1000L);
                this.a.setVisibility(0);
                this.a.startAnimation((Animation)translateAnimation);
                this.a.getParent().requestLayout();
            }
        }
    }
}
