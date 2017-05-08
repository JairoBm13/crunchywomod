// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.text;

import android.util.Log;
import com.google.android.exoplayer.util.Util;
import android.text.TextUtils;
import android.graphics.Paint$Join;
import android.graphics.Color;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.graphics.Paint$Style;
import android.util.AttributeSet;
import android.content.Context;
import android.text.TextPaint;
import android.text.StaticLayout;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout$Alignment;

final class CuePainter
{
    private boolean applyEmbeddedStyles;
    private int backgroundColor;
    private float bottomPaddingFraction;
    private final float cornerRadius;
    private float cueLine;
    private int cueLineAnchor;
    private int cueLineType;
    private float cuePosition;
    private int cuePositionAnchor;
    private float cueSize;
    private CharSequence cueText;
    private Layout$Alignment cueTextAlignment;
    private int edgeColor;
    private int edgeType;
    private int foregroundColor;
    private final RectF lineBounds;
    private final float outlineWidth;
    private final Paint paint;
    private int parentBottom;
    private int parentLeft;
    private int parentRight;
    private int parentTop;
    private final float shadowOffset;
    private final float shadowRadius;
    private final float spacingAdd;
    private final float spacingMult;
    private StaticLayout textLayout;
    private int textLeft;
    private int textPaddingX;
    private final TextPaint textPaint;
    private float textSizePx;
    private int textTop;
    private int windowColor;
    
    public CuePainter(final Context context) {
        this.lineBounds = new RectF();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes((AttributeSet)null, new int[] { 16843287, 16843288 }, 0, 0);
        this.spacingAdd = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        this.spacingMult = obtainStyledAttributes.getFloat(1, 1.0f);
        obtainStyledAttributes.recycle();
        final int round = Math.round(2.0f * context.getResources().getDisplayMetrics().densityDpi / 160.0f);
        this.cornerRadius = round;
        this.outlineWidth = round;
        this.shadowRadius = round;
        this.shadowOffset = round;
        (this.textPaint = new TextPaint()).setAntiAlias(true);
        this.textPaint.setSubpixelText(true);
        (this.paint = new Paint()).setAntiAlias(true);
        this.paint.setStyle(Paint$Style.FILL);
    }
    
    private static boolean areCharSequencesEqual(final CharSequence charSequence, final CharSequence charSequence2) {
        return charSequence == charSequence2 || (charSequence != null && charSequence.equals(charSequence2));
    }
    
    private void drawLayout(final Canvas canvas) {
        final StaticLayout textLayout = this.textLayout;
        if (textLayout == null) {
            return;
        }
        final int save = canvas.save();
        canvas.translate((float)this.textLeft, (float)this.textTop);
        if (Color.alpha(this.windowColor) > 0) {
            this.paint.setColor(this.windowColor);
            canvas.drawRect((float)(-this.textPaddingX), 0.0f, (float)(textLayout.getWidth() + this.textPaddingX), (float)textLayout.getHeight(), this.paint);
        }
        if (Color.alpha(this.backgroundColor) > 0) {
            this.paint.setColor(this.backgroundColor);
            float bottom = textLayout.getLineTop(0);
            for (int lineCount = textLayout.getLineCount(), i = 0; i < lineCount; ++i) {
                this.lineBounds.left = textLayout.getLineLeft(i) - this.textPaddingX;
                this.lineBounds.right = textLayout.getLineRight(i) + this.textPaddingX;
                this.lineBounds.top = bottom;
                this.lineBounds.bottom = textLayout.getLineBottom(i);
                bottom = this.lineBounds.bottom;
                canvas.drawRoundRect(this.lineBounds, this.cornerRadius, this.cornerRadius, this.paint);
            }
        }
        if (this.edgeType == 1) {
            this.textPaint.setStrokeJoin(Paint$Join.ROUND);
            this.textPaint.setStrokeWidth(this.outlineWidth);
            this.textPaint.setColor(this.edgeColor);
            this.textPaint.setStyle(Paint$Style.FILL_AND_STROKE);
            textLayout.draw(canvas);
        }
        else if (this.edgeType == 2) {
            this.textPaint.setShadowLayer(this.shadowRadius, this.shadowOffset, this.shadowOffset, this.edgeColor);
        }
        else if (this.edgeType == 3 || this.edgeType == 4) {
            boolean b;
            if (this.edgeType == 3) {
                b = true;
            }
            else {
                b = false;
            }
            int edgeColor;
            if (b) {
                edgeColor = -1;
            }
            else {
                edgeColor = this.edgeColor;
            }
            int edgeColor2;
            if (b) {
                edgeColor2 = this.edgeColor;
            }
            else {
                edgeColor2 = -1;
            }
            final float n = this.shadowRadius / 2.0f;
            this.textPaint.setColor(this.foregroundColor);
            this.textPaint.setStyle(Paint$Style.FILL);
            this.textPaint.setShadowLayer(this.shadowRadius, -n, -n, edgeColor);
            textLayout.draw(canvas);
            this.textPaint.setShadowLayer(this.shadowRadius, n, n, edgeColor2);
        }
        this.textPaint.setColor(this.foregroundColor);
        this.textPaint.setStyle(Paint$Style.FILL);
        textLayout.draw(canvas);
        this.textPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        canvas.restoreToCount(save);
    }
    
