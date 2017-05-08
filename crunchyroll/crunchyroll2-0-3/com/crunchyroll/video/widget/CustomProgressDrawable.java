// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.video.widget;

import android.view.Gravity;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.Paint$Style;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.Drawable;
import java.util.List;
import android.content.Context;

public class CustomProgressDrawable
{
    public static Drawable createDrawable(final Context context, final List<Double> list, final int n) {
        final ShapeDrawable shapeDrawable = new ShapeDrawable() {
            public void draw(final Canvas canvas) {
                final Rect bounds = this.getBounds();
                this.getPaint().setColor(context.getResources().getColor(2131558500));
                super.draw(canvas);
                this.getPaint().setColor(context.getResources().getColor(17170443));
                if (list != null) {
                    for (int i = 0; i < list.size(); ++i) {
                        if (n != 0) {
                            final float n = (float)(Object)list.get(i) * bounds.width() / n;
                            canvas.drawRect(n - bounds.height() / 2, (float)bounds.top, n + bounds.height() / 2, (float)bounds.bottom, this.getPaint());
                        }
                    }
                }
            }
        };
        shapeDrawable.getPaint().setStyle(Paint$Style.FILL);
        shapeDrawable.getPaint().setColor(context.getResources().getColor(2131558500));
        return (Drawable)new LayerDrawable(new Drawable[] { shapeDrawable, createProgressBar(context, list, n, 2131558477) });
    }
    
    private static ClipDrawable createProgressBar(final Context context, final List<Double> list, final int n, final int n2) {
        final ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setStyle(Paint$Style.FILL);
        shapeDrawable.getPaint().setColor(context.getResources().getColor(n2));
        final Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(17170443));
        return new ClipDrawable(shapeDrawable, 3, 1) {
            private Rect r = new Rect();
            
            public void draw(final Canvas canvas) {
                super.draw(canvas);
                if (this.getLevel() != 0 && list != null) {
                    final Rect bounds = this.getBounds();
                    final int level = this.getLevel();
                    final int width = bounds.width();
                    final int n = width - (width + 0) * (10000 - level) / 10000;
                    final int n2 = bounds.top * 2 + bounds.height();
                    Gravity.apply(3, n, n2, bounds, this.r);
                    if (n > 0 && n2 > 0) {
                        final float n3 = bounds.height() * 1.5f;
                        canvas.save();
                        final Rect r = this.r;
                        r.left -= (int)n3;
                        final Rect r2 = this.r;
                        r2.right += (int)n3;
                        canvas.clipRect(this.r);
                        for (int i = 0; i < list.size(); ++i) {
                            if (n != 0) {
                                final float n4 = (float)(Object)list.get(i) * bounds.width() / n;
                                canvas.drawRect(n4 - bounds.height() / 2, (float)bounds.top, n4 + bounds.height() / 2, (float)bounds.bottom, paint);
                            }
                        }
                        canvas.restore();
                    }
                }
            }
        };
    }
}
