// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core.internal.models;

public class SignalData
{
    public final String code;
    public final long faultAddress;
    public final String name;
    
    public SignalData(final String name, final String code, final long faultAddress) {
        this.name = name;
        this.code = code;
        this.faultAddress = faultAddress;
    }
}
