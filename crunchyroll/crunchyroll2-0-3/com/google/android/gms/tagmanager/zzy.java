// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import android.util.Log;

public class zzy implements zzbh
{
    private int zzKR;
    
    public zzy() {
        this.zzKR = 5;
    }
    
    @Override
    public void zzaA(final String s) {
        if (this.zzKR <= 4) {
            Log.i("GoogleTagManager", s);
        }
    }
    
    @Override
    public void zzaB(final String s) {
        if (this.zzKR <= 2) {
            Log.v("GoogleTagManager", s);
        }
    }
    
    @Override
    public void zzaC(final String s) {
        if (this.zzKR <= 5) {
            Log.w("GoogleTagManager", s);
        }
    }
    
    @Override
    public void zzaz(final String s) {
        if (this.zzKR <= 6) {
            Log.e("GoogleTagManager", s);
        }
    }
    
    @Override
    public void zzb(final String s, final Throwable t) {
        if (this.zzKR <= 6) {
            Log.e("GoogleTagManager", s, t);
        }
    }
}
