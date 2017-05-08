// 
// Decompiled by Procyon v0.5.30
// 

package com.secondtv.android.ads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import android.os.Bundle;
import java.util.List;
import android.app.Activity;

public final class AdTrigger implements Runnable
{
    private Activity mActivity;
    private List<AdRoll> mAdRolls;
    private AdTriggerProvider mAdTriggerProvider;
    private DeepLinker mDeepLinker;
    private int mImplementation;
    private boolean mIsShowPreroll;
    private int mMaxAdStartSeconds;
    private int mPreviousTriggerPositionMs;
    private AdTrackListener mTrackListener;
    
    public AdTrigger(final Activity mActivity, final AdTriggerProvider mAdTriggerProvider, final Bundle bundle, final List<AdSlot> list, final AdTrackListener mTrackListener, final int mMaxAdStartSeconds, final boolean mIsShowPreroll, final DeepLinker mDeepLinker, final int mImplementation) {
        this.mActivity = mActivity;
        this.mAdTriggerProvider = mAdTriggerProvider;
        this.mTrackListener = mTrackListener;
        this.mDeepLinker = mDeepLinker;
        this.mIsShowPreroll = mIsShowPreroll;
        this.mImplementation = mImplementation;
        HashSet<Double> set = new HashSet<Double>();
        if (bundle != null) {
            final HashSet set2 = (HashSet)bundle.getBundle("AdTrigger").getSerializable("shownAdSlots");
            set = set;
            if (set2 != null) {
                set = (HashSet<Double>)set2;
            }
        }
        this.mMaxAdStartSeconds = mMaxAdStartSeconds;
        this.mPreviousTriggerPositionMs = -1;
        this.createAdRolls(list, set);
    }
    
    private AdRoll getClosestMidRoll(final int n) {
        Label_0039: {
            if (this.mPreviousTriggerPositionMs <= 0) {
                break Label_0039;
            }
            final int n2 = n - this.mPreviousTriggerPositionMs;
            if (n2 >= 900 && n2 <= 1100) {
                break Label_0039;
            }
            this.mPreviousTriggerPositionMs = n;
            return null;
        }
        this.mPreviousTriggerPositionMs = n;
        AdRoll adRoll2 = null;
        for (final AdRoll adRoll3 : this.mAdRolls) {
            if (adRoll3.getPlayhead() * 1000.0 <= n && (adRoll2 == null || adRoll3.getPlayhead() > adRoll2.getPlayhead())) {
                adRoll2 = adRoll3;
            }
        }
        AdRoll adRoll;
        if ((adRoll = adRoll2) == null) {
            return adRoll;
        }
        adRoll = adRoll2;
        if (adRoll2.getPlayhead() == 0.0) {
            return null;
        }
        return adRoll;
    }
    
    private AdRoll getPreRoll() {
        for (final AdRoll adRoll : this.mAdRolls) {
            if (adRoll.getPlayhead() == 0.0) {
                return adRoll;
            }
        }
        return null;
    }
    
    private boolean playAdRoll(final AdRoll adRoll) {
        if (adRoll != null && !adRoll.mIsShown) {
            if (this.mAdTriggerProvider != null) {
                this.mAdTriggerProvider.onAdStart();
            }
            switch (this.mImplementation) {
                case 0: {
                    AdsActivity.start(this.mActivity, adRoll, this.mTrackListener, this.mMaxAdStartSeconds, this.mDeepLinker);
                    break;
                }
                case 1: {
                    AdsActivityTremorWorkaround.start(this.mActivity, adRoll, this.mTrackListener, this.mMaxAdStartSeconds, this.mDeepLinker);
                    break;
                }
            }
            adRoll.setShown(true);
            return true;
        }
        return false;
    }
    
    void createAdRolls(final List<AdSlot> list, final HashSet<Double> set) {
        this.mAdRolls = new ArrayList<AdRoll>();
        for (final AdSlot adSlot : list) {
            AdRoll adRoll;
            if ((adRoll = this.getAdRoll(adSlot.getPlayhead())) == null) {
                adRoll = new AdRoll(adSlot.getPlayhead());
                if (set.contains(adSlot.getPlayhead())) {
                    adRoll.setShown(true);
                }
                this.mAdRolls.add(adRoll);
            }
            adRoll.getSlots().add(adSlot);
        }
    }
    
    AdRoll getAdRoll(final double n) {
        if (this.mAdRolls != null) {
            for (final AdRoll adRoll : this.mAdRolls) {
                if (adRoll.getPlayhead() == n) {
                    return adRoll;
                }
            }
        }
        return null;
    }
    
    public void playPreRoll() {
        if (this.mIsShowPreroll) {
            this.playAdRoll(this.getPreRoll());
        }
    }
    
    @Override
    public final void run() {
        try {
            if (this.mAdTriggerProvider != null && this.mAdTriggerProvider.isReady() && this.mAdTriggerProvider.isPlaying()) {
                this.playAdRoll(this.getClosestMidRoll(this.mAdTriggerProvider.getPlayhead()));
            }
        }
        catch (Exception ex) {}
    }
    
    public void saveInstanceState(final Bundle bundle) {
        if (bundle != null) {
            final HashSet<Double> set = new HashSet<Double>();
            for (final AdRoll adRoll : this.mAdRolls) {
                if (adRoll.mIsShown) {
                    set.add(adRoll.getPlayhead());
                }
            }
            final Bundle bundle2 = new Bundle();
            bundle2.putSerializable("shownAdSlots", (Serializable)set);
            bundle.putBundle("AdTrigger", bundle2);
        }
    }
    
    public void setClosestAdRollToShown(final int n) {
        final AdRoll closestMidRoll = this.getClosestMidRoll(n);
        if (closestMidRoll != null) {
            closestMidRoll.setShown(true);
        }
    }
}
