// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.widget.TableLayout;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import org.json.JSONArray;
import android.text.Html;
import org.json.JSONObject;
import com.tremorvideo.sdk.android.richmedia.ae;
import android.view.ViewGroup$LayoutParams;
import android.widget.TableRow$LayoutParams;
import android.widget.TableRow;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ViewFlipper;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.view.View;
import android.text.TextUtils$TruncateAt;
import android.graphics.Typeface;
import android.widget.TextView;
import android.view.WindowManager;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class bz
{
    final int a;
    final int b;
    final int c;
    boolean d;
    List<String> e;
    c f;
    int g;
    t h;
    
    public bz(final t h) {
        this.a = 5;
        this.b = 5;
        this.c = 5;
        this.d = false;
        this.e = new ArrayList<String>();
        this.g = 0;
        this.h = h;
    }
    
    private int a(final Context context) {
        int width = ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getWidth();
        if (this.h.U() != null) {
            width -= this.h.U().a();
        }
        return width;
    }
    
    private TextView a(final Context context, final bw bw) {
        final TextView textView = new TextView(context);
        textView.setTextSize((float)ac.K());
        textView.setTypeface(Typeface.create("helvetica", 1));
        textView.setEllipsize(TextUtils$TruncateAt.END);
        textView.setTextColor(bw.a(com.tremorvideo.sdk.android.videoad.bw.c.g));
        textView.setShadowLayer(1.2f, 1.0f, 1.0f, bw.a(com.tremorvideo.sdk.android.videoad.bw.c.h));
        textView.setPadding(0, 0, 5, 0);
        textView.setGravity(19);
        textView.setVerticalFadingEdgeEnabled(true);
        textView.setFadingEdgeLength(10);
        textView.setLinksClickable(true);
        return textView;
    }
    
    public View a(final Context context, final int n, final bw bw) {
        final ImageView imageView = new ImageView(context);
        imageView.setImageDrawable((Drawable)new a(bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ad)));
        imageView.setPadding(0, 5, 0, 0);
        final ImageView imageView2 = new ImageView(context);
        imageView2.setImageDrawable((Drawable)new a(bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ae)));
        final ImageView imageView3 = new ImageView(context);
        imageView3.setImageDrawable((Drawable)new a(bw.a(com.tremorvideo.sdk.android.videoad.bw.d.ag)));
        final ViewFlipper viewFlipper = new ViewFlipper(context);
        int i = 0;
    Label_0165_Outer:
        while (i < Math.min(10, this.e.size())) {
            while (true) {
                try {
                    final TextView a = this.a(context, bw);
                    a.setText((CharSequence)this.e.get(i));
                    viewFlipper.addView((View)a);
                    ++i;
                    continue Label_0165_Outer;
                }
                catch (Exception ex) {
                    ac.a(ex);
                    continue;
                }
                break;
            }
            break;
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();
        viewFlipper.setBackgroundDrawable((Drawable)new BitmapDrawable(bw.a(com.tremorvideo.sdk.android.videoad.bw.d.af)));
        final AlphaAnimation inAnimation = new AlphaAnimation(0.0f, 1.0f);
        ((Animation)inAnimation).setDuration(300L);
        ((Animation)inAnimation).setStartOffset(300L);
        viewFlipper.setInAnimation((Animation)inAnimation);
        final AlphaAnimation outAnimation = new AlphaAnimation(1.0f, 0.0f);
        ((Animation)outAnimation).setDuration(300L);
        viewFlipper.setOutAnimation((Animation)outAnimation);
        final TableRow tableRow = new TableRow(context);
        final TableRow$LayoutParams tableRow$LayoutParams = new TableRow$LayoutParams();
        tableRow$LayoutParams.gravity = 51;
        tableRow.addView((View)imageView, (ViewGroup$LayoutParams)tableRow$LayoutParams);
        final TableRow tableRow2 = new TableRow(context);
        tableRow2.addView((View)imageView2);
        tableRow2.addView((View)viewFlipper);
        tableRow2.addView((View)imageView3);
        final b b = new b(context, bw, n);
        b.setPadding(5, 0, 5, 0);
        b.addView((View)tableRow);
        b.addView((View)tableRow2);
        b.a((View)viewFlipper);
        b.b();
        b.setFocusable(false);
        return (View)b;
    }
    
    public void a(final String s, final c f) {
        if (!this.d) {
            this.f = f;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean b = false;
                    while (true) {
                        Label_0214: {
                            boolean b2;
                            try {
                                final bb a = bb.a(s, ae.c(s));
                                a.a();
                                b2 = b;
                                if (a.b() != null) {
                                    final JSONArray jsonArray = new JSONObject(a.b()).getJSONArray("results");
                                    int i = 0;
                                    while (i < jsonArray.length()) {
                                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        try {
                                            bz.this.e.add("@" + jsonObject.getString("from_user") + ": " + Html.fromHtml(jsonObject.getString("text")).toString());
                                            ++i;
                                        }
                                        catch (Exception ex) {
                                            ac.a(ex);
                                        }
                                    }
                                    break Label_0214;
                                }
                            }
                            catch (Exception ex2) {
                                ac.e(ex2.getMessage());
                                b2 = b;
                            }
                            bz.this.d = true;
                            bz.this.f.a(bz.this, b2);
                            return;
                        }
                        boolean b2 = true;
                        continue;
                    }
                }
            }).run();
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
            super.draw(canvas);
        }
        
        public int getIntrinsicHeight() {
            return this.a.getHeight();
        }
        
        public int getIntrinsicWidth() {
            return this.a.getWidth();
        }
    }
    
    class b extends TableLayout implements bd
    {
        View a;
        bw b;
        int c;
        
        b(final Context context, final bw b, final int c) {
            super(context);
            this.c = c;
            this.b = b;
        }
        
        private int c() {
            int n = this.c;
            if (this.c == 0) {
                n = bz.this.a(this.getContext());
            }
            return n - this.b.a(bw.d.ae).getWidth() - this.b.a(bw.d.ag).getWidth() - 10;
        }
        
        public void a() {
            this.b();
        }
        
        public void a(final View a) {
            this.a = a;
        }
        
        public void b() {
            this.a.setLayoutParams((ViewGroup$LayoutParams)new TableRow$LayoutParams(this.c(), this.b.a(bw.d.ag).getHeight()));
        }
    }
    
    public interface c
    {
        void a(final bz p0, final boolean p1);
    }
}
