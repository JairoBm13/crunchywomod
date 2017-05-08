// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import android.graphics.Typeface;
import android.view.View$OnClickListener;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.graphics.drawable.Drawable;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.content.BroadcastReceiver;
import android.app.Dialog;

public class aa extends Dialog
{
    BroadcastReceiver a;
    private Bitmap[] b;
    private Bitmap[] c;
    private bw d;
    private TextView e;
    private TextView f;
    private af g;
    private af h;
    private b i;
    
    public aa(final Context context, final bw d, final a a, final b i) {
        super(context);
        this.b = null;
        this.c = null;
        this.a = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                aa.this.a();
            }
        };
        this.getContext().registerReceiver(this.a, new IntentFilter("android.intent.action.SCREEN_OFF"));
        this.d = d;
        this.i = i;
        this.a(d);
        this.requestWindowFeature(1);
        this.getWindow().setBackgroundDrawable((Drawable)new al(this.b, this.d.a(bw.c.f)));
        this.setContentView(this.a(a), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -2));
    }
    
    private View a(final View view, final View view2, final Context context) {
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(5);
        if (view2 != null) {
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -2);
            linearLayout$LayoutParams.setMargins(10, 10, 10, 10);
            linearLayout.addView(view2, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        }
        if (view != null) {
            final LinearLayout$LayoutParams linearLayout$LayoutParams2 = new LinearLayout$LayoutParams(-2, -2);
            linearLayout$LayoutParams2.setMargins(0, 10, 10, 10);
            linearLayout.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
        }
        return (View)linearLayout;
    }
    
    private View a(final a a) {
        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(1);
        (this.e = new TextView(this.getContext())).setText((CharSequence)"");
        this.e.setPadding(10, 5, 10, 5);
        this.e.setTextColor(this.d.a(bw.c.i));
        this.e.setTextSize(2, (float)ac.J());
        (this.g = new af(this.getContext(), "", this.d, bw.d.ak, bw.d.al)).setId(45);
        this.g.setFocusable(true);
        this.g.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                aa.this.dismiss();
                aa.this.i.a(false);
            }
        });
        if (a == aa.a.b) {
            (this.h = new af(this.getContext(), "", this.d, bw.d.ak, bw.d.al)).setId(46);
            this.h.setFocusable(true);
            this.h.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    aa.this.dismiss();
                    aa.this.i.a(true);
                }
            });
        }
        linearLayout.addView((View)(this.f = this.a("", this.getContext())));
        linearLayout.addView((View)this.e);
        linearLayout.addView(this.a(this.g, this.h, this.getContext()));
        return (View)linearLayout;
    }
    
    private TextView a(final String text, final Context context) {
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)text);
        textView.setTextSize(2, (float)ac.I());
        textView.setTextColor(this.d.a(bw.c.g));
        textView.setTypeface(Typeface.create("helvetica", 1));
        textView.setShadowLayer(5.0f, 0.0f, 0.0f, this.d.a(bw.c.h));
        textView.setPadding(10, 5, 10, 0);
        return textView;
    }
    
    private void a() {
        if (this.g != null) {
            this.g.a(false);
        }
        if (this.h != null) {
            this.h.a(false);
        }
    }
    
    private void a(final bw bw) {
        (this.b = new Bitmap[al.a.values().length])[al.a.a.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.V);
        this.b[al.a.b.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.W);
        this.b[al.a.c.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.X);
        this.b[al.a.d.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.Y);
        this.b[al.a.e.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.Z);
        this.b[al.a.f.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.aa);
        this.b[al.a.g.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ab);
        this.b[al.a.h.ordinal()] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ac);
        (this.c = new Bitmap[3])[0] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ah);
        this.c[1] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ai);
        this.c[2] = bw.a(com.tremorvideo.sdk.android.videoad.bw.d.aj);
    }
    
    public void a(final String text) {
        this.e.setText((CharSequence)text);
    }
    
    public void a(final String s, final String s2) {
        if (this.g != null) {
            this.g.a(s);
        }
        if (this.h != null) {
            this.h.a(s2);
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 84) {
            return true;
        }
        if (n == 4) {
            this.dismiss();
            this.i.a(this.h != null);
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }
    
    protected void onStop() {
        super.onStop();
        this.getContext().unregisterReceiver(this.a);
    }
    
    public void setTitle(final CharSequence text) {
        this.f.setText(text);
    }
    
    public enum a
    {
        a, 
        b;
    }
    
    public interface b
    {
        void a(final boolean p0);
    }
}
