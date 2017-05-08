// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.b;

import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable$Orientation;
import java.util.Iterator;
import android.view.View$OnClickListener;
import android.graphics.drawable.Drawable;
import android.graphics.Paint$Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.shapes.Shape;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.text.TextUtils$TruncateAt;
import java.util.ArrayList;
import android.content.Context;
import java.util.List;
import android.widget.TextView;
import android.widget.LinearLayout;

public class a extends LinearLayout
{
    private d a;
    private TextView b;
    private TextView c;
    private List<a> d;
    
    public a(final Context context, final d a) {
        super(context);
        this.d = new ArrayList<a>();
        this.a = a;
        (this.b = new TextView(context)).setTextColor(a.h);
        this.b.setIncludeFontPadding(false);
        this.b.setGravity(16);
        this.b.setEllipsize(TextUtils$TruncateAt.END);
        this.b.setSingleLine();
        final int a2 = com.google.ads.interactivemedia.v3.b.b.c.a(a.k, this.getResources().getDisplayMetrics().density);
        this.b.setPadding(a2, a2, a2, a2);
        this.addView((View)this.b, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -2, 1.0f));
        if (a.l) {
            (this.c = new TextView(context)).setTextColor(a.o);
            this.c.setTextSize(a.p);
            this.c.setText((CharSequence)a.n);
            this.c.setIncludeFontPadding(false);
            this.c.setPadding(10, 10, 10, 10);
            this.c.setGravity(16);
            this.c.setEllipsize(TextUtils$TruncateAt.END);
            this.c.setSingleLine();
            final ShapeDrawable backgroundDrawable = new ShapeDrawable((Shape)new Shape() {
                public void draw(final Canvas canvas, final Paint paint) {
                    canvas.drawLine(0.0f, 0.0f, 0.0f, this.getHeight(), paint);
                }
            });
            backgroundDrawable.getPaint().setColor(a.e);
            backgroundDrawable.getPaint().setStrokeWidth((float)a.f);
            backgroundDrawable.getPaint().setStyle(Paint$Style.STROKE);
            this.c.setBackgroundDrawable((Drawable)backgroundDrawable);
            this.c.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    com.google.ads.interactivemedia.v3.b.b.a.this.a();
                }
            });
            final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(-2, -2);
            this.c.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.addView((View)this.c, (ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    protected void a() {
        final Iterator<a> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            iterator.next().c();
        }
    }
    
    public void a(final a a) {
        this.d.add(a);
    }
    
    public void a(final String text) {
        this.b.setText((CharSequence)text);
    }
    
    public void b(final String text) {
        this.c.setText((CharSequence)text);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable$Orientation.TOP_BOTTOM, this.a.b);
        gradientDrawable.setBounds(0, 0, n, n2);
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)new Shape() {
            public void draw(final Canvas canvas, final Paint paint) {
                canvas.drawLine(0.0f, this.getHeight(), this.getWidth(), this.getHeight(), paint);
            }
        });
        shapeDrawable.getPaint().setColor(this.a.c);
        shapeDrawable.getPaint().setStrokeWidth((float)this.a.d);
        shapeDrawable.getPaint().setStyle(Paint$Style.STROKE);
        shapeDrawable.setBounds(0, 0, n, n2);
        this.setBackgroundDrawable((Drawable)new LayerDrawable(new Drawable[] { gradientDrawable, shapeDrawable }));
    }
    
    public interface a
    {
        void c();
    }
}
