// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.device;

import android.content.Context;
import android.telephony.TelephonyManager;

public class AndroidTelephonyManagerWrapper implements ITelephonyManager
{
    private final TelephonyManager manager;
    
    public AndroidTelephonyManagerWrapper(final Context context) {
        this.manager = (TelephonyManager)context.getSystemService("phone");
    }
    
    @Override
    public String getSimCountryIso() {
        if (this.manager != null) {
            return this.manager.getSimCountryIso();
        }
        return null;
    }
    
    @Override
    public String getSimOperator() {
        if (this.manager != null) {
            return this.manager.getSimOperator();
        }
        return null;
    }
    
    @Override
    public String getSimOperatorName() {
        if (this.manager != null) {
            return this.manager.getSimOperatorName();
        }
        return null;
    }
}
