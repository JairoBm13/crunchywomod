// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class CreditCardInfo implements Parcelable
{
    public static final Parcelable$Creator<CreditCardInfo> CREATOR;
    private double balance;
    private String expiresAt;
    private String lastFourDigits;
    private String provider;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<CreditCardInfo>() {
            public CreditCardInfo createFromParcel(final Parcel parcel) {
                return new CreditCardInfo(parcel.readDouble(), parcel.readString(), parcel.readString(), parcel.readString());
            }
            
            public CreditCardInfo[] newArray(final int n) {
                return new CreditCardInfo[n];
            }
        };
    }
    
    public CreditCardInfo() {
    }
    
    public CreditCardInfo(final double balance, final String lastFourDigits, final String provider, final String expiresAt) {
        this.balance = balance;
        this.lastFourDigits = lastFourDigits;
        this.provider = provider;
        this.expiresAt = expiresAt;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof CreditCardInfo)) {
                return false;
            }
            final CreditCardInfo creditCardInfo = (CreditCardInfo)o;
            if (this.balance != creditCardInfo.balance) {
                return false;
            }
            if (!this.lastFourDigits.equals(creditCardInfo.lastFourDigits)) {
                return false;
            }
            if (!this.provider.equals(creditCardInfo.provider)) {
                return false;
            }
            if (!this.expiresAt.equals(creditCardInfo.expiresAt)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        final int n = hashCode = Double.valueOf(this.balance).hashCode();
        if (this.lastFourDigits != null) {
            hashCode = n * 31 + this.lastFourDigits.hashCode();
        }
        int n2 = hashCode;
        if (this.provider != null) {
            n2 = hashCode * 31 + this.provider.hashCode();
        }
        int n3 = n2;
        if (this.expiresAt != null) {
            n3 = n2 * 31 + this.expiresAt.hashCode();
        }
        return n3;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeDouble(this.balance);
        parcel.writeString(this.lastFourDigits);
        parcel.writeString(this.provider);
        parcel.writeString(this.expiresAt);
    }
}
