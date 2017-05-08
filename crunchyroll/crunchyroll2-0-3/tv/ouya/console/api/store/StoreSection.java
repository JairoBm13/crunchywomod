// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api.store;

import java.util.ArrayList;
import android.os.Parcel;
import java.util.List;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class StoreSection implements Parcelable
{
    public static final Parcelable$Creator<StoreSection> CREATOR;
    private String name;
    private List<StoreSection> subsections;
    private Type type;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<StoreSection>() {
            public StoreSection createFromParcel(final Parcel parcel) {
                final String string = parcel.readString();
                final String string2 = parcel.readString();
                final int int1 = parcel.readInt();
                final ArrayList list = new ArrayList<StoreSection>(int1);
                for (int i = 0; i < int1; ++i) {
                    list.add(this.createFromParcel(parcel));
                }
                return new StoreSection(string, string2, (List<StoreSection>)list);
            }
            
            public StoreSection[] newArray(final int n) {
                return new StoreSection[n];
            }
        };
    }
    
    public StoreSection() {
    }
    
    public StoreSection(final String name, final String s, final List<StoreSection> subsections) {
        this.name = name;
        this.type = Type.valueOf(s.toUpperCase());
        this.subsections = subsections;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof StoreSection)) {
                return false;
            }
            final StoreSection storeSection = (StoreSection)o;
            if (!this.name.equals(storeSection.name)) {
                return false;
            }
            if (!this.type.equals(storeSection.type)) {
                return false;
            }
            if (!this.subsections.equals(storeSection.subsections)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return (this.name.hashCode() * 31 + this.type.hashCode()) * 31 + this.subsections.hashCode();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.name);
        parcel.writeString(this.type.name().toUpperCase());
        parcel.writeInt(this.subsections.size());
        for (int i = 0; i < this.subsections.size(); ++i) {
            this.subsections.get(i).writeToParcel(parcel, n);
        }
    }
    
    public enum Type
    {
        LIST, 
        TAG;
    }
}
