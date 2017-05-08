// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api.store;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class Tag implements Parcelable
{
    public static final Parcelable$Creator<Tag> CREATOR;
    public Boolean adminOnly;
    private String imageUrl;
    private String name;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Tag>() {
            public Tag createFromParcel(final Parcel parcel) {
                boolean b = true;
                final String string = parcel.readString();
                if (parcel.readInt() != 1) {
                    b = false;
                }
                return new Tag(string, b, parcel.readString());
            }
            
            public Tag[] newArray(final int n) {
                return new Tag[n];
            }
        };
    }
    
    public Tag() {
    }
    
    public Tag(final String name, final Boolean adminOnly, final String imageUrl) {
        this.name = name;
        this.adminOnly = adminOnly;
        this.imageUrl = imageUrl;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof Tag)) {
                return false;
            }
            final Tag tag = (Tag)o;
            if (!this.name.equals(tag.name)) {
                return false;
            }
            if (this.adminOnly != this.adminOnly) {
                return false;
            }
            if (!this.imageUrl.equals(tag.imageUrl)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return (this.name.hashCode() * 31 + this.adminOnly.hashCode()) * 31 + this.imageUrl.hashCode();
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeString(this.name);
        if (this.adminOnly) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeString(this.imageUrl);
    }
}
