// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.text.method.TransformationMethod;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;

public class AppCompatTextView extends TextView
{
    public AppCompatTextView(final Context context) {
        this(context, null);
    }
    
    public AppCompatTextView(final Context context, final AttributeSet set) {
        this(context, set, 16842884);
    }
    
    public AppCompatTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AppCompatTextView, n, 0);
        final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextView_android_textAppearance, -1);
        obtainStyledAttributes.recycle();
        if (resourceId != -1) {
            final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, R.styleable.TextAppearance);
            if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                this.setAllCaps(obtainStyledAttributes2.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
            }
            obtainStyledAttributes2.recycle();
        }
        final TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(set, R.styleable.AppCompatTextView, n, 0);
        if (obtainStyledAttributes3.hasValue(R.styleable.AppCompatTextView_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes3.getBoolean(R.styleable.AppCompatTextView_textAllCaps, false));
        }
        obtainStyledAttributes3.recycle();
    }
    
    public void setAllCaps(final boolean b) {
        Object transformationMethod;
        if (b) {
            transformationMethod = new AllCapsTransformationMethod(this.getContext());
        }
        else {
            transformationMethod = null;
        }
        this.setTransformationMethod((TransformationMethod)transformationMethod);
    }
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        obtainStyledAttributes.recycle();
    }
}
