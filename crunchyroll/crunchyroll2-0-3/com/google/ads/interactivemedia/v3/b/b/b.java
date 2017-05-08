// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.b;

import android.graphics.Paint$Style;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class b extends FrameLayout
{
    private final float a;
    private final TextView b;
    
    public b(final Context context) {
        super(context);
        this.a = this.getResources().getDisplayMetrics().density;
        this.setBackgroundDrawable((Drawable)new a());
        final int a = this.a(8, this.a);
        this.setPadding(a, a, a, a);
        (this.b = new TextView(context)).setTextColor(-3355444);
        this.b.setIncludeFontPadding(false);
        this.b.setGravity(17);
        this.addView((View)this.b);
    }
    
    private int a(final int n, final float n2) {
        return (int)(n * n2 + 0.5f);
    }
    
    public void a(final String text) {
        this.b.setText((CharSequence)text);
    }
    
    private static class a extends ShapeDrawable
    {
        private Paint a;
        private Paint b;
        
        public a() {
            super((Shape)new Shape() {
                private Path a;
                
                public void draw(final Canvas canvas, final Paint paint) {
                    canvas.drawPath(this.a, paint);
                }
                
                public void onResize(final float n, final float n2) {
                    (this.a = new Path()).moveTo(this.getWidth(), this.getHeight());
                    this.a.lineTo((float)6, this.getHeight());
                    this.a.arcTo(new RectF(0.0f, this.getHeight() - 12, (float)12, this.getHeight()), 90.0f, 90.0f);
                    this.a.lineTo(0.0f, (float)6);
                    this.a.arcTo(new RectF(0.0f, 0.0f, (float)12, (float)12), 180.0f, 90.0f);
                    this.a.lineTo(this.getWidth(), 0.0f);
                }
            });
            (this.a = new Paint()).setAntiAlias(true);
            this.a.setStyle(Paint$Style.STROKE);
            this.a.setStrokeWidth(1.0f);
            this.a.setARGB(150, 255, 255, 255);
            (this.b = new Paint()).setStyle(Paint$Style.FILL);
            this.b.setColor(-16777216);
            this.b.setAlpha(140);
        }
        
        protected void onDraw(final Shape shape, final Canvas canvas, final Paint paint) {
            shape.draw(canvas, this.b);
            shape.draw(canvas, this.a);
        }
    }
}
