// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.animation;

import android.os.Build$VERSION;

public abstract class AnimatorCompatHelper
{
    static AnimatorProvider IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 12) {
            AnimatorCompatHelper.IMPL = new HoneycombMr1AnimatorCompatProvider();
            return;
        }
        AnimatorCompatHelper.IMPL = new DonutAnimatorCompatProvider();
    }
    
    public static ValueAnimatorCompat emptyValueAnimator() {
        return AnimatorCompatHelper.IMPL.emptyValueAnimator();
    }
}
