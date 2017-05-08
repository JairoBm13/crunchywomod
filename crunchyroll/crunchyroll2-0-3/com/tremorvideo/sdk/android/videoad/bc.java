// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$OnClickListener;
import android.text.TextUtils$TruncateAt;
import android.graphics.Typeface;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.view.View;
import android.content.Context;

public class bc
{
    bw a;
    String b;
    String c;
    String d;
    int e;
    
    public bc(final bw a, final String b, final String c, final String d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = -1;
    }
    
    public View a(final Context context, final a a) {
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundDrawable((Drawable)new bt(this.a.a(bw.d.am), this.a.a(bw.d.an)));
        linearLayout.setGravity(1);
        linearLayout.setPadding(5, 5, 5, 5);
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)this.b);
        textView.setTextSize((float)ac.K());
        textView.setTypeface(Typeface.create("helvetica", 1));
        textView.setEllipsize(TextUtils$TruncateAt.END);
        textView.setTextColor(this.a.a(bw.c.g));
        textView.setShadowLayer(1.2f, 1.0f, 1.0f, this.a.a(bw.c.h));
        textView.setMaxLines(2);
        textView.setPadding(0, 0, 5, 0);
        textView.setGravity(19);
        final af af = new af(context, this.c, 100, this.a, new bt(this.a.a(bw.d.ao), this.a.a(bw.d.ap)), new bt(this.a.a(bw.d.aq), this.a.a(bw.d.ar)));
        af.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                a.a(0);
            }
        });
        final af af2 = new af(context, this.d, 100, this.a, new bt(this.a.a(bw.d.ao), this.a.a(bw.d.ap)), new bt(this.a.a(bw.d.aq), this.a.a(bw.d.ar)));
        af2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                a.a(1);
            }
        });
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams.weight = 0.8f;
        linearLayout.addView((View)textView, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        final LinearLayout$LayoutParams linearLayout$LayoutParams2 = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams2.gravity = 21;
        linearLayout.addView((View)af, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
        final LinearLayout$LayoutParams linearLayout$LayoutParams3 = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams3.gravity = 21;
        linearLayout$LayoutParams3.leftMargin = 2;
        linearLayout.addView((View)af2, (ViewGroup$LayoutParams)linearLayout$LayoutParams3);
        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setPadding(10, 0, 10, 0);
        linearLayout2.addView((View)linearLayout, -2, -2);
        return (View)linearLayout2;
    }
    
    public interface a
    {
        void a(final int p0);
    }
}