    public void draw(final Cue cue, final boolean applyEmbeddedStyles, final CaptionStyleCompat captionStyleCompat, final float n, final float bottomPaddingFraction, final Canvas canvas, int i, int n2, int max, int parentBottom) {
        final CharSequence text = cue.text;
        if (TextUtils.isEmpty(text)) {
            return;
        }
        CharSequence string = text;
        if (!applyEmbeddedStyles) {
            string = text.toString();
        }
        if (areCharSequencesEqual(this.cueText, string) && Util.areEqual(this.cueTextAlignment, cue.textAlignment) && this.cueLine == cue.line && this.cueLineType == cue.lineType && Util.areEqual(this.cueLineAnchor, cue.lineAnchor) && this.cuePosition == cue.position && Util.areEqual(this.cuePositionAnchor, cue.positionAnchor) && this.cueSize == cue.size && this.applyEmbeddedStyles == applyEmbeddedStyles && this.foregroundColor == captionStyleCompat.foregroundColor && this.backgroundColor == captionStyleCompat.backgroundColor && this.windowColor == captionStyleCompat.windowColor && this.edgeType == captionStyleCompat.edgeType && this.edgeColor == captionStyleCompat.edgeColor && Util.areEqual(this.textPaint.getTypeface(), captionStyleCompat.typeface) && this.textSizePx == n && this.bottomPaddingFraction == bottomPaddingFraction && this.parentLeft == i && this.parentTop == n2 && this.parentRight == max && this.parentBottom == parentBottom) {
            this.drawLayout(canvas);
            return;
        }
        this.cueText = string;
        this.cueTextAlignment = cue.textAlignment;
        this.cueLine = cue.line;
        this.cueLineType = cue.lineType;
        this.cueLineAnchor = cue.lineAnchor;
        this.cuePosition = cue.position;
        this.cuePositionAnchor = cue.positionAnchor;
        this.cueSize = cue.size;
        this.applyEmbeddedStyles = applyEmbeddedStyles;
        this.foregroundColor = captionStyleCompat.foregroundColor;
        this.backgroundColor = captionStyleCompat.backgroundColor;
        this.windowColor = captionStyleCompat.windowColor;
        this.edgeType = captionStyleCompat.edgeType;
        this.edgeColor = captionStyleCompat.edgeColor;
        this.textPaint.setTypeface(captionStyleCompat.typeface);
        this.textSizePx = n;
        this.bottomPaddingFraction = bottomPaddingFraction;
        this.parentLeft = i;
        this.parentTop = n2;
        this.parentRight = max;
        this.parentBottom = parentBottom;
        max = this.parentRight - this.parentLeft;
        final int n3 = this.parentBottom - this.parentTop;
        this.textPaint.setTextSize(n);
        final int textPaddingX = (int)(0.125f * n + 0.5f);
        n2 = (i = max - textPaddingX * 2);
        if (this.cueSize != Float.MIN_VALUE) {
            i = (int)(n2 * this.cueSize);
        }
        if (i <= 0) {
            Log.w("CuePainter", "Skipped drawing subtitle cue (insufficient space)");
            return;
        }
        Layout$Alignment layout$Alignment;
        if (this.cueTextAlignment == null) {
            layout$Alignment = Layout$Alignment.ALIGN_CENTER;
        }
        else {
            layout$Alignment = this.cueTextAlignment;
        }
        this.textLayout = new StaticLayout(string, this.textPaint, i, layout$Alignment, this.spacingMult, this.spacingAdd, true);
        final int height = this.textLayout.getHeight();
        n2 = 0;
        for (parentBottom = this.textLayout.getLineCount(), i = 0; i < parentBottom; ++i) {
            n2 = Math.max((int)Math.ceil(this.textLayout.getLineWidth(i)), n2);
        }
        n2 += textPaddingX * 2;
        if (this.cuePosition != Float.MIN_VALUE) {
            i = Math.round(max * this.cuePosition) + this.parentLeft;
            if (this.cuePositionAnchor == 2) {
                i -= n2;
            }
            else if (this.cuePositionAnchor == 1) {
                i = (i * 2 - n2) / 2;
            }
            max = Math.max(i, this.parentLeft);
            parentBottom = Math.min(max + n2, this.parentRight);
        }
        else {
            max = (max - n2) / 2;
            parentBottom = max + n2;
        }
        if (this.cueLine != Float.MIN_VALUE) {
            if (this.cueLineType == 0) {
                i = Math.round(n3 * this.cueLine) + this.parentTop;
            }
            else {
                i = this.textLayout.getLineBottom(0) - this.textLayout.getLineTop(0);
                if (this.cueLine >= 0.0f) {
                    i = Math.round(this.cueLine * i) + this.parentTop;
                }
                else {
                    i = Math.round(this.cueLine * i) + this.parentBottom;
                }
            }
            if (this.cueLineAnchor == 2) {
                i -= height;
            }
            else if (this.cueLineAnchor == 1) {
                i = (i * 2 - height) / 2;
            }
            if (i + height > this.parentBottom) {
                n2 = this.parentBottom - height;
                i = this.parentBottom;
            }
            else if ((n2 = i) < this.parentTop) {
                n2 = this.parentTop;
                i = this.parentTop;
            }
        }
        else {
            n2 = this.parentBottom - height - (int)(n3 * bottomPaddingFraction);
        }
        this.textLayout = new StaticLayout(string, this.textPaint, parentBottom - max, layout$Alignment, this.spacingMult, this.spacingAdd, true);
        this.textLeft = max;
        this.textTop = n2;
        this.textPaddingX = textPaddingX;
        this.drawLayout(canvas);
    }
}
