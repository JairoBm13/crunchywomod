// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.content.Intent;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View$OnClickListener;
import java.util.ArrayList;
import android.app.Activity;

public class c extends a
{
    private az a;
    private boolean b;
    private boolean e;
    private n f;
    
    public c(final a a, final Activity activity, final n f) {
        super(a, activity);
        this.f = f;
        this.a = f.s();
        this.b = false;
        this.e = false;
        this.d.a(this.a.a(aw.b.k));
        final az s = f.s();
        final aw a2 = s.a(aw.b.l);
        final aw a3 = s.a(aw.b.n);
        final String h = a2.h();
        final ArrayList<w> list = new ArrayList<w>();
        list.add(new w(h, aw.b.l.b()));
        final v v = new v((Context)this.c, (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (view.getTag().equals(h)) {
                    c.this.g();
                    return;
                }
                c.this.f();
            }
        }, list, this.f.q(), true, a3.h(), this.e());
        final Bitmap a4 = bw.a(this.a.d());
        final ImageView imageView = new ImageView((Context)this.c);
        imageView.setImageBitmap(a4);
        imageView.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
        imageView.setFocusable(false);
        final FrameLayout contentView = new FrameLayout((Context)this.c);
        contentView.addView((View)imageView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, -2);
        frameLayout$LayoutParams.gravity = 80;
        contentView.addView(v.d(), (ViewGroup$LayoutParams)frameLayout$LayoutParams);
        this.c.setContentView((View)contentView);
        if (this.a.a() > 0) {
            ac.e("Skip Time MS: " + this.a.a());
            contentView.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!c.this.e && !c.this.b) {
                        c.this.f();
                    }
                }
            }, (long)this.a.a());
        }
    }
    
    private int e() {
        return ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay().getWidth();
    }
    
    private void f() {
        synchronized (this) {
            if (!this.e) {
                this.e = true;
                this.d.a(this.a.a(aw.b.n));
                this.d.a(this);
            }
        }
    }
    
    private void g() {
        synchronized (this) {
            if (!this.b && !this.e) {
                this.d.a(this.a.a(aw.b.l));
                this.b = true;
                final Intent intent = new Intent((Context)this.c, (Class)Playvideo.class);
                intent.putExtra("tremorVideoType", "webview");
                intent.putExtra("tremorVideoURL", this.a.b());
                this.c.startActivityForResult(intent, 32);
            }
        }
    }
    
    @Override
    public void a() {
        if (this.b) {
            this.d.a(this.a.a(aw.b.m));
            this.d.a(this);
        }
    }
    
    @Override
    public void a(final int n, final int n2, final Intent intent) {
        if (n == 32) {
            this.d.a(this);
        }
    }
    
    @Override
    public boolean a(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            this.f();
            return true;
        }
        return super.a(n, keyEvent);
    }
    
    @Override
    public b n() {
        return com.tremorvideo.sdk.android.videoad.a.b.a;
    }
}
