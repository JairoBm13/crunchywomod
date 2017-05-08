// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.Display;
import android.view.WindowManager;
import android.content.Context;
import android.widget.VideoView;

public class ap extends VideoView
{
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    
    ap(final Context context, final int a, final int b, final int c) {
        super(context);
        this.a = 0;
        this.d = 0;
        this.e = 0;
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    ap(final Context context, final int a, final int b, final int c, final int d, final int e) {
        super(context);
        this.a = 0;
        this.d = 0;
        this.e = 0;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    public int getId() {
        return 48879;
    }
    
    protected void onMeasure(int n, int n2) {
        float n3;
        float n4;
        if (this.d > 0 && this.e > 0) {
            n3 = this.d;
            n4 = this.e;
        }
        else {
            final Display defaultDisplay = ((WindowManager)this.getContext().getSystemService("window")).getDefaultDisplay();
            n3 = defaultDisplay.getWidth();
            n4 = defaultDisplay.getHeight() - this.a;
        }
        final float n5 = this.b / this.c;
        if (n5 > n3 / n4) {
            n2 = (int)n3;
            n = (int)(n3 / n5);
        }
        else {
            n2 = (int)(n5 * n4);
            n = (int)n4;
        }
        this.setMeasuredDimension(n2, n);
    }
}
