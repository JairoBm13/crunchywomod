// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import java.util.Iterator;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.View$OnFocusChangeListener;
import android.widget.Button;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable$Orientation;
import java.util.Collections;
import java.util.Comparator;
import android.graphics.Color;
import android.view.View$OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ScrollView;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.widget.LinearLayout;
import android.app.Dialog;
import android.content.Context;
import org.json.JSONArray;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.List;

public class be
{
    static boolean e;
    List<aw> a;
    String b;
    long c;
    boolean d;
    
    static {
        be.e = false;
    }
    
    public be(final JSONObject jsonObject) throws Exception {
        if (jsonObject.has("survey-skip-seconds")) {
            this.c = 1000L * jsonObject.getLong("survey-skip-seconds");
        }
        else {
            this.c = 0L;
        }
        if (jsonObject.has("survey-skip")) {
            this.d = jsonObject.getBoolean("survey-skip");
        }
        else {
            this.d = false;
        }
        this.b = jsonObject.getString("survey-question");
        final JSONArray jsonArray = jsonObject.getJSONArray("events");
        this.a = new ArrayList<aw>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); ++i) {
            this.a.add(new aw(jsonArray.getJSONObject(i)));
        }
        be.e = false;
    }
    
    public static Dialog a(final Context context, final bw bw, final be be, final a.a a) {
        try {
            final ai a2 = ai.a(context);
            final Bitmap[] a3 = a(bw);
            final ScrollView a4 = a(context, bw, a, a2, be);
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setPadding(10, 10, 10, 10);
            final LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(0);
            final TextView a5 = a(context, bw, "Survey");
            a5.setFocusable(false);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -2);
            linearLayout$LayoutParams.weight = 1.0f;
            linearLayout2.addView((View)a5, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            final ImageButton a6 = a(context, bw, a, be, a2);
            a6.setFocusable(false);
            final LinearLayout$LayoutParams linearLayout$LayoutParams2 = new LinearLayout$LayoutParams(-2, -2);
            linearLayout$LayoutParams2.gravity = 5;
            linearLayout2.addView((View)a6, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
            linearLayout.addView((View)linearLayout2, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
            linearLayout.addView((View)b(context, bw, be.b));
            linearLayout.addView((View)a4);
            a2.requestWindowFeature(1);
            a2.getWindow().setBackgroundDrawable((Drawable)new al(a3, bw.a(bw.c.f)));
            a2.getWindow().setFlags(1024, 1024);
            a2.addContentView((View)linearLayout, new ViewGroup$LayoutParams(-1, -1));
            a2.setCanceledOnTouchOutside(false);
            return a2;
        }
        catch (Exception ex) {
            ac.a(ex);
            return null;
        }
    }
    
    private static ImageButton a(final Context context, final bw bw, final a.a a, final be be, final Dialog dialog) {
        final Bitmap a2 = bw.a(bw.d.as);
        final ImageButton imageButton = new ImageButton(context);
        imageButton.setBackgroundDrawable((Drawable)null);
        imageButton.setImageBitmap(a2);
        if (be.d && be.c > 0L) {
            imageButton.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    b(dialog, a, be);
                }
            }, be.c);
        }
        imageButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                b(dialog, a, be);
            }
        });
        return imageButton;
    }
    
    private static ScrollView a(final Context context, final bw bw, final a.a a, final Dialog dialog, final be be) {
        final int[] array = { Color.rgb(156, 157, 158), Color.rgb(39, 40, 41), Color.rgb(59, 60, 63) };
        final int[] array2 = { Color.rgb(68, 121, 254), Color.rgb(4, 63, 212) };
        final ScrollView scrollView = new ScrollView(context);
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        final ArrayList<aw> list = new ArrayList<aw>();
        int n = 0;
        int n2;
        for (int i = 0; i < be.a.size(); ++i, n = n2) {
            n2 = n;
            if (be.a.get(i).a() == aw.b.R) {
                final int n3 = n + 1;
                list.add(be.a.get(i));
                if ((n2 = n3) == 10) {
                    break;
                }
            }
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<aw>() {
            public int a(final aw aw, final aw aw2) {
                if (aw.i() < aw2.i()) {
                    return -1;
                }
                if (aw.i() > aw2.i()) {
                    return 1;
                }
                return 0;
            }
        });
        for (int j = 0; j < list.size(); ++j) {
            final GradientDrawable backgroundDrawable = new GradientDrawable(GradientDrawable$Orientation.TOP_BOTTOM, array);
            backgroundDrawable.setCornerRadius(6.0f);
            backgroundDrawable.setGradientType(0);
            final Button button = new Button(context);
            button.setText((CharSequence)list.get(j).h());
            button.setTag(list.get(j));
            button.setBackgroundDrawable((Drawable)backgroundDrawable);
            button.setTextColor(-1);
            button.setPadding(5, 5, 5, 5);
            button.setId(j + 35);
            button.setFocusable(true);
            final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(-1, -2);
            layoutParams.bottomMargin = 5;
            button.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    be.e = true;
                    a.a((aw)view.getTag());
                    dialog.dismiss();
                }
            });
            button.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
                public void onFocusChange(final View view, final boolean b) {
                    int[] array;
                    if (b) {
                        array = array2;
                    }
                    else {
                        array = array;
                    }
                    final GradientDrawable backgroundDrawable = new GradientDrawable(GradientDrawable$Orientation.TOP_BOTTOM, array);
                    backgroundDrawable.setCornerRadius(5.0f);
                    backgroundDrawable.setGradientType(0);
                    view.setBackgroundDrawable((Drawable)backgroundDrawable);
                }
            });
            button.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    int[] array = null;
                    if (motionEvent.getAction() == 0) {
                        array = array2;
                    }
                    else if (motionEvent.getAction() == 3) {
                        array = array;
                    }
                    else if (motionEvent.getAction() == 1) {
                        array = array;
                    }
                    if (array != null) {
                        final GradientDrawable backgroundDrawable = new GradientDrawable(GradientDrawable$Orientation.TOP_BOTTOM, array);
                        backgroundDrawable.setCornerRadius(5.0f);
                        backgroundDrawable.setGradientType(0);
                        view.setBackgroundDrawable((Drawable)backgroundDrawable);
                    }
                    return false;
                }
            });
            linearLayout.addView((View)button);
            linearLayout.setPadding(5, 5, 5, 5);
        }
        scrollView.addView((View)linearLayout);
        return scrollView;
    }
    
    private static TextView a(final Context context, final bw bw, final String text) {
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)text);
        textView.setTextSize(2, (float)ac.I());
        textView.setTextColor(bw.a(bw.c.g));
        textView.setTypeface(Typeface.create("helvetica", 1));
        textView.setShadowLayer(5.0f, 0.0f, 0.0f, bw.a(bw.c.h));
        textView.setPadding(10, 5, 10, 0);
        return textView;
    }
    
    private static aw a(final aw.b b, final List<aw> list) {
        for (final aw aw : list) {
            if (aw.a() == b) {
                return aw;
            }
        }
        return null;
    }
    
    private static Bitmap[] a(final bw bw) {
        final Bitmap[] array = new Bitmap[al.a.values().length];
        array[al.a.a.ordinal()] = bw.a(bw.d.V);
        array[al.a.b.ordinal()] = bw.a(bw.d.W);
        array[al.a.c.ordinal()] = bw.a(bw.d.X);
        array[al.a.d.ordinal()] = bw.a(bw.d.Y);
        array[al.a.e.ordinal()] = bw.a(bw.d.Z);
        array[al.a.f.ordinal()] = bw.a(bw.d.aa);
        array[al.a.g.ordinal()] = bw.a(bw.d.ab);
        array[al.a.h.ordinal()] = bw.a(bw.d.ac);
        return array;
    }
    
    private static TextView b(final Context context, final bw bw, final String text) {
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)text);
        textView.setPadding(10, 5, 10, 5);
        textView.setTextColor(bw.a(bw.c.i));
        textView.setTextSize((float)ac.J());
        return textView;
    }
    
    private static void b(final Dialog dialog, final a.a a, final be be) {
        synchronized (be.class) {
            if (!be.e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                final aw a2 = a(aw.b.Q, be.a);
                if (a2 != null) {
                    be.a.remove(a2);
                    a.a(a2);
                }
            }
        }
    }
    
    public aw a(final aw.b b) {
        for (final aw aw : this.a) {
            if (aw.a() == b) {
                return aw;
            }
        }
        return null;
    }
}
