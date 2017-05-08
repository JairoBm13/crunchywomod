// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import android.annotation.SuppressLint;
import android.os.Build$VERSION;
import com.swrve.sdk.conversations.engine.model.ControlBase;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Paint$Style;
import android.graphics.drawable.shapes.Shape;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils$TruncateAt;
import android.util.AttributeSet;
import android.content.Context;
import com.swrve.sdk.conversations.engine.model.ButtonControl;
import android.widget.Button;

public class ConversationButton extends Button implements ConversationControl
{
    private int backgroundColor;
    private int backgroundColorPressed;
    private ButtonControl model;
    private int textColor;
    private int textColorPressed;
    
    public ConversationButton(final Context context, final ButtonControl model, final int n) {
        super(context, (AttributeSet)null, n);
        if (model != null) {
            this.model = model;
            this.setText((CharSequence)model.getDescription());
        }
        this.initColors();
        this.initTextColorStates();
        this.initBackgroundColorStates();
        this.setLines(1);
        this.setEllipsize(TextUtils$TruncateAt.END);
    }
    
    private Drawable getOutlineDrawable(final int color, final int color2) {
        final RectF rectF = new RectF(6.0f, 6.0f, 6.0f, 6.0f);
        final float[] array2;
        final float[] array = array2 = new float[8];
        array2[1] = (array2[0] = 1.0f);
        array2[3] = (array2[2] = 1.0f);
        array2[5] = (array2[4] = 1.0f);
        array2[7] = (array2[6] = 1.0f);
        final ShapeDrawable shapeDrawable = new ShapeDrawable((Shape)new RoundRectShape(array, rectF, array));
        final Paint paint = shapeDrawable.getPaint();
        paint.setColor(color);
        paint.setStyle(Paint$Style.FILL);
        paint.setStrokeWidth(6.0f);
        final GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color2);
        return (Drawable)new LayerDrawable(new Drawable[] { gradientDrawable, shapeDrawable });
    }
    
    private StateListDrawable getStateListDrawable(final Drawable drawable, final Drawable drawable2, final Drawable drawable3) {
        final StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { 16842919 }, drawable);
        stateListDrawable.addState(new int[] { 16842908 }, drawable2);
        stateListDrawable.addState(new int[0], drawable3);
        return stateListDrawable;
    }
    
    private void initBackgroundColorStates() {
        Object backgroundForOs = new ColorDrawable();
        if (this.model.getStyle().isSolidStyle()) {
            final GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(this.backgroundColor);
            final GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setColor(this.backgroundColorPressed);
            backgroundForOs = this.getStateListDrawable((Drawable)gradientDrawable2, (Drawable)gradientDrawable2, (Drawable)gradientDrawable);
        }
        else if (this.model.getStyle().isOutlineStyle()) {
            final Drawable outlineDrawable = this.getOutlineDrawable(this.textColor, this.backgroundColor);
            final Drawable outlineDrawable2 = this.getOutlineDrawable(this.textColorPressed, this.backgroundColorPressed);
            backgroundForOs = this.getStateListDrawable(outlineDrawable2, outlineDrawable2, outlineDrawable);
        }
        this.setBackgroundForOs((Drawable)backgroundForOs);
    }
    
    private void initColors() {
        final int n = -16777216;
        this.textColor = this.model.getStyle().getTextColorInt();
        final int textColor = this.textColor;
        int n2;
        if (this.isLight(this.textColor)) {
            n2 = -16777216;
        }
        else {
            n2 = -1;
        }
        this.textColorPressed = this.lerpColor(textColor, n2, 0.3f);
        this.backgroundColor = this.model.getStyle().getBgColorInt();
        final int backgroundColor = this.backgroundColor;
        int n3;
        if (this.isLight(this.backgroundColor)) {
            n3 = n;
        }
        else {
            n3 = -1;
        }
        this.backgroundColorPressed = this.lerpColor(backgroundColor, n3, 0.3f);
    }
    
    private void initTextColorStates() {
        this.setTextColor(new ColorStateList(new int[][] { { 16842919 }, { 16842908 }, new int[0] }, new int[] { this.textColorPressed, this.textColorPressed, this.textColor }));
    }
    
    private boolean isLight(final int n) {
        return 0.2126 * (Color.red(n) / 255.0f) + 0.7152 * (Color.green(n) / 255.0f) + 0.0722 * (Color.blue(n) / 255.0f) > 0.5;
    }
    
    private float lerp(final float n, final float n2, final float n3) {
        return (n2 - n) * n3 + n;
    }
    
    private int lerpColor(final int n, final int n2, final float n3) {
        int rgb = n;
        if (n != 0) {
            rgb = Color.rgb((int)(byte)this.lerp(Color.red(n), Color.red(n2), n3), (int)(byte)this.lerp(Color.green(n), Color.green(n2), n3), (int)(byte)this.lerp(Color.blue(n), Color.blue(n2), n3));
        }
        return rgb;
    }
    
    public ButtonControl getModel() {
        return this.model;
    }
    
    @SuppressLint({ "NewApi" })
    public void setBackgroundForOs(final Drawable drawable) {
        if (Build$VERSION.SDK_INT < 16) {
            this.setBackgroundDrawable(drawable);
            return;
        }
        this.setBackground(drawable);
    }
}
