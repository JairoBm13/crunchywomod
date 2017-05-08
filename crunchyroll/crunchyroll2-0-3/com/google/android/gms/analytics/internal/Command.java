// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Command implements Parcelable
{
    @Deprecated
    public static final Parcelable$Creator<Command> CREATOR;
    private String mValue;
    private String zzKI;
    private String zzKJ;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Command>() {
            @Deprecated
            public Command[] zzaa(final int n) {
                return new Command[n];
            }
            
            @Deprecated
            public Command zzq(final Parcel parcel) {
                return new Command(parcel);
            }
        };
    }
    
    public Command() {
    }
    
    Command(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    @Deprecated
    private void readFromParcel(final Parcel parcel) {
        this.zzKI = parcel.readString();
        this.zzKJ = parcel.readString();
        this.mValue = parcel.readString();
    }
    
    @Deprecated
    public int describeContents() {
        return 0;
    }
    
    public String getId() {
        return this.zzKI;
    }
    
    public String getValue() {
        return this.mValue;
    }
    
    @Deprecated
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.zzKI);
        parcel.writeString(this.zzKJ);
        parcel.writeString(this.mValue);
    }
}
