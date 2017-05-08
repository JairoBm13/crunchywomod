// 
// Decompiled by Procyon v0.5.30
// 

package com.nineoldandroids.animation;

import java.util.ArrayList;

public abstract class Animator implements Cloneable
{
    ArrayList<AnimatorListener> mListeners;
    
    public Animator() {
        this.mListeners = null;
    }
    
    public void addListener(final AnimatorListener animatorListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<AnimatorListener>();
        }
        this.mListeners.add(animatorListener);
    }
    
    public Animator clone() {
        Animator animator;
        try {
            animator = (Animator)super.clone();
            if (this.mListeners != null) {
                final ArrayList<AnimatorListener> mListeners = this.mListeners;
                animator.mListeners = new ArrayList<AnimatorListener>();
                for (int size = mListeners.size(), i = 0; i < size; ++i) {
                    animator.mListeners.add(mListeners.get(i));
                }
            }
        }
        catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
        return animator;
    }
    
    public ArrayList<AnimatorListener> getListeners() {
        return this.mListeners;
    }
    
    public void removeListener(final AnimatorListener animatorListener) {
        if (this.mListeners != null) {
            this.mListeners.remove(animatorListener);
            if (this.mListeners.size() == 0) {
                this.mListeners = null;
            }
        }
    }
    
    public void start() {
    }
    
    public interface AnimatorListener
    {
        void onAnimationEnd(final Animator p0);
        
        void onAnimationRepeat(final Animator p0);
        
        void onAnimationStart(final Animator p0);
    }
}
