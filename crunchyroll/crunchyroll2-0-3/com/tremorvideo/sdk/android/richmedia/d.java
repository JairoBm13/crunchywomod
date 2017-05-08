// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import com.tremorvideo.sdk.android.videoad.ac;
import android.graphics.Canvas;
import android.content.Context;
import android.view.View;

public class d extends View
{
    m a;
    
    public d(final Context context, final a a) {
        super(context);
    }
    
    public void a(final m a) {
        this.a = a;
    }
    
    protected int getSuggestedMinimumHeight() {
        return 0;
    }
    
    protected int getSuggestedMinimumWidth() {
        return 0;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
        while (true) {
            try {
                this.a.c(canvas);
                canvas.restore();
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
}
