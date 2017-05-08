// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

class AdvertisingInfo
{
    public final String advertisingId;
    public final boolean limitAdTrackingEnabled;
    
    AdvertisingInfo(final String advertisingId, final boolean limitAdTrackingEnabled) {
        this.advertisingId = advertisingId;
        this.limitAdTrackingEnabled = limitAdTrackingEnabled;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final AdvertisingInfo advertisingInfo = (AdvertisingInfo)o;
            if (this.limitAdTrackingEnabled != advertisingInfo.limitAdTrackingEnabled) {
                return false;
            }
            if (this.advertisingId != null) {
                if (this.advertisingId.equals(advertisingInfo.advertisingId)) {
                    return true;
                }
            }
            else if (advertisingInfo.advertisingId == null) {
                return true;
            }
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        int hashCode;
        if (this.advertisingId != null) {
            hashCode = this.advertisingId.hashCode();
        }
        else {
            hashCode = 0;
        }
        if (this.limitAdTrackingEnabled) {
            n = 1;
        }
        return hashCode * 31 + n;
    }
}
