// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.util.Log;
import com.google.android.gms.analytics.Logger;

class zzs implements Logger
{
    private boolean zzIz;
    private int zzKR;
    
    zzs() {
        this.zzKR = 2;
    }
    
    @Override
    public void error(final String s) {
    }
    
    @Override
    public int getLogLevel() {
        return this.zzKR;
    }
    
    @Override
    public void setLogLevel(final int zzKR) {
        this.zzKR = zzKR;
        if (!this.zzIz) {
            Log.i((String)zzy.zzLb.get(), "Logger is deprecated. To enable debug logging, please run:\nadb shell setprop log.tag." + zzy.zzLb.get() + " DEBUG");
            this.zzIz = true;
        }
    }
    
    @Override
    public void warn(final String s) {
    }
}
