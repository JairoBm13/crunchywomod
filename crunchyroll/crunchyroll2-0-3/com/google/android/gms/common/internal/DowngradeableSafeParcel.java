// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public abstract class DowngradeableSafeParcel implements SafeParcelable
{
    private static final Object zzZZ;
    private static ClassLoader zzaaa;
    private static Integer zzaab;
    private boolean zzaac;
    
    static {
        zzZZ = new Object();
        DowngradeableSafeParcel.zzaaa = null;
        DowngradeableSafeParcel.zzaab = null;
    }
    
    public DowngradeableSafeParcel() {
        this.zzaac = false;
    }
}
