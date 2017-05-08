// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import android.content.Intent;

public class AdHandler
{
    private final AdRoll mAdRoll;
    private AdShower mAdShower;
    private AdTracker mAdTracker;
    private int mCurrentAdOption;
    private int mCurrentAdSlot;
    private final DeepLinker mDeepLinker;
    private int mMaxAdStartSeconds;
    private int mPreviousAdSlot;
    private int mStartPlayTimeInSeconds;
    
    public AdHandler(final AdTracker mAdTracker, final AdShower mAdShower, final AdRoll mAdRoll, final int mMaxAdStartSeconds, final DeepLinker mDeepLinker) {
        this.mAdTracker = mAdTracker;
        this.mAdShower = mAdShower;
        this.mAdRoll = mAdRoll;
        this.mStartPlayTimeInSeconds = this.getCurrentTimeInSeconds();
        this.mMaxAdStartSeconds = mMaxAdStartSeconds;
        this.mPreviousAdSlot = -1;
        this.mCurrentAdSlot = 0;
        this.mCurrentAdOption = 0;
        this.mDeepLinker = mDeepLinker;
    }
    
    public AdHandler(final AdTracker adTracker, final AdRoll adRoll, final int n) {
        this(adTracker, null, adRoll, n, null);
    }
    
    public AdHandler(final AdRoll adRoll, final int n) {
        this(null, null, adRoll, n, null);
    }
    
    private int getCurrentTimeInSeconds() {
        return (int)(System.currentTimeMillis() / 1000L);
    }
    
    public void next() {
        ++this.mCurrentAdOption;
        if (this.mCurrentAdSlot < this.mAdRoll.getSlots().size() && this.mCurrentAdOption >= this.mAdRoll.getSlots().get(this.mCurrentAdSlot).getOptions().size()) {
            ++this.mCurrentAdSlot;
            this.mCurrentAdOption = 0;
        }
        this.play();
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == 45242) {
            ++this.mCurrentAdSlot;
            this.mCurrentAdOption = 0;
            this.play();
            return;
        }
        this.next();
    }
    
    public void play() {
        if (this.mCurrentAdSlot >= this.mAdRoll.getSlots().size()) {
            this.mAdShower.finishAds();
            return;
        }
        if (this.mPreviousAdSlot < this.mCurrentAdSlot && this.mAdTracker != null) {
            this.mAdTracker.onAdSlotStart();
        }
        this.mPreviousAdSlot = this.mCurrentAdSlot;
        if (this.getCurrentTimeInSeconds() - this.mStartPlayTimeInSeconds < this.mMaxAdStartSeconds) {
            this.mAdShower.showAd(this.mAdRoll.getSlots().get(this.mCurrentAdSlot).getOptions().get(this.mCurrentAdOption));
            return;
        }
        this.mAdShower.finishAds();
    }
    
    public void setAdShower(final AdShower mAdShower) {
        this.mAdShower = mAdShower;
    }
    
    public interface AdShower
    {
        void finishAds();
        
        void showAd(final AdOption p0);
    }
    
    public interface AdTracker
    {
        void onAdSlotStart();
    }
}
