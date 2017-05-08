// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Purchasable implements Parcelable
{
    public static final Parcelable$Creator<Purchasable> CREATOR;
    private String mIV;
    private String mKey;
    private String mPayload;
    private String productId;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Purchasable>() {
            public Purchasable createFromParcel(final Parcel parcel) {
                final String string = parcel.readString();
                switch (parcel.readByte()) {
                    default: {
                        throw new IllegalArgumentException("Unable to rebuild purchasable. Encryption data state unknown");
                    }
                    case 1: {
                        return new Purchasable(string);
                    }
                    case 2: {
                        return new Purchasable(string, parcel.readString(), parcel.readString(), parcel.readString());
                    }
                }
            }
            
            public Purchasable[] newArray(final int n) {
                return new Purchasable[n];
            }
        };
    }
    
    public Purchasable(final String productId) {
        this.productId = productId;
    }
    
    public Purchasable(final String productId, final String mKey, final String miv, final String mPayload) {
        this.productId = productId;
        this.mKey = mKey;
        this.mIV = miv;
        this.mPayload = mPayload;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.productId.equals(((Purchasable)o).productId));
    }
    
    public boolean hasEncryptionParameters() {
        return this.mKey != null && this.mIV != null && this.mPayload != null;
    }
    
    @Override
    public int hashCode() {
        return this.productId.hashCode();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.productId);
        if (this.hasEncryptionParameters()) {
            parcel.writeByte((byte)2);
            parcel.writeString(this.mKey);
            parcel.writeString(this.mIV);
            parcel.writeString(this.mPayload);
            return;
        }
        parcel.writeByte((byte)1);
    }
}
