// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import android.content.ActivityNotFoundException;
import android.util.Log;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.app.Activity;
import android.content.DialogInterface$OnClickListener;

public class zzg implements DialogInterface$OnClickListener
{
    private final Activity mActivity;
    private final Intent mIntent;
    private final Fragment zzZX;
    private final int zzZY;
    
    public zzg(final Activity mActivity, final Intent mIntent, final int zzZY) {
        this.mActivity = mActivity;
        this.zzZX = null;
        this.mIntent = mIntent;
        this.zzZY = zzZY;
    }
    
    public zzg(final Fragment zzZX, final Intent mIntent, final int zzZY) {
        this.mActivity = null;
        this.zzZX = zzZX;
        this.mIntent = mIntent;
        this.zzZY = zzZY;
    }
    
    public void onClick(final DialogInterface dialogInterface, final int n) {
        try {
            if (this.mIntent != null && this.zzZX != null) {
                this.zzZX.startActivityForResult(this.mIntent, this.zzZY);
            }
            else if (this.mIntent != null) {
                this.mActivity.startActivityForResult(this.mIntent, this.zzZY);
            }
            dialogInterface.dismiss();
        }
        catch (ActivityNotFoundException ex) {
            Log.e("SettingsRedirect", "Can't redirect to app settings for Google Play services");
        }
    }
}
