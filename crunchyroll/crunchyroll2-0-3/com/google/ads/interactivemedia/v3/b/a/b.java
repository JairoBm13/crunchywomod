// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.a;

public class b
{
    public int adPosition;
    public boolean isBumper;
    public double maxDuration;
    public int podIndex;
    public double timeOffset;
    public int totalAds;
    
    public b() {
        this.totalAds = 1;
        this.adPosition = 1;
        this.isBumper = false;
        this.maxDuration = -1.0;
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
            final b b = (b)o;
            if (this.adPosition != b.adPosition) {
                return false;
            }
            if (this.isBumper != b.isBumper) {
                return false;
            }
            if (this.totalAds != b.totalAds) {
                return false;
            }
            if (this.maxDuration != b.maxDuration) {
                return false;
            }
            if (this.podIndex != b.podIndex) {
                return false;
            }
            if (this.timeOffset != b.timeOffset) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int adPosition = this.adPosition;
        int n;
        if (this.isBumper) {
            n = 1231;
        }
        else {
            n = 1237;
        }
        final int totalAds = this.totalAds;
        final long doubleToLongBits = Double.doubleToLongBits(this.maxDuration);
        final int n2 = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final int podIndex = this.podIndex;
        final long doubleToLongBits2 = Double.doubleToLongBits(this.timeOffset);
        return ((((n + (adPosition + 31) * 31) * 31 + totalAds) * 31 + n2) * 31 + podIndex) * 31 + (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
    }
    
    @Override
    public String toString() {
        return "AdPodInfo [totalAds=" + this.totalAds + ", adPosition=" + this.adPosition + ", isBumper=" + this.isBumper + ", maxDuration=" + this.maxDuration + ", podIndex=" + this.podIndex + ", timeOffset=" + this.timeOffset + "]";
    }
}
