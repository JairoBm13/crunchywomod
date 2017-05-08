// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.widget.TableLayout$LayoutParams;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.View$OnFocusChangeListener;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable$Orientation;
import android.widget.TableRow$LayoutParams;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.graphics.Color;
import android.widget.ScrollView;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.app.Dialog;
import android.content.Context;

public class am
{
    private static int a() {
        int n = a(300);
        if (Math.max(ac.n(), ac.m()) > 320) {
            n = Math.min(Math.round(ac.m() * 0.6f), n);
        }
        return Math.round(n * 0.9f);
    }
    
    private static int a(final int n) {
        return Math.round(Math.max(ac.n(), ac.m()) / 320.0f * n);
    }
    
    public static Dialog a(final Context context, final a a, final String s, final String text, final bw bw, final boolean b, int inputType, int n, final String[] array) {
        final ai a2 = ai.a(context);
        final Bitmap[] a3 = a(bw);
        final EditText editText = new EditText(context);
        final ScrollView a4 = a(context, editText, a, bw, a2, array);
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(10, 10, 10, 10);
        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        final TextView textView = new TextView(context);
        textView.setText((CharSequence)text);
        textView.setFocusable(false);
        linearLayout2.addView((View)textView, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
        editText.setFocusable(true);
        if (b) {
            editText.setText((CharSequence)"");
        }
        else {
            editText.setText((CharSequence)String.valueOf(s));
        }
        editText.setInputType(inputType);
        editText.setFilters(new InputFilter[] { new InputFilter$LengthFilter(n) });
        linearLayout2.addView((View)editText, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
        linearLayout.addView((View)linearLayout2, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -2));
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(0, 0);
        linearLayout$LayoutParams.weight = 1.0f;
        linearLayout.addView(new View(context), (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        final LinearLayout$LayoutParams linearLayout$LayoutParams2 = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams2.gravity = 81;
        linearLayout.addView((View)a4, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
        final int a5 = a();
        n = (inputType = a(100));
        if (Math.max(ac.n(), ac.m()) > 320) {
            inputType = Math.min(Math.round(ac.n() * 0.5f), n);
        }
        a2.requestWindowFeature(1);
        a2.getWindow().setBackgroundDrawable((Drawable)new al(a3, bw.a(bw.c.f)));
        a2.addContentView((View)linearLayout, new ViewGroup$LayoutParams(a5, inputType));
        return a2;
    }
    
    private static ScrollView a(final Context context, final EditText editText, final a a, final bw bw, final Dialog dialog, final String[] array) {
        final int[] array2 = { Color.rgb(156, 157, 158), Color.rgb(39, 40, 41), Color.rgb(59, 60, 63) };
        final int[] array3 = { Color.rgb(68, 121, 254), Color.rgb(4, 63, 212) };
        final ScrollView scrollView = new ScrollView(context);
        final TableLayout tableLayout = new TableLayout(context);
        final TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams((ViewGroup$LayoutParams)new TableRow$LayoutParams(-1, -2));
        String[] array4 = null;
        Label_0154: {
            if (array != null) {
                array4 = array;
                if (array.length >= 1) {
                    break Label_0154;
                }
            }
            array4 = new String[] { "   OK   ", "Cancel" };
        }
        for (int i = 0; i < array4.length; ++i) {
            final GradientDrawable backgroundDrawable = new GradientDrawable(GradientDrawable$Orientation.TOP_BOTTOM, array2);
            backgroundDrawable.setCornerRadius(6.0f);
            backgroundDrawable.setGradientType(0);
            final Button button = new Button(context);
            button.setText((CharSequence)array4[i]);
            button.setBackgroundDrawable((Drawable)backgroundDrawable);
            button.setTextColor(-1);
            button.setPadding(5, 5, 5, 5);
            button.setId(i + 35);
            button.setFocusable(true);
            button.setTag((Object)i);
            button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(View string) {
                    Label_0061: {
                        if ((int)string.getTag() != 0) {
                            break Label_0061;
                        }
                        string = null;
                        while (true) {
                            try {
                                string = (View)editText.getText().toString();
                                if (string == null) {
                                    a.a(dialog);
                                    Label_0045: {
                                        return;
                                    }
                                }
                                a.a(dialog, (String)string);
                                return;
                                // iftrue(Label_0045:, (Integer)string.getTag().intValue() != 1)
                                a.a(dialog);
                            }
                            catch (Exception ex) {
                                continue;
                            }
                            break;
                        }
                    }
                }
            });
            button.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
                public void onFocusChange(final View view, final boolean b) {
                    int[] array;
                    if (b) {
                        array = array3;
                    }
                    else {
                        array = array2;
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
                        array = array3;
                    }
                    else if (motionEvent.getAction() == 3) {
                        array = array2;
                    }
                    else if (motionEvent.getAction() == 1) {
                        array = array2;
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
            final TableRow$LayoutParams tableRow$LayoutParams = new TableRow$LayoutParams(-1, -2);
            tableRow$LayoutParams.gravity = 1;
            tableRow.addView((View)button, (ViewGroup$LayoutParams)tableRow$LayoutParams);
            tableLayout.setColumnStretchable(i, true);
        }
        tableLayout.addView((View)tableRow, (ViewGroup$LayoutParams)new TableLayout$LayoutParams(-1, -2));
        scrollView.addView((View)tableLayout, new ViewGroup$LayoutParams(-1, -2));
        return scrollView;
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
    
    public interface a
    {
        void a(final Dialog p0);
        
        void a(final Dialog p0, final String p1);
    }
}
