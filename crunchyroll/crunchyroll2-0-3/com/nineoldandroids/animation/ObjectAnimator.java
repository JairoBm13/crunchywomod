// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.view.animation.AnimatorProxy;
import java.util.HashMap;
import com.nineoldandroids.util.Property;
import java.util.Map;

public final class ObjectAnimator extends ValueAnimator
{
    private static final Map<String, Property> PROXY_PROPERTIES;
    private Property mProperty;
    private String mPropertyName;
    private Object mTarget;
    
    static {
        (PROXY_PROPERTIES = new HashMap<String, Property>()).put("alpha", PreHoneycombCompat.ALPHA);
        ObjectAnimator.PROXY_PROPERTIES.put("pivotX", PreHoneycombCompat.PIVOT_X);
        ObjectAnimator.PROXY_PROPERTIES.put("pivotY", PreHoneycombCompat.PIVOT_Y);
        ObjectAnimator.PROXY_PROPERTIES.put("translationX", PreHoneycombCompat.TRANSLATION_X);
        ObjectAnimator.PROXY_PROPERTIES.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
        ObjectAnimator.PROXY_PROPERTIES.put("rotation", PreHoneycombCompat.ROTATION);
        ObjectAnimator.PROXY_PROPERTIES.put("rotationX", PreHoneycombCompat.ROTATION_X);
        ObjectAnimator.PROXY_PROPERTIES.put("rotationY", PreHoneycombCompat.ROTATION_Y);
        ObjectAnimator.PROXY_PROPERTIES.put("scaleX", PreHoneycombCompat.SCALE_X);
        ObjectAnimator.PROXY_PROPERTIES.put("scaleY", PreHoneycombCompat.SCALE_Y);
        ObjectAnimator.PROXY_PROPERTIES.put("scrollX", PreHoneycombCompat.SCROLL_X);
        ObjectAnimator.PROXY_PROPERTIES.put("scrollY", PreHoneycombCompat.SCROLL_Y);
        ObjectAnimator.PROXY_PROPERTIES.put("x", PreHoneycombCompat.X);
        ObjectAnimator.PROXY_PROPERTIES.put("y", PreHoneycombCompat.Y);
    }
    
    public ObjectAnimator() {
    }
    
    private ObjectAnimator(final Object mTarget, final String propertyName) {
        this.mTarget = mTarget;
        this.setPropertyName(propertyName);
    }
    
    public static ObjectAnimator ofFloat(final Object o, final String s, final float... floatValues) {
        final ObjectAnimator objectAnimator = new ObjectAnimator(o, s);
        objectAnimator.setFloatValues(floatValues);
        return objectAnimator;
    }
    
    @Override
    void animateValue(final float n) {
        super.animateValue(n);
        for (int length = this.mValues.length, i = 0; i < length; ++i) {
            this.mValues[i].setAnimatedValue(this.mTarget);
        }
    }
    
    @Override
    public ObjectAnimator clone() {
        return (ObjectAnimator)super.clone();
    }
    
    @Override
    void initAnimation() {
        if (!this.mInitialized) {
            if (this.mProperty == null && AnimatorProxy.NEEDS_PROXY && this.mTarget instanceof View && ObjectAnimator.PROXY_PROPERTIES.containsKey(this.mPropertyName)) {
                this.setProperty(ObjectAnimator.PROXY_PROPERTIES.get(this.mPropertyName));
            }
            for (int length = this.mValues.length, i = 0; i < length; ++i) {
                this.mValues[i].setupSetterAndGetter(this.mTarget);
            }
            super.initAnimation();
        }
    }
    
    @Override
    public ObjectAnimator setDuration(final long duration) {
        super.setDuration(duration);
        return this;
    }
    
    @Override
    public void setFloatValues(final float... floatValues) {
        if (this.mValues != null && this.mValues.length != 0) {
            super.setFloatValues(floatValues);
            return;
        }
        if (this.mProperty != null) {
            this.setValues(PropertyValuesHolder.ofFloat(this.mProperty, floatValues));
            return;
        }
        this.setValues(PropertyValuesHolder.ofFloat(this.mPropertyName, floatValues));
    }
    
    public void setProperty(final Property property) {
        if (this.mValues != null) {
            final PropertyValuesHolder propertyValuesHolder = this.mValues[0];
            final String propertyName = propertyValuesHolder.getPropertyName();
            propertyValuesHolder.setProperty(property);
            this.mValuesMap.remove(propertyName);
            this.mValuesMap.put(this.mPropertyName, propertyValuesHolder);
        }
        if (this.mProperty != null) {
            this.mPropertyName = property.getName();
        }
        this.mProperty = property;
        this.mInitialized = false;
    }
    
    public void setPropertyName(final String s) {
        if (this.mValues != null) {
            final PropertyValuesHolder propertyValuesHolder = this.mValues[0];
            final String propertyName = propertyValuesHolder.getPropertyName();
            propertyValuesHolder.setPropertyName(s);
            this.mValuesMap.remove(propertyName);
            this.mValuesMap.put(s, propertyValuesHolder);
        }
        this.mPropertyName = s;
        this.mInitialized = false;
    }
    
    @Override
    public void start() {
        super.start();
    }
    
    @Override
    public String toString() {
        String string2;
        String string = string2 = "ObjectAnimator@" + Integer.toHexString(this.hashCode()) + ", target " + this.mTarget;
        if (this.mValues != null) {
            int n = 0;
            while (true) {
                string2 = string;
                if (n >= this.mValues.length) {
                    break;
                }
                string = string + "\n    " + this.mValues[n].toString();
                ++n;
            }
        }
        return string2;
    }
}
