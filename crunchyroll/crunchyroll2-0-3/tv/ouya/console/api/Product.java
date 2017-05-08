// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Product implements Parcelable
{
    public static final Parcelable$Creator<Product> CREATOR;
    private String identifier;
    private String name;
    private int priceInCents;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Product>() {
            public Product createFromParcel(final Parcel parcel) {
                return new Product(parcel.readString(), parcel.readString(), parcel.readInt());
            }
            
            public Product[] newArray(final int n) {
                return new Product[n];
            }
        };
    }
    
    public Product() {
    }
    
    public Product(final String identifier, final String name, final int priceInCents) {
        this.identifier = identifier;
        this.name = name;
        this.priceInCents = priceInCents;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof Product)) {
                return false;
            }
            final Product product = (Product)o;
            if (this.priceInCents != product.priceInCents) {
                return false;
            }
            if (!this.name.equals(product.name)) {
                return false;
            }
            if (!this.identifier.equals(product.identifier)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return (this.identifier.hashCode() * 31 + this.name.hashCode()) * 31 + this.priceInCents;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.identifier);
        parcel.writeString(this.name);
        parcel.writeInt(this.priceInCents);
    }
}
