// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class AdRoll implements Serializable
{
    private static final long serialVersionUID = -4741240216792028332L;
    boolean mIsShown;
    double mPlayhead;
    List<AdSlot> mSlots;
    
    public AdRoll(final double mPlayhead) {
        this.mPlayhead = mPlayhead;
        this.mSlots = new ArrayList<AdSlot>();
        this.mIsShown = false;
    }
    
    public void addSlot(final AdSlot adSlot) {
        this.mSlots.add(adSlot);
    }
    
    public double getPlayhead() {
        return this.mPlayhead;
    }
    
    public List<AdSlot> getSlots() {
        return this.mSlots;
    }
    
    public void setShown(final boolean mIsShown) {
        this.mIsShown = mIsShown;
    }
    
    @Override
    public String toString() {
        String string = "AdRoll [";
        for (int i = 0; i < this.mSlots.size(); ++i) {
            final AdSlot adSlot = this.mSlots.get(i);
            String string2 = string;
            if (i > 0) {
                string2 = string + ", ";
            }
            string = string2 + adSlot.toString();
        }
        return string + "]";
    }
}
