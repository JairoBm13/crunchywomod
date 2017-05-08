// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import android.os.Build$VERSION;

public final class zzlk
{
    private static boolean zzbR(final int n) {
        return Build$VERSION.SDK_INT >= n;
    }
    
    public static boolean zzoR() {
        return zzbR(11);
    }
    
    public static boolean zzoT() {
        return zzbR(13);
    }
    
    public static boolean zzoU() {
        return zzbR(14);
    }
    
    public static boolean zzoV() {
        return zzbR(16);
    }
    
    public static boolean zzoX() {
        return zzbR(19);
    }
    
    public static boolean zzoY() {
        return zzbR(20);
    }
    
    public static boolean zzoZ() {
        return zzbR(21);
    }
}
