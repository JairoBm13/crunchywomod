// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.os.Parcel;
import java.util.TimeZone;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Receipt implements Parcelable
{
    public static final Parcelable$Creator<Receipt> CREATOR;
    private static final SimpleDateFormat DATE_PARSER;
    private String gamer;
    private Date generatedDate;
    private String identifier;
    private int priceInCents;
    private Date purchaseDate;
    private String uuid;
    
    static {
        (DATE_PARSER = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss'Z'")).setTimeZone(TimeZone.getTimeZone("UTC"));
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Receipt>() {
            public Receipt createFromParcel(final Parcel parcel) {
                return new Receipt(parcel.readString(), parcel.readInt(), new Date(parcel.readLong()), new Date(parcel.readLong()), parcel.readString(), parcel.readString());
            }
            
            public Receipt[] newArray(final int n) {
                return new Receipt[n];
            }
        };
    }
    
    public Receipt() {
    }
    
    public Receipt(final String identifier, final int priceInCents, final Date purchaseDate, final Date generatedDate, final String gamer, final String uuid) {
        this.identifier = identifier;
        this.priceInCents = priceInCents;
        this.purchaseDate = purchaseDate;
        this.generatedDate = generatedDate;
        this.gamer = gamer;
        this.uuid = uuid;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof Receipt)) {
                return false;
            }
            final Receipt receipt = (Receipt)o;
            if (!this.identifier.equals(receipt.identifier)) {
                return false;
            }
            if (this.priceInCents != receipt.priceInCents) {
                return false;
            }
            if (!this.purchaseDate.equals(receipt.purchaseDate)) {
                return false;
            }
            if (!this.gamer.equals(receipt.gamer)) {
                return false;
            }
            if (!this.uuid.equals(receipt.uuid)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return (((this.identifier.hashCode() * 31 + this.priceInCents) * 31 + this.purchaseDate.hashCode()) * 31 + this.gamer.hashCode()) * 31 + this.uuid.hashCode();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.identifier);
        parcel.writeInt(this.priceInCents);
        parcel.writeLong(this.purchaseDate.getTime());
        parcel.writeLong(this.generatedDate.getTime());
        parcel.writeString(this.gamer);
        parcel.writeString(this.uuid);
    }
}
