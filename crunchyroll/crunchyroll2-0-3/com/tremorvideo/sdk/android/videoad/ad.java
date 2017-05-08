// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.graphics.Paint;
import android.os.Build;
import android.graphics.Rect;
import android.view.View;
import android.text.method.TransformationMethod;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils$TruncateAt;
import android.view.View$OnClickListener;
import android.content.Context;
import android.widget.Button;

public class ad extends Button
{
    int a;
    int b;
    int c;
    int d;
    aw e;
    
    public ad(final Context context, final View$OnClickListener view$OnClickListener, final aw aw, final bw bw) {
        super(context);
        this.b = 0;
        this.c = 30;
        this.d = 0;
        this.setTextSize(2, 13.0f);
        this.setTextColor(-1);
        this.setMaxLines(2);
        this.setEllipsize(TextUtils$TruncateAt.END);
        this.a("You can skip this ad");
        final int densityDpi = this.getResources().getDisplayMetrics().densityDpi;
        int height;
        if (densityDpi <= 120) {
            height = 36;
            this.setTextSize(2, 15.0f);
            this.c = 34;
        }
        else if (densityDpi <= 160) {
            height = 44;
            this.setTextSize(2, 14.0f);
            this.c = 40;
        }
        else if (densityDpi <= 240) {
            height = 60;
            this.setTextSize(2, 14.0f);
            this.c = 50;
        }
        else if (densityDpi <= 320) {
            height = 86;
            this.setTextSize(2, 14.0f);
            this.c = 64;
        }
        else if (densityDpi <= 480) {
            height = 120;
            this.setTextSize(2, 13.0f);
            this.c = 80;
        }
        else {
            height = 150;
            this.setTextSize(2, 13.0f);
            this.c = 100;
        }
        this.setHeight(height);
        this.setWidth(3 * height);
        final GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(-16777216);
        gradientDrawable.setStroke(2, -1);
        if (Build$VERSION.SDK_INT < 16) {
            this.setBackgroundDrawable((Drawable)gradientDrawable);
        }
        else {
            this.setBackground((Drawable)gradientDrawable);
        }
        this.setTransformationMethod((TransformationMethod)null);
        this.setTag((Object)aw);
        this.e = aw;
        this.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (ad.this.d != 0) {
                    view$OnClickListener.onClick(view);
                }
            }
        });
        this.a = Integer.MIN_VALUE;
    }
    
    private Rect a(final String s) {
        final Rect rect = new Rect();
        ((Paint)this.getPaint()).getTextBounds(s, 0, s.length(), rect);
        return rect;
    }
    
    private String a(final String s, final int n) {
        return s.replaceFirst("#", Integer.toString(n));
    }
    
    private String a(String s, String string, String s2, String s3, final String s4, final int n) {
        final Rect a = this.a(string);
        final Rect a2 = this.a(s2);
        final Rect a3 = this.a(s3);
        string = "";
        final String[] split = s.split("[\\s\\n]");
        if (split.length != 0) {
            string = "";
            String s5 = "";
            s = split[0];
            int n2 = 1;
            while (true) {
                while (this.a(s).width() <= a.width()) {
                    if (n2 < split.length) {
                        s2 = s + " " + split[n2];
                        ++n2;
                        string = s;
                        s = s2;
                    }
                    else {
                        final Boolean value = true;
                        final String s6 = s;
                        final int n3 = n2 - 1;
                        Boolean b = value;
                        s3 = s5;
                        if (!value) {
                            b = value;
                            s3 = s5;
                            if (n3 != 0) {
                                s = split[n3];
                                int n4 = n3 + 1;
                                Boolean value2;
                                while (true) {
                                    value2 = value;
                                    if (this.a(s).width() > a2.width()) {
                                        break;
                                    }
                                    if (n4 >= split.length) {
                                        value2 = true;
                                        s5 = s;
                                        break;
                                    }
                                    s2 = s + " " + split[n4];
                                    ++n4;
                                    s5 = s;
                                    s = s2;
                                }
                                b = value2;
                                s3 = s5;
                                if (!value2) {
                                    b = value2;
                                    s3 = s5;
                                    if (n4 == split.length) {
                                        if (n == 1 && this.a(s).width() <= a.width()) {
                                            b = true;
                                            s3 = s;
                                        }
                                        else {
                                            b = value2;
                                            s3 = s5;
                                            if (n == 2) {
                                                b = value2;
                                                s3 = s5;
                                                if (this.a(s).width() <= a3.width()) {
                                                    b = true;
                                                    s3 = s;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        s = s6;
                        Label_0423: {
                            if (!s6.equals("")) {
                                string = s6 + " " + "\u2026";
                                if (s3.equals("")) {
                                    s = s6;
                                    if (!s3.equals("")) {
                                        break Label_0423;
                                    }
                                    s = s6;
                                    if (b) {
                                        break Label_0423;
                                    }
                                    s = s6;
                                    if (this.a(string).width() <= a.width()) {
                                        break Label_0423;
                                    }
                                }
                                s = s6 + " ";
                            }
                        }
                        string = (s += s3);
                        if (!b) {
                            return string + " " + s4;
                        }
                        return s;
                    }
                }
                final Boolean value3 = false;
                final String s6 = string;
                final Boolean value = value3;
                continue;
            }
        }
        s = string;
        return s;
    }
    
    private aw c() {
        return this.e;
    }
    
    public void a() {
        this.getBackground().setAlpha(153);
        this.setTextColor(this.getTextColors().withAlpha(153));
        this.setHintTextColor(this.getHintTextColors().withAlpha(153));
        this.setLinkTextColor(this.getLinkTextColors().withAlpha(153));
    }
    
    public void a(final int a) {
        if (a != this.a) {
            this.a = a;
            this.setPadding(8, 0, 8, 0);
            if (this.a > 0) {
                this.d = 0;
                this.setText((CharSequence)this.a(this.a(this.c().a("countdown-text", "You can skip this ad by #s"), this.a), "You can skip this", "ad by 0s the e", "You can skip this", "\u2026", 1));
                return;
            }
            if (this.d == 0) {
                this.b = 0;
                this.d = 1;
                final String a2 = this.c().a("text", "Skip");
                final String manufacturer = Build.MANUFACTURER;
                final String model = Build.MODEL;
                String s;
                if (manufacturer.equals("samsung") && model.equals("GT-I9100")) {
                    s = "\u25b6\ufe33";
                }
                else if (manufacturer.equals("LGE") || manufacturer.equals("samsung")) {
                    s = "\u25ba\u2759";
                }
                else {
                    s = "\u25b6\u2759";
                }
                this.setText((CharSequence)(this.a(a2, "You can skip this", "ad by 0s t", "You can skip ", "\u2026", 2) + " " + s));
                if (this.a < -2) {
                    this.d = 2;
                    this.a();
                }
            }
            else if (this.d == 1) {
                ++this.b;
                if (this.b >= 3) {
                    this.d = 2;
                    this.a();
                }
            }
        }
    }
    
    public boolean b() {
        return this.d != 0;
    }
}
