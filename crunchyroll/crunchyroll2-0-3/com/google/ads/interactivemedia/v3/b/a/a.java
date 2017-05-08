// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.a;

import java.util.Arrays;
import com.google.ads.interactivemedia.v3.api.Ad;

public class a implements Ad
{
    private String adId;
    private b adPodInfo;
    private String adSystem;
    private String[] adWrapperIds;
    private String[] adWrapperSystems;
    private String clickThroughUrl;
    private double duration;
    private int height;
    private boolean linear;
    private boolean skippable;
    private String traffickingParameters;
    private int width;
    
    public a() {
        this.adPodInfo = new b();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final a a = (a)o;
            if (this.adId == null) {
                if (a.adId != null) {
                    return false;
                }
            }
            else if (!this.adId.equals(a.adId)) {
                return false;
            }
            if (this.adSystem == null) {
                if (a.adSystem != null) {
                    return false;
                }
            }
            else if (!this.adSystem.equals(a.adSystem)) {
                return false;
            }
            if (this.adPodInfo == null) {
                if (a.adPodInfo != null) {
                    return false;
                }
            }
            else if (!this.adPodInfo.equals(a.adPodInfo)) {
                return false;
            }
            if (this.clickThroughUrl == null) {
                if (a.clickThroughUrl != null) {
                    return false;
                }
            }
            else if (!this.clickThroughUrl.equals(a.clickThroughUrl)) {
                return false;
            }
            if (Double.doubleToLongBits(this.duration) != Double.doubleToLongBits(a.duration)) {
                return false;
            }
            if (this.height != a.height) {
                return false;
            }
            if (this.linear != a.linear) {
                return false;
            }
            if (this.traffickingParameters == null) {
                if (a.traffickingParameters != null) {
                    return false;
                }
            }
            else if (!this.traffickingParameters.equals(a.traffickingParameters)) {
                return false;
            }
            if (this.width != a.width) {
                return false;
            }
        }
        return true;
    }
    
    public String getClickThruUrl() {
        return this.clickThroughUrl;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.adId == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.adId.hashCode();
        }
        int hashCode3;
        if (this.adPodInfo == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.adPodInfo.hashCode();
        }
        int hashCode4;
        if (this.clickThroughUrl == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.clickThroughUrl.hashCode();
        }
        final long doubleToLongBits = Double.doubleToLongBits(this.duration);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final int height = this.height;
        int n2;
        if (this.linear) {
            n2 = 1231;
        }
        else {
            n2 = 1237;
        }
        if (this.traffickingParameters != null) {
            hashCode = this.traffickingParameters.hashCode();
        }
        return ((n2 + (((hashCode4 + (hashCode3 + (hashCode2 + 31) * 31) * 31) * 31 + n) * 31 + height) * 31) * 31 + hashCode) * 31 + this.width;
    }
    
    public boolean isLinear() {
        return this.linear;
    }
    
    @Override
    public boolean isSkippable() {
        return this.skippable;
    }
    
    @Override
    public String toString() {
        return "Ad [adId=" + this.adId + ", adWrapperIds=" + Arrays.toString(this.adWrapperIds) + ", adWrapperSystems=" + Arrays.toString(this.adWrapperSystems) + ", adSystem=" + this.adSystem + ", linear=" + this.linear + ", skippable=" + this.skippable + ", width=" + this.width + ", height=" + this.height + ", traffickingParameters=" + this.traffickingParameters + ", clickThroughUrl=" + this.clickThroughUrl + ", duration=" + this.duration + ", adPodInfo=" + this.adPodInfo + "]";
    }
}
